package com.example.PartyForm.Controller;

import com.example.PartyForm.Model.Form;
import com.example.PartyForm.QR.EmailService;
import com.example.PartyForm.Repo.FormRepo;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@RestController
 // Allow CORS from all origins or specify your front-end URL
public class FormController {

    @Autowired
    private FormRepo formDataRepository;
    @Autowired
    private EmailService emailService;
    @PostMapping("/submit")
    public ResponseEntity<String> submitForm(@RequestBody Form formData) {
        // Check if email already exists
        if (formDataRepository.existsByEmail(formData.getEmail())) {
            // If email exists, return 409 Conflict status
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists!");
        }

        // If email is not duplicate, save the form data

        try {
            emailService.sendRegistrationEmail(formData);
            formDataRepository.save(formData);// Send invitation email with QR code
        } catch (MessagingException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
        }
        return ResponseEntity.ok("Form submitted successfully!");
    }
    @GetMapping("/submissions/download")
    public ResponseEntity<byte[]> downloadSubmissionsAsCSV() {
        List<Form> submissions = formDataRepository.findAll();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // Write CSV Header
        writer.println("ID,Name,Email,Year,Gender"); // Adjust according to your Form fields

        // Write data
        for (Form submission : submissions) {
            writer.printf("%s,%s,%s,%s,%s%n",
                    submission.getId(),
                    submission.getName(),
                    submission.getEmail(),
                    submission.getYear(),
                    submission.getGender());
        }

        writer.flush();
        writer.close();

        byte[] data = outputStream.toByteArray();

        // Prepare the response
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=submissions.csv");

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List> getAllData() {
        List<Form> submissions = formDataRepository.findAll();

        return new ResponseEntity<>(submissions,  HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFormById(@PathVariable("id") String id) {
        // Check if the form data exists by id
        Optional<Form> formData = formDataRepository.findById(id);

        if (formData.isPresent()) {
            formDataRepository.deleteById(id); // Delete form data by id
            return new ResponseEntity<>("Form data deleted successfully", HttpStatus.OK);
        } else {
            // If the form data with the specified id doesn't exist, return 404
            return new ResponseEntity<>("Form data not found", HttpStatus.NOT_FOUND);
        }
    }
}
