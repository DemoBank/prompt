package com.ai.prompt.controller;

import com.openai.models.ChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptStuffingController {

    @Autowired
    private ChatClient chatClient;

    @Value("classpath:PromptTemplates/SystemPromptTemplate.st")
    Resource systemPromptTemplate;

    @GetMapping("/prompt-stuffing")
    public String promptStuffing(@RequestParam("message") String message) {
        return chatClient
                .prompt()
                .options(OpenAiChatOptions.builder().model(ChatModel.GPT_4_1_NANO_2025_04_14.toString()).maxTokens(200).temperature(0.7))
                .system(systemPromptTemplate)
                .user(message)
                .call().content();
    }
}
