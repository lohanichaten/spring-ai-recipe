package com.learning.musicapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learning.musicapp.dto.DocumentUpload;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UploadController {

	private final VectorStore vectoreStore;
	private final ResourceLoader resourceLoader;

	@PutMapping("/documents")
	List<String> addDocument(@RequestBody DocumentUpload documentUpload) {
		log.info("Adding document from ${}", documentUpload.getUrl());
		Resource resource = resourceLoader.getResource(documentUpload.getUrl());
		var content = new TikaDocumentReader(documentUpload.getUrl()).get();
		var documents = new TokenTextSplitter().split(content);
		vectoreStore.add(documents);
		return documents.stream().map(d -> d.getText()).collect(Collectors.toList());

	}
}
