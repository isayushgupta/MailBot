package com.emailassistant.mailbot.controller;


import com.emailassistant.mailbot.dto.EmailRequestDTO;
import com.emailassistant.mailbot.service.EmailGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("mailbot/api/response")
public class requestHandler {

    private final EmailGeneratorService emailGeneratorService;

    @SuppressWarnings("NullableProblems")
    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequestDTO emailRequestDTO)
    {
        System.out.println("Request received on backend ...");
        String reply = emailGeneratorService.generateEmailReply(emailRequestDTO);
        System.out.println("Reply getting generated");
        return ResponseEntity.ok(reply);
    }
}
