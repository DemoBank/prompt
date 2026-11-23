package com.ai.prompt.rag;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.slf4j.Logger;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

public class WebSearchDocumentRetriever implements DocumentRetriever {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(WebSearchDocumentRetriever.class);
    private static final String TAVILY_API_KEY = System.getenv("TAVILY_API_KEY");
    private static final String TAVILY_API_URL = "https://api.tavily.com/search";
    private static final int DEFAULT_RESULT_LIMIT = 5;
    private final int resultLimit;
    private final RestClient restClient;

    public WebSearchDocumentRetriever(RestClient.Builder restClientBuilder, int resultLimit) {
        Assert.notNull(restClientBuilder, "RestClient.Builder cannot be null");
        String apiKey = TAVILY_API_KEY;
        Assert.hasText(apiKey, "Tavily API key must be provided in the environment variable TAVILY_API_KEY");

        this.restClient = restClientBuilder.baseUrl(TAVILY_API_URL)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
        if(resultLimit <= 0 || resultLimit > DEFAULT_RESULT_LIMIT) {
            throw new IllegalArgumentException("Result limit must be between 1 and " + DEFAULT_RESULT_LIMIT);
        }
        this.resultLimit = resultLimit;
    }

    @Override
    public List<Document> retrieve(Query query) {
        logger.info("Processing query", query.text());
        Assert.notNull(query, "query cannot be null");

        String q = query.text();
        Assert.hasText(q,"query.text() cannot be empty");

        TavilyResponsePayload tavilyResponsePayload = restClient.post()
                .body(new TavilyRequestPayload(q,"advanced", resultLimit))
                .retrieve()
                .body(TavilyResponsePayload.class);

        if(tavilyResponsePayload == null || CollectionUtils.isEmpty(tavilyResponsePayload.results())){
            return List.of();
        }

        List<Document> docs = new ArrayList<>(tavilyResponsePayload.results().size());

        for(TavilyResponsePayload.Hit hit: tavilyResponsePayload.results()){
            Document doc = Document.builder()
                    .text(hit.content())
                    .metadata("title",hit.title())
                    .metadata("url", hit.url())
                    .score(hit.score())
                    .build();
            docs.add(doc);
        }
        return docs;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record TavilyRequestPayload(String query, String searchDepth, int maxResults) {}

    record TavilyResponsePayload(List<Hit> results){
        record Hit(String title, String url, String content, Double score){}
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder{
        private RestClient.Builder restClientBuilder;
        private int resultLimit = DEFAULT_RESULT_LIMIT;

        public Builder(){}

        public Builder restClientBuilder(RestClient.Builder restClientBuilder) {
            this.restClientBuilder = restClientBuilder;
            return this;
        }

        public Builder resultLimit(int resultLimit) {
            if(resultLimit <= 0 || resultLimit > DEFAULT_RESULT_LIMIT) {
                throw new IllegalArgumentException("Result limit must be between 1 and " + DEFAULT_RESULT_LIMIT);
            }
            this.resultLimit = resultLimit;
            return this;
        }

        public WebSearchDocumentRetriever build() {
            return new WebSearchDocumentRetriever(restClientBuilder, resultLimit);
        }
    }
}
