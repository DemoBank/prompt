package com.ai.prompt.controller;

import com.ai.prompt.config.ChatMemoryChatClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.MemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ChatClient webSeachChatClient;
    @Value("classpath:/promptTemplates/SystemPromptRandomDataTemplate.st")
    Resource promptTemplate;

    @Value("classpath:/promptTemplates/SystemPromptTemplate.st")
    Resource hrPromptTemplate;


    public RagController(@Qualifier("chatMemoryClient") ChatClient chatClient, VectorStore vectorStore, @Qualifier("webSearchChatClient") ChatClient webSeachChatClient) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.webSeachChatClient = webSeachChatClient;
    }

    @GetMapping("random/chat")
    public org.springframework.http.ResponseEntity<String> randomChat(@RequestHeader("username") String username, @RequestParam("message") String message) {
//       SearchRequest searchRequest=  SearchRequest.builder()
//                .query(message)
//                .topK(3)
//                .similarityThreshold(0.6)
//                .build();
//
//       List<Document> similarDocument =  vectorStore.similaritySearch(searchRequest);
//       String similarContext = similarDocument.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));
       String answer = chatClient.prompt()
//               .system(promptSystemSpec -> promptSystemSpec.text(promptTemplate)
//               .param("documents", similarContext))
               .advisors(advisorSpec -> advisorSpec.param(MemoryAdvisor.CONVERSATION_ID, username))
                .user(message)
               .call().content();
       return org.springframework.http.ResponseEntity.ok(answer);
    }

    @GetMapping("document/chat")
    public org.springframework.http.ResponseEntity<String> documentChat(@RequestHeader("username") String username, @RequestParam("message") String message) {
//        SearchRequest searchRequest=  SearchRequest.builder()
//                .query(message)
//                .topK(3)
//                .similarityThreshold(0.6)
//                .build();
//
//        List<Document> similarDocument =  vectorStore.similaritySearch(searchRequest);
//        String similarContext = similarDocument.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));
        String answer = chatClient.prompt()
//                .system(promptSystemSpec -> promptSystemSpec.text(hrPromptTemplate)
//                        .param("documents", similarContext))
                .advisors(advisorSpec -> advisorSpec.param(MemoryAdvisor.CONVERSATION_ID, username))
                .user(message)
                .call().content();
        return org.springframework.http.ResponseEntity.ok(answer);
    }

    @GetMapping("/web/search/chat")
    public ResponseEntity<String> webSearchChat(@RequestHeader("userName") String userName, @RequestParam("message") String message ){
        String answers = webSeachChatClient.prompt().advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userName))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answers);
    }
}
