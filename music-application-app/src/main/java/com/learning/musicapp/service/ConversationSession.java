package com.learning.musicapp.service;

import java.util.List;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


@Component
@SessionScope

public class ConversationSession {

	private final ChatMemory chatMemory;
	private final NameGenerator nameGenerator;
	
	@Value(value = "classpath:prompts/system_prompt.md")
	private Resource prompt;
	String conversationId;
	
	
	
	public ConversationSession(ChatMemory chatMemory, NameGenerator nameGenerator) {
		
		this.chatMemory = chatMemory;
		this.nameGenerator = nameGenerator;
		 conversationId  = nameGenerator.generateName();
	}



	List<Message> messages(){
		return chatMemory.get(conversationId);
	}

	Resource promptResource() {
		return this.prompt;
	}



	public ChatMemory getChatMemory() {
		return chatMemory;
	}
	
	
	
}
