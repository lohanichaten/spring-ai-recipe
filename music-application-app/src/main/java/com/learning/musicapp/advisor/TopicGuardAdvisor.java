package com.learning.musicapp.advisor;

import java.util.List;
import java.util.Set;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

import lombok.extern.slf4j.Slf4j;



@Slf4j
public class TopicGuardAdvisor implements CallAdvisor{
	
	private final ChatClient chatClient;
	private final Set<Topic> bannedTopics;
	private final UserContentExtractor contentExtractor;
	private final RetryTemplate retryTemplate ;
	
	
	public TopicGuardAdvisor(ChatModel chatModel, Set<Topic> bannedTopics, UserContentExtractor contentExtractor
			) {
		super();
		this.chatClient = ChatClient.builder(chatModel).build();
		this.bannedTopics = bannedTopics;
		this.contentExtractor = contentExtractor;
		this.retryTemplate =new 	RetryTemplateBuilder().maxAttempts(3).fixedBackoff(1000).build();;
	}
	
	@Override
	public String getName() {
		return TopicGuardAdvisor.class.getSimpleName();
	}

	@Override
	public int getOrder() {
		
		return 0;
	}

	@Override
	public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
		Boolean isTopicBanned=false;
		try {
					isTopicBanned= this.retryTemplate.execute((Boolean)-> isBannedTopic(chatClientRequest.prompt().getUserMessage().getText()));
				
		}catch(Exception t) {
            log.error("We tried really hard but the model kept failing. Don't fail the advisor chain", t);
		}
		
		if(isTopicBanned) {
	        List<Generation> generations=List.of(new Generation(new AssistantMessage("I'm sorry, but I can only help you with Classical music.")));
			return ChatClientResponse.builder().chatResponse(ChatResponse.builder().generations(generations).build()).build();
			
		}
		return callAdvisorChain.nextCall(chatClientRequest);
	}


	private Boolean isBannedTopic(String userContent) {
		TopicClassification classification=chatClient
								.prompt()
								.user(u-> u.text(new ClassPathResource("prompt/topic_guard.md"))
												.param("content", userContent))
								.call()
								.entity(TopicClassification.class);
		
		   log.info("User content '$userContent' classified as {}",classification.getTopic());
		   return bannedTopics.contains(classification.getTopic());
	}


}
