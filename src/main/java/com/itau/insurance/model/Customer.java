package com.itau.insurance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private String document_number;
    private String name;
    private String type;
    private String gender;
    private String date_of_birth;
    private String email;
    private Long phone_number;
    
}