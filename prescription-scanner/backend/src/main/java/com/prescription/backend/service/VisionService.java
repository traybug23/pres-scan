package com.prescription.backend.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VisionService {

    public String extractTextFromImage(byte[] imageBytes) {
        try {
            System.out.println("Creating Vision API client...");
            
            ImageAnnotatorClient client = ImageAnnotatorClient.create();

            ByteString imgBytes = ByteString.copyFrom(imageBytes);
            Image img = Image.newBuilder().setContent(imgBytes).build();

            Feature feat = Feature.newBuilder()
                .setType(Feature.Type.DOCUMENT_TEXT_DETECTION)
                .build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

            System.out.println("Sending request to Vision API...");
            
            BatchAnnotateImagesResponse response = 
                client.batchAnnotateImages(List.of(request));

            AnnotateImageResponse res = response.getResponsesList().get(0);
            
            if (res.hasError()) {
                System.err.println("Vision API Error: " + res.getError().getMessage());
                throw new RuntimeException("Vision API Error: " + res.getError().getMessage());
            }

            String extractedText = res.getFullTextAnnotation().getText();
            System.out.println("Successfully extracted text from image");
            System.out.println("Extracted text length: " + extractedText.length() + " characters");
            
            client.close();
            return extractedText;
            
        } catch (Exception e) {
            System.err.println("Google Vision error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Google Vision error: " + e.getMessage(), e);
        }
    }
}
