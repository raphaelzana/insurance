package com.itau.insurance.model;

import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceQuote {

    @Nullable
    private Long id;

    @Nullable
    private Long insurance_policy_id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String product_id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String offer_id;

    @NotBlank
    @Size(min = 1, max = 255)
    private String category;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Nullable
    private String created_at;

    @Nullable
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String updated_at;

    @NotNull
    private Double total_monthly_premium_amount;

    @NotNull
    private Double total_coverage_amount;

    private Map<String, Double> coverages;

    private List<String> assistances;

    @NotNull
    private Customer customer;

}