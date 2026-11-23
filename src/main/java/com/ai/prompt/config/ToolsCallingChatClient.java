package com.ai.prompt.config;

import com.ai.prompt.advisors.TokenUsageAuditAdvisor;
import com.ai.prompt.tools.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ToolsCallingChatClient {

    @Bean("ToolCallingChatClient")
    public ChatClient ToolsCallingChatClient(ChatClient.Builder builder, ChatMemory chatMemory, TimeTools timeTools){
        return builder
                .defaultTools(timeTools)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(),new TokenUsageAuditAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory)
                        .build())).build();
    }

}
