package com.itau.insurance.model;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Size(min = 1, max = 255)
    @Id
    private String id;

    @Size(min = 1, max = 255)
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String created_at;

    private boolean active;
    
    private List<String> offers;

}