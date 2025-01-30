package com.itau.insurance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

import io.micrometer.common.lang.Nullable;

@Entity
@Table(name = "quote")
@Data
public class Quote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "json_data", columnDefinition = "TEXT")
    private String jsonData;  // Usando String para armazenar o JSON (pode usar JSONObject ou outra abordagem)

    @Column (name = "insurance_policy_id")
    @Nullable
    private Long insurancePolicyId;
}