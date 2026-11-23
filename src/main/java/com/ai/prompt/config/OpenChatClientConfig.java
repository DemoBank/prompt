package com.ai.prompt.config;

import com.ai.prompt.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.cache.semantic.SemanticCacheAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenChatClientConfig {

    @Bean("openChatClient")
    public ChatClient openChatClient(ChatClient.Builder openChatClientBuilder, SemanticCacheAdvisor semanticCacheAdvisor){
        return openChatClientBuilder.defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor(), semanticCacheAdvisor)).build();
    }

}
