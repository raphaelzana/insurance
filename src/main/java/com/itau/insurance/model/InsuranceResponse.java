package com.itau.insurance.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceResponse {
    private String id;
    private String name;
    private String created_at;
    private boolean active;
    private List<String> offers;

}