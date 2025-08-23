package com.learning.service;

import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.learning.component.CompressionDocumentPostProcessor;

import reactor.core.publisher.Flux;

@Service
public class ChatPostProcessService implements AiService {
	
	
	 private final String CONVERSATION_ID = UUID.randomUUID().toString();
	 private final ChatClient chatClient;

	 
	 

	 public ChatPostProcessService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
	        this.chatClient = chatClientBuilder
	                .defaultAdvisors(RetrievalAugmentationAdvisor.builder()
	                        .documentRetriever(VectorStoreDocumentRetriever.builder()
	                                .vectorStore(vectorStore)
	                                .similarityThreshold(0.5)
	                                .topK(3)
	                                .build())
	                        .documentPostProcessors(CompressionDocumentPostProcessor.builder()
	                                .chatClientBuilder(chatClientBuilder.clone())
	                                .build())
	                        .build())
	                .build();
	    }

	    @Override
	    public Flux<String> stream(String input) {
	        return chatClient.prompt()
	                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, CONVERSATION_ID))
	                .user(input)
	                .stream()
	                .content();
	    }

	    @Override
	    @Nullable
	    public String chat(String input) {
	        return chatClient.prompt()
	                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, CONVERSATION_ID))
	                .user(input)
	                .call()
	                .content();
	    }

}
