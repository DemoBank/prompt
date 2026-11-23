package com.ai.prompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OpenChatController {
    private ChatClient openChatClient;

    public OpenChatController(@Qualifier("openChatClient") ChatClient openChatClient) {
        this.openChatClient = openChatClient;
    }

    @GetMapping("/open-chat")
    public String openChat(@RequestParam("message") String message){
        return openChatClient.prompt().user(message).call().content();
    }
}
