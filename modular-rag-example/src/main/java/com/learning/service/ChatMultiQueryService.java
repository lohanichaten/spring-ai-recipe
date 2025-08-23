package com.learning.service;

import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.task.TaskExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class ChatMultiQueryService implements AiService {
	
	  private final String CONVERSATION_ID = UUID.randomUUID().toString();
	  private final ChatClient chatClient;


	    public ChatMultiQueryService(ChatClient.Builder chatClientBuilder, TaskExecutor taskExecutor, VectorStore vectorStore) {
	        this.chatClient = chatClientBuilder
	                .defaultAdvisors(RetrievalAugmentationAdvisor.builder()
	                        .queryExpander(MultiQueryExpander.builder()
	                                .chatClientBuilder(chatClientBuilder.clone())
	                                .numberOfQueries(3)
	                                .build())
	                        .documentRetriever(VectorStoreDocumentRetriever.builder()
	                                .vectorStore(vectorStore)
	                                .similarityThreshold(0.5)
	                                .topK(3)
	                                .build())
	                        .documentJoiner(new ConcatenationDocumentJoiner())
	                        .queryAugmenter(ContextualQueryAugmenter.builder()
	                                .allowEmptyContext(true)
	                                .build())
	                        .taskExecutor(taskExecutor)
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
