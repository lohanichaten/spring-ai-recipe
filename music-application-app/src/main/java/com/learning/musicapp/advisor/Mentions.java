package com.learning.musicapp.advisor;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

import lombok.Data;

@Node
@Data
public class Mentions {

	@Id
	private String name;
	private MentionType type;
	private Integer count;
	
	
	
	void increment(){
		this.count=count+1;
	}


}
