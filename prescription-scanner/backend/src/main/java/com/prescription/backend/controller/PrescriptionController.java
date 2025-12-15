package com.prescription.backend.controller;

import com.prescription.backend.service.VisionService;
import com.prescription.backend.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class PrescriptionController 
{
    @Autowired
    private VisionService visionservice;
    
    @Autowired
    private OpenAiService openaiservice;
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadfile(@RequestParam("image") MultipartFile file)
    {
        long startTime = System.currentTimeMillis();
        System.out.println("========================================");
        System.out.println("Request received at: " + startTime);
        System.out.println("File name: " + file.getOriginalFilename());
        System.out.println("File size: " + file.getSize() + " bytes");
        
        try {
            // Step 1: Extract text from image using Google Vision
            long visionStart = System.currentTimeMillis();
            String extractedtext = visionservice.extractTextFromImage(file.getBytes());
            long visionTime = System.currentTimeMillis() - visionStart;
            System.out.println("✓ Vision API took: " + visionTime + "ms");
            System.out.println("Extracted text length: " + extractedtext.length() + " characters");
            System.out.println("Extracted text preview: " + extractedtext.substring(0, Math.min(100, extractedtext.length())));
            
            // Step 2: Process extracted text with OpenAI
            long openaiStart = System.currentTimeMillis();
            String structureddata = openaiservice.strprescriptiontext(extractedtext);
            long openaiTime = System.currentTimeMillis() - openaiStart;
            System.out.println("✓ OpenAI API took: " + openaiTime + "ms");
            
            // Total time
            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println("========================================");
            System.out.println("✓ TOTAL REQUEST TIME: " + totalTime + "ms");
            System.out.println("Breakdown: Vision=" + visionTime + "ms, OpenAI=" + openaiTime + "ms");
            System.out.println("========================================");
            
            return ResponseEntity.ok(structureddata);
            
        } catch (Exception e) {
            long errorTime = System.currentTimeMillis() - startTime;
            System.err.println("========================================");
            System.err.println("✗ ERROR after " + errorTime + "ms");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("========================================");
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
        }
    }
    
    @GetMapping("/health")
    public String health() {
        return "Welcome to DocWriting Scan!";
    }
}