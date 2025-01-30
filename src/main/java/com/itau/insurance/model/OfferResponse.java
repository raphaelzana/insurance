package com.itau.insurance.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferResponse {

    private String id;
    private String product_id;
    private String name;
    private String created_at;
    private boolean active;
    private Map<String, Double> coverages;
    private List<String> assistances;
    private MonthlyPremiumAmount monthly_premium_amount;
}
