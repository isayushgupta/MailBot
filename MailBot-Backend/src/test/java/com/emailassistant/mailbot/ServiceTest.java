package com.emailassistant.mailbot;

import com.emailassistant.mailbot.dto.EmailRequestDTO;
import com.emailassistant.mailbot.service.EmailGeneratorService;
// import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;



@SpringBootTest
public class ServiceTest {

    @Autowired
        EmailGeneratorService emailGeneratorService;

    @Test
    public void serviceTest() {

        EmailRequestDTO emailRequestDTO = new EmailRequestDTO(
                "Hey Geek, You have successfully registered...",
                "Friendly"
        );

        String testResult = emailGeneratorService.generateEmailReply(emailRequestDTO);

         assertNotNull(testResult);
    }
}
