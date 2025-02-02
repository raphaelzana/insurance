package com.itau.insurance.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.insurance.model.InsuranceQuote;
import com.itau.insurance.model.Quote;
import com.itau.insurance.repository.QuoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RabbitMQConsumerTest {

    @InjectMocks
    private RabbitMQConsumer rabbitMQConsumer;

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConsumeSuccess() throws Exception {
        String message = "{\"id\":1}";
        InsuranceQuote request = new InsuranceQuote();
        request.setId(1L);
        

        when(objectMapper.readValue(message, InsuranceQuote.class)).thenReturn(request);
        when(quoteRepository.findById(1L)).thenReturn(Optional.of(Quote.builder().id(1L).build()));

        rabbitMQConsumer.consume(message);

        verify(quoteRepository).save(any(Quote.class));
    }

    @Test
    public void testConsumeFailure() throws Exception {
        String message = "{\"id\":1}";
        InsuranceQuote request = new InsuranceQuote();
        request.setId(1L);

        when(objectMapper.readValue(message, InsuranceQuote.class)).thenReturn(request);
        when(quoteRepository.findById(1L)).thenReturn(Optional.empty());

        rabbitMQConsumer.consume(message);

        verify(quoteRepository, never()).save(any(Quote.class));
    }
}
