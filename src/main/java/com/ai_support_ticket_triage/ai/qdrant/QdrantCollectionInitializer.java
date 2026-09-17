package com.ai_support_ticket_triage.ai.qdrant;


import com.google.common.util.concurrent.ListenableFuture;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class QdrantCollectionInitializer {

    private final QdrantClient qdrantClient;

    @Value("${app.qdrant.collection-name}")
    private String collectionName;

    @Value("${app.embedding.dimension}")
    private int embeddingDimension;

    @PostConstruct
    public void initialize() {

        boolean exists = await(qdrantClient.collectionExistsAsync(collectionName));

        if (exists) {
            log.info("Qdrant collection '{}' already exists", collectionName);
            return;
        }

        await(
                qdrantClient.createCollectionAsync(
                        collectionName,
                        Collections.VectorParams.newBuilder()
                                .setSize(embeddingDimension)
                                .setDistance(Collections.Distance.Cosine)
                                .build()
                )
        );

        log.info(
                "Created Qdrant collection '{}' with dimension {} and Cosine distance",
                collectionName,
                embeddingDimension
        );
    }

    private <T> T await(ListenableFuture<T> future) {
        try {
            return future.get();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while initializing Qdrant collection", ex);
        } catch (ExecutionException ex) {
            throw new IllegalStateException("Failed to initialize Qdrant collection", ex);
        }
    }
}
