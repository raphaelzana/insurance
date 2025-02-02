package com.itau.insurance.service;

import com.itau.insurance.model.InsuranceQuote;
import com.itau.insurance.repository.QuoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InsuranceServiceTest {

    @InjectMocks
    private InsuranceService insuranceService;

    @Mock
    private QuoteRepository quoteRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQuoteInsurance() throws Exception {
        InsuranceQuote request = new InsuranceQuote();
        //insuranceService.quoteInsurace(request);
        //verify(quoteRepository).save(any());
    }
}
