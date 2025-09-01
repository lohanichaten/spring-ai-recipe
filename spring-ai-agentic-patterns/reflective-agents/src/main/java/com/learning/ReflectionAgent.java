package com.learning;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class ReflectionAgent {
	
	
	   private final ChatClient generateChatClient;

	    private final ChatClient critiqueChatClient;
	    
	    public ReflectionAgent(ChatModel chatModel) {
	        this.generateChatClient = ChatClient.builder(chatModel)
	                .defaultSystem("""
	                        You are a Java programmer tasked with generating high quality Java code.
	                        Your task is to Generate the best content possible for the user's request. If the user provides critique,
	                        respond with a revised version of your previous attempt.
	                        """)
	                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
	                .build();

	        this.critiqueChatClient = ChatClient.builder(chatModel)
	                .defaultSystem("""
	                        You are tasked with generating critique and recommendations to the user's generated content.
	                        If the user content has something wrong or something to be improved, output a list of recommendations
	                        and critiques. If the user content is ok and there's nothing to change, output this: <OK>
	                        """)
	                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
	                .build();
	    }
	    
	    public String run(String userQuestion, int maxIterations) {

	        String generation = generateChatClient.prompt(userQuestion).call().content();
	        System.out.println("##generation\n\n" + generation);
	        String critique;
	        for (int i = 0; i < maxIterations; i++) {

	            critique = critiqueChatClient.prompt(generation).call().content();

	            System.out.println("##Critique\n\n" + critique);
	            if (critique.contains("<OK>")) {
	                System.out.println("\n\nStop sequence found\n\n");
	                break;
	            }
	            generation = generateChatClient.prompt(critique).call().content();
	        }
	        return generation;

	    }


}
