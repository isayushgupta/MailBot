package com.emailassistant.mailbot.service;

import com.emailassistant.mailbot.dto.EmailRequestDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailGeneratorService
{
    @Value("${gemini.api.key}")
    String geminiKey;

    @Value("${gemini.api.url}")
    String geminiUrl;

    private final WebClient webClient;

    public String generateEmailReply(EmailRequestDTO emailRequestDTO)
    {
        // Build Prompt
        String prompt = buildPrompt(emailRequestDTO);

        // Craft A Request
        TextDTO textDTO = new TextDTO(prompt);
        PartDTO partDTO = new PartDTO(List.of(textDTO));
        GeminiRequestDTO geminiRequestDTO = new GeminiRequestDTO(List.of(partDTO));

        // Do request and get email response
        String reply =  (webClient.post().
                uri(geminiUrl+geminiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(geminiRequestDTO)
                .retrieve()
                .bodyToMono(String.class)
                .block());

        // extract the result from the response return the result

        return extractResponseEmail(reply);
    }

    private String extractResponseEmail(String jsonResponse)
    {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonResponse);

        try {
        return root.path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText();
        }

        catch (Exception e)
        {
            return "Cannot process request Error: "+e.getMessage();
        }

    }

    private String buildPrompt(EmailRequestDTO emailRequestDTO)
    {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content. Please don't generate a subject line. Generate only the reply. ");
        if(emailRequestDTO.tone() != null && !emailRequestDTO.tone().isEmpty())
        {
            prompt.append("\nPlease use a ").append(emailRequestDTO.tone()).append(" tone");
        }

        prompt.append("\n Original Email: \n").append(emailRequestDTO.emailContent());

        return prompt.toString();
    }

    record TextDTO(String text){}
    record PartDTO(List<TextDTO> parts){}
    record GeminiRequestDTO(List<PartDTO> contents){}
}
