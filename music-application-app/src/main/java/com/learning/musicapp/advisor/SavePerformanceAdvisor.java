package com.learning.musicapp.advisor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

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
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

import lombok.Data;
import lombok.val;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SavePerformanceAdvisor implements CallAdvisor{

	
	private final ChatClient chatCLient;
	private final Neo4jTemplate neo4jTemplate;
	private final UserContentExtractor userContentExtractor;
	private final Executor executor;
	private final RetryTemplate retryTemplate;
	
	public SavePerformanceAdvisor(ChatModel chatModel, Neo4jTemplate neo4jTemplate,
			UserContentExtractor userContentExtractor,Executor executor) {	
		this.chatCLient = ChatClient.builder(chatModel).build();
		this.neo4jTemplate = neo4jTemplate;
		this.userContentExtractor = userContentExtractor;
		this.executor=executor;
		this.retryTemplate= new RetryTemplateBuilder().maxAttempts(3).fixedBackoff(1000).build();
	}

	
	
	@Override
	public String getName() {		
		return SavePerformanceAdvisor.class.getSimpleName();
	}
	

	@Override
	public int getOrder() {
	
		return 0;
	}

	@Override
	public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        var performanceSaved = new  CompletableFuture<Boolean>();
        Runnable backgroundTask = ()-> {
            try {
                // Allow for flaky model
            	Boolean result = this.retryTemplate.execute(context -> {
            	    return performanceWasSaved(userContentExtractor.extract(chatClientRequest));
            	});

                 performanceSaved.complete(result);
            } catch ( Exception t ) {
                log.error("We tried really hard but the model kept failing. Don't fail the advisor chain", t);
                performanceSaved.complete(false);
            };
        };

        
        executor.execute(backgroundTask);
        
        // We're blocking here to change the response,
        // but if we didn't want to do that,
        // we could simply return and let the background task complete without blocking the main interaction
        
        try {
			if (performanceSaved.get()) {
				ChatResponse chatResponse=   ChatResponse.builder().generations( List.of(new Generation(new AssistantMessage("Thank you! I've made a note of that performance.")))).build();
				 return  ChatClientResponse.builder().context(chatClientRequest.context())
				  					.chatResponse(chatResponse)
				  					.build();
				
			}
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return callAdvisorChain.nextCall(chatClientRequest);
	}
	
	private Boolean performanceWasSaved(String userContent) {
		  val performanceResponse = chatCLient
		            .prompt()
		            .user(u->{u.text(new ClassPathResource("prompts/save_performance.md"))
		            		 .param("content", userContent) 
		            		 .param("now",new Date());
		            })
		            .call()
		            .entity(PerformanceResponse.class);
		  
		  if(performanceResponse!=null) {
			  	log.info("Adding performance: {}", performanceResponse);
	            neo4jTemplate.save(performanceResponse);
	            return true;
		  }
		  
		  return false;
		  
		  
	}
	
	@Data
	class PerformanceResponse{
		private String work;
		private Date date;
		private String composer;
		
		
	}

}
