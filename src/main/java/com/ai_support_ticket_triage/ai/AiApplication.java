package com.ai_support_ticket_triage.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Main Spring Boot application entry point for the AI Support Ticket Triage system.
 *
 * <p>This application provides AI-powered ticket classification and semantic
 * document search capabilities. It integrates with Ollama for LLM-based
 * classification and Qdrant for vector storage.</p>
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class AiApplication {

	/**
	 * Application entry point.
	 *
	 * @param args command-line arguments
	 */
	public static void main(final String[] args) {
		SpringApplication.run(AiApplication.class, args);
	}




}
