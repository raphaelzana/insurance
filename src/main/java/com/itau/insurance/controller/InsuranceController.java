package com.itau.insurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itau.insurance.model.InsuranceQuote;
import com.itau.insurance.service.InsuranceService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import com.itau.insurance.exception.ApiException;

@RestController
@RequestMapping("/api/v1/insurance")
public class InsuranceController {

    private static final Logger logger = LoggerFactory.getLogger(InsuranceController.class);

    @Autowired
    private InsuranceService insuranceService;

    @PostMapping("/quote")
    public ResponseEntity<InsuranceQuote> publish(@RequestBody @Valid InsuranceQuote body) throws Exception { 

        String requestId = UUID.randomUUID().toString();
        
        logger.info("REQUEST ID: {}, REQUEST: {}", requestId, body.toString());

        try {
            body = insuranceService.quoteInsurace(body);
        } catch (IllegalArgumentException e) {
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("REQUEST ID: {}, Error processing insurance quote request", requestId, e);
            throw new ApiException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Request-ID", requestId);

        return new ResponseEntity<>(body, responseHeaders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/quote/{id}")
    public ResponseEntity<InsuranceQuote> findQuote(@Valid @PathVariable Long id) throws Exception { 
        InsuranceQuote body = new InsuranceQuote();
            
        String requestId = UUID.randomUUID().toString();
            
        logger.info("REQUEST ID: {}, REQUEST: {}", requestId, id);

        try {
            body = insuranceService.findInsuranceQuote(id);
        } catch (IllegalArgumentException e) {
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("REQUEST ID: {}, Error processing insurance quote finding", requestId, e);
            throw new ApiException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Request-ID", requestId);

        return new ResponseEntity<>(body, responseHeaders, HttpStatus.ACCEPTED);
    }
}