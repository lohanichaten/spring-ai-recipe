package com.learning.musicapp.advisor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.neo4j.core.Neo4jTemplate;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class CountMentionsAdvisor implements CallAdvisor{

	private final Neo4jTemplate neo4jTemplate;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	
	
	
	@Override
	public String getName() {
		return "NoteMentions";
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
	   var mentions = neo4jTemplate.findAll(Mentions.class);
	   String userText= chatClientRequest.copy().prompt().getUserMessage().getText();
	   List<Mentions> mentioned = mentions.stream().filter(mention->  userText.contains(mention.getName())).collect(Collectors.toList());
	    		
	   for(Mentions mention:  mentioned) {
	            noteMention(mention);
	    }
	      
		return callAdvisorChain.nextCall(chatClientRequest);
	}

	private void noteMention(Mentions mention) {
		this.applicationEventPublisher.publishEvent(mention);
		
		 Optional<Mentions> mentionOptional = neo4jTemplate.findById(mention.getName(), Mentions.class);
		 if(mentionOptional.isPresent()) {
			 mention=mentionOptional.get();
			 mention.increment();
			 neo4jTemplate.save(mention);
		 }
			      
	}
	
}
