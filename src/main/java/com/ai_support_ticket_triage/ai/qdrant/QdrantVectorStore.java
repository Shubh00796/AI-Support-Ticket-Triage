package com.ai_support_ticket_triage.ai.qdrant;


import com.ai_support_ticket_triage.ai.chunks.EmbeddedChunk;
import com.ai_support_ticket_triage.ai.vectors.VectorStore;
import io.qdrant.client.PointIdFactory;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.VectorsFactory;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class QdrantVectorStore implements VectorStore {

    private final QdrantClient qdrantClient;

    @Value("${app.qdrant.collection-name}")
    private String collectionName;

    @Override
    public void save(EmbeddedChunk chunk) {

        Points.PointStruct point = Points.PointStruct.newBuilder()
                .setId(PointIdFactory.id(chunk.chunkId()))
                .setVectors(VectorsFactory.vectors(chunk.vector()))
                .putAllPayload(
                        Map.of(
                                "chunkId",
                                JsonWithInt.Value.newBuilder()
                                        .setStringValue(chunk.chunkId().toString())
                                        .build(),

                                "documentId",
                                JsonWithInt.Value.newBuilder()
                                        .setStringValue(chunk.documentId().toString())
                                        .build(),

                                "chunkIndex",
                                JsonWithInt.Value.newBuilder()
                                        .setIntegerValue(chunk.chunkIndex())
                                        .build(),

                                "pageNumber",
                                JsonWithInt.Value.newBuilder()
                                        .setIntegerValue(chunk.pageNumber())
                                        .build()
                        )
                )
                .build();

        try {
            qdrantClient
                    .upsertAsync(
                            collectionName,
                            List.of(point)
                    )
                    .get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread interrupted while saving chunk to Qdrant", e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Failed to save chunk to Qdrant", e);
        }
    }
}