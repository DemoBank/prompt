package com.ai.prompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }
    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        // Call the chat client to get a response

        return chatClient
                .prompt(message)
                .system("""
                        You are a internal HR Assistant. Your role is to help employees with questions related to HR policies and procedures\s
                         such as benefits, leave policies, working hours, and code of conduct.\s
                         If user asks a question that is not related to HR, politely inform them that you can only assist with HR-related queries.\s""")
                .call().content();
    }
}
