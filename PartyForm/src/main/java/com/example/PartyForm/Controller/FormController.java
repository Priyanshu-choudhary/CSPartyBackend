package com.example.PartyForm.Controller;

import com.example.PartyForm.Model.Form;
import com.example.PartyForm.Repo.FormRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

@RestController
 // Allow CORS from all origins or specify your front-end URL
public class FormController {

    @Autowired
    private FormRepo formDataRepository;

    @PostMapping("/submit")
    public ResponseEntity<String> submitForm(@RequestBody Form formData) {
        formDataRepository.save(formData);
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
}
