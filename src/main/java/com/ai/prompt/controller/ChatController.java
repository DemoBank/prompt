package com.ai.prompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ChatClient chatClient;

//    public ChatController(ChatClient chatClient) {
//      this.chatClient = chatClient;
//    }
//
    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        // Call the chat client to get a response

        return chatClient
                .prompt(message)
                .advisors(new com.ai.prompt.advisors.TokenUsageAuditAdvisor())
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
