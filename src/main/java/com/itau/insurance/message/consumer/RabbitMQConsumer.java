package com.itau.insurance.message.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itau.insurance.service.InsuranceService;

@Service
public class RabbitMQConsumer {

    @Autowired
    private InsuranceService insuranceService;

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.consumer.queue.name}"})
    public void consume(String message) throws Exception{

        logger.info(String.format("Received message -> %s", message));

        insuranceService.updateInsuranceQuote(message);

    }
}