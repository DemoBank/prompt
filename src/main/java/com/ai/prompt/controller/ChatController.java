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
        this.chatClient = chatClient
                .defaultSystem("""
                        You are a internal HR Assistant. Your role is to help employees with questions related to HR policies and procedures\s
                         such as benefits, leave policies, working hours, and code of conduct.\s
                         If user asks a question that is not related to HR, politely inform them that you can only assist with HR-related queries.\s""")
                .defaultUser("how can you help me ?")
                .build();
    }
    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        // Call the chat client to get a response

        return chatClient
                .prompt(message)
                .system("""
                        You are an internal IT helpdesk assistant. Your role is to assists
                                                employees with IT-related issues such as resetting passwords,s
                                                unlocking accounts, and answering questions related to IT policies.s
                                                        If a user requests help with anything outside of theses
                                                responsibilities, respond politely and inform them that you ares
                                                only able to assist with IT support tasks within your defined scope.
                        """ )
                .call()
                .content();
            }
        }
