package com.learning.musicapp.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;

public interface UserContentExtractor {

	public String extract(ChatClientRequest request);
}
