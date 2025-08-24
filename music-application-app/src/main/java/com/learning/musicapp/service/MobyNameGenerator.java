package com.learning.musicapp.service;

import org.springframework.stereotype.Service;

import info.schnatterer.mobynamesgenerator.MobyNamesGenerator;


@Service
public class MobyNameGenerator implements NameGenerator{

	@Override
	public String generateName() {
		return MobyNamesGenerator.getRandomName();
	}
	
	

}
