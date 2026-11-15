package com.ai.prompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptChatTemplateController {

    @Autowired
    private ChatClient chatClient;

    String promptTemplate = """
            A customer name {customerName} has sent the following message: "{customerMessage}".
            Write a polite and helpful email response to address the customer's concerns and provide assistance. 
            Ensure that the response is professional, empathetic, and offers a solution or next steps.
            Respond as if you are writing the email body only. don't include subject and signature. 
            Keep the response concise and clear, and avoid using any technical jargon.
            """;

    @GetMapping("/email")
    public String emailResponse(@RequestParam("customerName") String customerName, @RequestParam("customerMessage") String customerMessage) {
        return chatClient
                .prompt()
                .system("""
                        You are a professional customer assistant which helps drafting email\s
                        responses to improve the productivity of the customer support team. \s""")
                .user(promptUserSpec -> promptUserSpec
                        .text(promptTemplate)
                        .param("customerName", customerName)
                        .param("customerMessage", customerMessage))
                .call().content();
    }

}
