package com.learning.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/chat")
public class ChatController {

	@Autowired
	private  ChatClient chatClient;
	


	@RequestMapping("/")
	public String chat(@RequestBody ChatRequest request) {
		return chatClient.prompt(request.text()).call().content();
	}
}


record ChatRequest(String text) {}