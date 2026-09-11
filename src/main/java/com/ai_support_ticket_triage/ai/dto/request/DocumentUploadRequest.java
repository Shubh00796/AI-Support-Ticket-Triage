package com.ai_support_ticket_triage.ai.dto.request;


import org.springframework.web.multipart.MultipartFile;

public record DocumentUploadRequest(
        MultipartFile file
) {
}