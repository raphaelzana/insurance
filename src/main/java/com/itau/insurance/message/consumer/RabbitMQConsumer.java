package com.itau.insurance.message.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.insurance.model.InsuranceQuoteRequest;
import com.itau.insurance.model.Quote;
import com.itau.insurance.repository.QuoteRepository;

@Service
public class RabbitMQConsumer {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private QuoteRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.consumer.queue.name}"})
    public void consume(String message) throws Exception{
        LOGGER.info(String.format("Received message -> %s", message));
        InsuranceQuoteRequest insuranceQuoteRequest = objectMapper.readValue(message, InsuranceQuoteRequest.class);
        LOGGER.info(insuranceQuoteRequest.toString());
        Quote quote = new Quote();
        quote.setId(insuranceQuoteRequest.getId());
        quote.setInsurancePolicyId(insuranceQuoteRequest.getInsurance_policy_id());
        quote.setJsonData(objectMapper.writeValueAsString(insuranceQuoteRequest));

        if (repository.findById(insuranceQuoteRequest.getId()).isEmpty()){
            throw new Exception();
        } else {
            repository.save(quote);
        }
    }
}