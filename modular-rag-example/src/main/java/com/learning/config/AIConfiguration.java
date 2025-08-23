package com.learning.config;

import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientCustomizer;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AIConfiguration {

	
	@Bean
	ChatClient chatClient(ChatModel chatModel) {
		return ChatClient.create(chatModel);
	}


    @Bean
    ChatClientCustomizer chatClientCustomizer(ChatMemory chatMemory) {
        return builder -> builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory)
                        .conversationId(UUID.randomUUID().toString())
                        .build());
    }

	
}
