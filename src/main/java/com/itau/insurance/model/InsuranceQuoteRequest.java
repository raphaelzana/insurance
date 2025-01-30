package com.itau.insurance.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceQuoteRequest {

    private Long id;
    private Long insurance_policy_id;
    private String product_id;
    private String offer_id;
    private String category;
    private String created_at;
    private String updated_at;
    private Double total_monthly_premium_amount;
    private Double total_coverage_amount;
    private Map<String, Double> coverages;
    private List<String> assistances;
    private Customer customer;

}