A Spring Boot backend service that extracts structured medical prescription information from uploaded images using OCR and LLM processing.
This project demonstrates how AI can help convert handwritten or printed prescriptions into machine-readable structured data, improving digital health workflows and reducing manual transcription effort.

**Features**
-> Upload prescription image via REST API
-> Extract text from prescription using OCR
-> Convert unstructured prescription text into structured medical data
-> RESTful backend architecture
-> Modular service design
-> Performance logging for API response time
-> Health check endpoint

**Tech Stack**

_Backend:_
Java
Spring Boot
REST API

_AI Services:_
OCR text extraction service
LLM text structuring

_Tools:_
Maven
Postman (API testing)

**Architecture Overview**
Client (future frontend)
        ↓
Spring Boot REST API
        ↓
Google Vision API → extracts text from image
        ↓
OpenAI API → converts raw text into structured prescription format
        ↓
JSON response

**PROJECT STRUCTURE**
src/main/java/com/prescription/backend

controller
 └── PrescriptionController.java

service
 ├── VisionService.java      
 └── OpenAiService.java     

**API Endpoints**
1. Upload Prescription Image
POST /upload

Uploads an image file and returns structured prescription data.

**Request**
multipart/form-data
key: image
value: prescription image file

**Example Response**
"patient_name": "John Doe",
"medications": 
[
{
  "name":  "Paracetamol",
  "dosage":  "500mg",
  "frequency":  "twice daily"
}
],
"notes":  "After meals"
}

2. Health Check

GET /health

Returns service status.

Response
Welcome to DocWriting Scan!

**Workflow**
Step 1 – OCR Processing

Image is sent to OCR service which extracts raw prescription text.

Example:

Tab Paracetamol 500mg BD
Tab Azithromycin 250mg OD
Step 2 – AI Structuring

Extracted text is processed using an LLM to convert into structured JSON.

Example structured output:

{
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "frequency": "BD"
    }
  ]
}
**Running the Project**

1. Clone repository
git clone https://github.com/traybug23/pres-scan
cd prescription-backend

3. Configure API keys
Add credentials for:
OCR service
LLM service

Example:
application.properties

3. Run application
mvn spring-boot:run
Server runs on:
http://localhost:8080

**Current Status**
✔ Backend API implemented
✔ OCR pipeline integrated
✔ AI structuring implemented
✔ Performance logging added
! Frontend interface not yet implemented

**Future Improvements**
-> Frontend interface for uploading prescription images
-> Support for multiple image formats
-> Improved medical entity recognition
-> Patient history storage
