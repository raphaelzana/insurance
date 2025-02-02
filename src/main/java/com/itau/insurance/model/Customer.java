package com.itau.insurance.model;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Size(min = 11, max = 14)
    @Id
    private String document_number;

    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 255)
    private String type;

    @Size(min = 1, max = 255)
    private String gender;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date_of_birth;

    @Size(min = 1, max = 255)
    private String email;

    @Size(min = 1, max = 255)
    private String phone_number;
    
}