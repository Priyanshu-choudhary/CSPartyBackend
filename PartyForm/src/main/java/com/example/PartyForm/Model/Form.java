package com.example.PartyForm.Model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "form_data")
@Data
public class Form {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    @Indexed(unique = true)
    private String email;
    private String year;
    private String gender;

    // Constructors, Getters and Setters (if not using Lombok)
}
