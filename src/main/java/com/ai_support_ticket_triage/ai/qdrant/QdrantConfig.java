package com.ai_support_ticket_triage.ai.qdrant;


import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QdrantConfig {

    @Bean
    public QdrantClient qdrantClient(
            @Value("${app.qdrant.host}") String host,
            @Value("${app.qdrant.api-key}") String apiKey
    ) {

        QdrantGrpcClient grpcClient =
                QdrantGrpcClient.newBuilder(
                                host,
                                6334,
                                true
                        )
                        .withApiKey(apiKey)
                        .build();

        return new QdrantClient(grpcClient);
    }
}
