package com.ai.prompt.config;

import com.ai.prompt.advisors.TokenUsageAuditAdvisor;
import com.ai.prompt.rag.PIIMaskingDocumentPostProsessor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class ChatMemoryChatClient {

    @Bean
    ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder().maxMessages(10).chatMemoryRepository(chatMemoryRepository).build();
    }

@Bean("chatMemoryClient")
    public ChatClient chatClient(ChatClient.Builder chatClient, ChatMemory chatMemory, RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
    Advisor loggerAdvisor = new SimpleLoggerAdvisor();
    Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor();

    Advisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return chatClient
                .defaultAdvisors(List.of(loggerAdvisor, messageChatMemoryAdvisor, tokenUsageAdvisor, retrievalAugmentationAdvisor ))
                .build();
    }

    @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore, ChatClient.Builder chBuilder ) {
        return RetrievalAugmentationAdvisor.builder()
                .queryTransformers(TranslationQueryTransformer.builder().chatClientBuilder(chBuilder.clone()).targetLanguage("English").build())
                .documentRetriever(
                VectorStoreDocumentRetriever.builder().vectorStore(vectorStore)
                        .topK(3)
                        .similarityThreshold(0.6)
                        .build()

        )
                .documentPostProcessors(PIIMaskingDocumentPostProsessor.builder())
                .build();
    }
}
