package com.learning.musicapp.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserContextExtractor implements UserContentExtractor {

	
	
	@Override
	public String extract(ChatClientRequest request) {
		return takeBefore(request.prompt().getUserMessage().getText(), "\n\n");
	}
	
	public  String takeBefore(String input, String what) {
        int index = input.indexOf(what);
        return index != -1 ? input.substring(0, index) : input;
    }


}
