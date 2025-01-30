package com.itau.insurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.itau.insurance.model.InsuranceQuoteRequest;
import com.itau.insurance.service.InsuranceService;


@RestController
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    @RequestMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void publish(@RequestBody @Validated InsuranceQuoteRequest body) throws Exception { 

        System.out.println("REQUEST: " + body.toString());

        insuranceService.quoteInsurace(body);
        
    }

}