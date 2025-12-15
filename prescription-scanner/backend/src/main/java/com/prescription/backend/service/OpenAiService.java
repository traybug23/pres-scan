package com.prescription.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
@Service
public class OpenAiService {
@Value("${openai.api-key}")
    private String apikey;// Inject API key from application properties
     @Autowired  // Inject the configured RestTemplate
    private final RestTemplate rest=new RestTemplate(); //mailman which we will use for exchange of requests to openai api later
    public String strprescriptiontext (String rawtext)
    { try {
            System.out.println("=== Starting OpenAI request ===");
    String url = "https://api.openai.com/v1/chat/completions"; // OpenAI Chat Completions endpoint
    String systemPrompt = """
            You are a medical prescription parsing expert. Extract information from prescription text and return ONLY valid JSON.
            Format: {"patient_name": string, "medication": string, "dosage": string, "frequency": string}
            If information is missing, use null. Return ONLY the JSON object, no other text.
            """;
            String userPrompt = "Prescription text: " + rawtext.substring(0, Math.min(rawtext.length(), 3000));

            //our request body is a json key
            String requestBody = """
            {
                "model": "gpt-3.5-turbo",
                "messages": [
                    {"role": "system", "content": "%s"}, //system prompt prescription parsing instructions
                    {"role": "user", "content": "%s"} //user prompt with prescription text
                ],
                "temperature": 0.1
            }
            """.formatted(systemPrompt, userPrompt.replace("\"", "\\\""));
            //add headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apikey);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers); // ready to send parcels where we are sending our request body (json string) and headers
            //send request to openai api
            ResponseEntity<String> response = rest.exchange(url, HttpMethod.POST, request, String.class); //rest as mailman, .exchange method to send request to openai api, post method, request object, expecting string response
            String responsebody  = response.getBody(); //extract response body
            if (responsebody != null && responsebody.contains("\"content\":"))
            {
                String content = responsebody.split("\"content\":\"")[1].split("\"")[0]; // extract content field from response by splitting the entire string to [0] and [1] parts
                return content.replace("\\n", "").replace("\\", ""); // clean up newlines and escape characters
            }
            return "{\"error\": \"Failed to parse prescription\"}";
}
catch (Exception e) {
            System.err.println("=== ERROR in OpenAI call ===");
            e.printStackTrace();
            return "{\"error\": \"OpenAI error: " + e.getMessage() + "\"}";
        }
    }
}
