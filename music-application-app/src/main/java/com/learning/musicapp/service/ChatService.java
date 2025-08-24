package com.learning.musicapp.service;



import java.util.Set;
import java.util.concurrent.Executor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.learning.musicapp.advisor.CountMentionsAdvisor;
import com.learning.musicapp.advisor.DefaultUserContextExtractor;
import com.learning.musicapp.advisor.SavePerformanceAdvisor;
import com.learning.musicapp.advisor.Topic;
import com.learning.musicapp.advisor.TopicGuardAdvisor;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatService {

	private ChatModel chatModel;
	private OllamaChatModel localChatModel;
	private VectorStore vectorStore;
	private Neo4jTemplate neo4jTemplate;
	private Executor executor;
	private ApplicationEventPublisher applicationEventPublisher;

	public ChatService(ChatModel chatModel, OllamaChatModel localChatModel, VectorStore vectorStore,
			Neo4jTemplate neo4jTemplate, Executor executor, ApplicationEventPublisher applicationEventPublisher) {
		super();
		this.chatModel = chatModel;
		this.localChatModel = localChatModel;
		this.vectorStore = vectorStore;
		this.neo4jTemplate = neo4jTemplate;
		this.executor = executor;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	ChatClient chatClientForSession(ConversationSession conversationSession) {
		return ChatClient.builder(chatModel).defaultAdvisors(
				// Out of the box advisor, adds conversation memory
				MessageChatMemoryAdvisor.builder(conversationSession.getChatMemory()).build(),
				new CountMentionsAdvisor(neo4jTemplate, applicationEventPublisher

				), new SavePerformanceAdvisor(chatModel, neo4jTemplate, new DefaultUserContextExtractor(), executor),
				new TopicGuardAdvisor(localChatModel, Set.of(Topic.POLITICS, Topic.RELIGION, Topic.SPORT),
						new DefaultUserContextExtractor()),
				// Out of the box advisor, handles RAG
				QuestionAnswerAdvisor.builder(vectorStore)
						.searchRequest(SearchRequest.builder().similarityThreshold(.2).topK(6).build()).build())
				.defaultSystem(conversationSession.promptResource()).build();
	}

	public ChatResponse respondToUserMessage(ConversationSession conversationSession, String userMessage) {
		ChatResponse chatResponse = chatClientForSession(conversationSession)
		            .prompt()
		            .advisors(it->it.param(ChatMemory.CONVERSATION_ID, conversationSession.conversationId) )
		            .user(userMessage)
		            .call()
		            .chatResponse();
		        log.info("ChatResponse: $chatResponse");
		        return chatResponse;
	}

}
