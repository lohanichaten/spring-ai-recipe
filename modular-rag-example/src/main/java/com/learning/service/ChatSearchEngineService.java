package com.learning.service;

import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.learning.component.SearchEngineDocumentRetriever;

import reactor.core.publisher.Flux;

@Service
public class ChatSearchEngineService implements AiService{

	private final String CONVERSATION_ID = UUID.randomUUID().toString();
	private final ChatClient chatClient;
	
	
	 public ChatSearchEngineService(ChatClient.Builder chatClientBuilder, RestClient.Builder restClientBuilder) {
		 
		   this.chatClient = chatClientBuilder
				   					.defaultAdvisors(RetrievalAugmentationAdvisor.builder()
				   							.queryTransformers(RewriteQueryTransformer.builder()
				   												.chatClientBuilder(chatClientBuilder.clone())
				   												.targetSearchSystem("web search engine")
				   												.build())
				   							.documentRetriever(SearchEngineDocumentRetriever.builder()
				   												.restClientBuilder(restClientBuilder)
				   												.maxResults(10)
				   												.build())
				   							.queryAugmenter(ContextualQueryAugmenter.builder()
				   											.allowEmptyContext(false)
				   											.build())
				   							.build())
				   					.build();
	 }
	
	
	@Override
	public String chat(String input) {
		
		return chatClient.prompt()
						.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, CONVERSATION_ID))
						.user(input)
						.call()
						.content();
	}

	
	@Override
    public Flux<String> stream(String input) {
        return chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, CONVERSATION_ID))
                .user(input)
                .stream()
                .content();
    }
}
