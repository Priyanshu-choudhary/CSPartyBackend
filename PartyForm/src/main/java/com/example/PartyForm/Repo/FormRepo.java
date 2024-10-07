package com.example.PartyForm.Repo;
import com.example.PartyForm.Model.Form;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FormRepo extends MongoRepository<Form, String> {
}
