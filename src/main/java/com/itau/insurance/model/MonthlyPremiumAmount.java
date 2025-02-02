package com.itau.insurance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyPremiumAmount {

    private double max_amount;
    private double min_amount;
    private double suggested_amount;

}
