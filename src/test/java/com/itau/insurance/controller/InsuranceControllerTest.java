package com.itau.insurance.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.insurance.model.InsuranceQuote;
import com.itau.insurance.service.InsuranceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InsuranceController.class)
public class InsuranceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private InsuranceService insuranceService;

    @InjectMocks
    private InsuranceController insuranceController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testPublish_Success() throws Exception {
        InsuranceQuote request = new InsuranceQuote();
        // ...populate request with valid data...

        mockMvc.perform(post("/insurance/quote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(insuranceService).quoteInsurace(request);
    }

    @Test
    public void testPublish_InternalServerError() throws Exception {
        InsuranceQuote request = new InsuranceQuote();
        // ...populate request with valid data...

        doThrow(new RuntimeException("Test Exception")).when(insuranceService).quoteInsurace(request);

        mockMvc.perform(post("/insurance/quote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testPublish_BadRequest() throws Exception {
        InsuranceQuote request = new InsuranceQuote();
        // ...populate request with invalid data...

        doThrow(new IllegalArgumentException("Invalid data")).when(insuranceService).quoteInsurace(request);

        mockMvc.perform(post("/insurance/quote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists("Request-ID"));
    }
}
