package com.itau.insurance.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "quote")
@Data
@Builder
public class Quote implements Serializable {

    public Quote() {
    }

    public Quote(Long id, String json_data, Long insurance_policy_id, String created_at, String updated_at) {
        this.id = id;
        this.json_data = json_data;
        this.insurance_policy_id = insurance_policy_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "json_data", columnDefinition = "TEXT")
    private String json_data;  // Usando String para armazenar o JSON (pode usar JSONObject ou outra abordagem)

    @Column(name = "insurance_policy_id")
    @Nullable
    private Long insurance_policy_id;

    @Column(name = "created_at")
    @Nullable
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String created_at;

    @Column(name = "updated_at")
    @Nullable
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String updated_at;
}