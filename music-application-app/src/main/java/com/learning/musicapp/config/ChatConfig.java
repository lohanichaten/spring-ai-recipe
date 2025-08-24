package com.learning.musicapp.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.ai.bedrock.converse.BedrockProxyChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatConfig {

	@Bean
	ChatMemoryRepository inmemoryChatMemoryRepository() {
		return new InMemoryChatMemoryRepository();
	}
	
	
	

	@Bean
	@Primary
	ChatClient chatClient(BedrockProxyChatModel chatModel) {
		return ChatClient.create(chatModel);
	}
	


	
	  @Bean
	  OllamaChatModel   localChatModel(){
	        return OllamaChatModel.builder()
	        		.ollamaApi( OllamaApi.builder().build())
	        		.defaultOptions(OllamaOptions.builder()
	        									.model("gemma2:2b")
	        									.build())
	            .build();
	    }

	  
	  @Bean(name = "taskExecutor")
	    public Executor taskExecutor() {
	        return Executors.newFixedThreadPool(5);
	    }

	
}
