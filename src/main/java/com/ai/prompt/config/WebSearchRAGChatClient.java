package com.ai.prompt.config;

import com.ai.prompt.advisors.TokenUsageAuditAdvisor;
import com.ai.prompt.rag.WebSearchDocumentRetriever;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration
public class WebSearchRAGChatClient {

    @Bean("webSearchChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClient, ChatMemory chatMemory, RestClient.Builder restClientBuilder) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();
        Advisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        var webSeacrchRAGAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(WebSearchDocumentRetriever.builder().restClientBuilder(restClientBuilder).resultLimit(5).build())
                .build();
        return chatClient
                .defaultAdvisors(List.of(loggerAdvisor, messageChatMemoryAdvisor, tokenUsageAdvisor, webSeacrchRAGAdvisor ))
                .build();
    }

}
