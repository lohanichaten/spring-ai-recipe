package com.learning;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.util.Assert;

public class RoutingWorkflow {
	
	private final ChatClient chatClient;

	public RoutingWorkflow(ChatClient chatClient) {
		this.chatClient = chatClient;
	}
	
	public String route(String input,Map<String,String> routes) {
		   Assert.notNull(input, "Input text cannot be null");
	       Assert.notEmpty(routes, "Routes map cannot be null or empty");

	       // Determine the appropriate route for the input
	        String routeKey = determineRoute(input, routes.keySet());

	        // Get the selected prompt from the routes map
	        String selectedPrompt = routes.get(routeKey);
	        if (selectedPrompt == null) {
	            throw new IllegalArgumentException("Selected route '" + routeKey + "' not found in routes map");
	        }

	        // Process the input with the selected prompt
	        return chatClient.prompt(selectedPrompt + "\nInput: " + input).call().content();
	}
	
	
	private String determineRoute(String input,Iterable<String> availableRoutes) {
		 System.out.println("\nAvailable routes: " + availableRoutes);
		 
		  String selectorPrompt = String.format("""
	                Analyze the input and select the most appropriate support team from these options: %s
	                First explain your reasoning, then provide your selection in this JSON format:

	                \\{
	                    "reasoning": "Brief explanation of why this ticket should be routed to a specific team.
	                                Consider key terms, user intent, and urgency level.",
	                    "selection": "The chosen team name"
	                \\}

	                Input: %s""", availableRoutes, input);
		  
		  RoutingResponse routingResponse=chatClient.prompt(selectorPrompt).call().entity(RoutingResponse.class);
		  
		  System.out.println(String.format("Routing Analysis:%s\nSelected route: %s",
	                routingResponse.reasoning(), routingResponse.selection()));
		  
		   return routingResponse.selection();
		  
	}
	

}
