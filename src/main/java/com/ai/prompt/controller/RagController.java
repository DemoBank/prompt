package com.ai.prompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.MemoryAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    @Value("classpath:/promptTemplates/SystemPromptRandomDataTemplate.st")
    Resource promptTemplate;

    public RagController(@Qualifier("chatMemoryClient") ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @GetMapping("random/chat")
    public org.springframework.http.ResponseEntity<String> randomChat(@RequestHeader("username") String username, @RequestParam("message") String message) {
       SearchRequest searchRequest=  SearchRequest.builder()
                .query(message)
                .topK(3)
                .similarityThreshold(0.6)
                .build();

       List<Document> similarDocument =  vectorStore.similaritySearch(searchRequest);
       String similarContext = similarDocument.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));
       String answer = chatClient.prompt().system(promptSystemSpec -> promptSystemSpec.text(promptTemplate)
               .param("documents", similarContext))
               .advisors(advisorSpec -> advisorSpec.param(MemoryAdvisor.CONVERSATION_ID, username))
                .user(message)
               .call().content();
       return org.springframework.http.ResponseEntity.ok(answer);
    }
}
