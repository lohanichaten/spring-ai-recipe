package com.learning.musicapp.advisor;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
public class Performance {

	@Id 
	@GeneratedValue(generatorClass = UUIDStringGenerator.class)
	private String id;
	private String work;
	private String composer;
	private Date date;
	
}
