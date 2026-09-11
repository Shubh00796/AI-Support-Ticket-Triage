package com.ai_support_ticket_triage.ai.services;


import com.ai_support_ticket_triage.ai.dto.responce.DocumentUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    DocumentUploadResponse upload(MultipartFile file);
}