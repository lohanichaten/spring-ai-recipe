package com.learning.service;

import org.springframework.lang.Nullable;

import reactor.core.publisher.Flux;

public interface AiService {

    @Nullable
    String chat(String input);

    default Flux<String> stream(String input) {
        return Flux.empty();
    }

}

