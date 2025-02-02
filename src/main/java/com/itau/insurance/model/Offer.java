package com.itau.insurance.model;

import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer {

    @NotBlank
    private String id;

    @NotBlank
    private String product_id;

    @Size(min = 1, max = 255)
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String created_at;

    private boolean active;

    private Map<String, Double> coverages;

    private List<String> assistances;

    private MonthlyPremiumAmount monthly_premium_amount;
}
