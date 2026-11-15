package com.ai.prompt.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClient) {
        return chatClient
                .defaultSystem("""
                        You are a internal HR Assistant. Your role is to help employees with questions related to HR policies and procedures\s
                         such as benefits, leave policies, working hours, and code of conduct.\s
                         If user asks a question that is not related to HR, politely inform them that you can only assist with HR-related queries.\s""")
                .defaultUser("how can you help me ?")
                .build();
    }
}
