package com.itau.insurance.message.producer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class RabbitMQProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMQProducer rabbitMQProducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(rabbitMQProducer, "exchange", "test-exchange");
        ReflectionTestUtils.setField(rabbitMQProducer, "routingKey", "test-routingKey");
    }

    @Test
    public void testSendMessage() {
        String message = "Hello, World!";
        rabbitMQProducer.sendMessage(message);
        verify(rabbitTemplate, times(1)).convertAndSend("test-exchange", "test-routingKey", message);
    }
}