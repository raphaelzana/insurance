package com.itau.insurance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itau.insurance.model.Quote;

@Repository
@Transactional
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    // Aqui você pode definir métodos personalizados, se necessário
}
