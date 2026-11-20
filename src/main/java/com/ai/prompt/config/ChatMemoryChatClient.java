package com.ai.prompt.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class ChatMemoryChatClient {

@Bean("chatMemoryClient")
    public ChatClient chatClient(ChatClient.Builder chatClient, ChatMemory chatMemory) {
    Advisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
    Advisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        return chatClient
                .defaultAdvisors(List.of(messageChatMemoryAdvisor, simpleLoggerAdvisor))
                .build();
    }
}
