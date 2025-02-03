package com.itau.insurance.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.insurance.connector.OfferApiConnector;
import com.itau.insurance.connector.ProductApiConnector;
import com.itau.insurance.message.producer.RabbitMQProducer;
import com.itau.insurance.model.InsuranceQuote;
import com.itau.insurance.model.Product;
import com.itau.insurance.model.Quote;
import com.itau.insurance.model.Offer;
import com.itau.insurance.repository.QuoteRepository;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InsuranceService {

    private static final Logger logger = LoggerFactory.getLogger(InsuranceService.class);

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private RabbitMQProducer producer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProductApiConnector productApiConnector;

    @Autowired
    private OfferApiConnector offerApiConnector;

    @Transactional
    public InsuranceQuote quoteInsurance(InsuranceQuote body) throws Exception {
        logger.info("Processing insurance quote request: {}", body);

        Product productResponse = productApiConnector.getInsuranceDataByProductId(body.getProduct_id());
        logger.info("API PRODUCT RESPONSE: {}", productResponse);
        validateProductResponse(productResponse);

        Offer offerResponse = offerApiConnector.getOfferDataByOfferId(body.getOffer_id());
        logger.info("API OFFER RESPONSE: {}", offerResponse);
        validateOfferResponse(body, offerResponse);

        Quote quote = saveInsuranceQuote(body);
        logger.info("Quote saved: {}", quote);

        body.setId(quote.getId());
        producer.sendMessage(objectMapper.writeValueAsString(body));

        return body;
    }

    public void updateInsuranceQuote(String message) throws Exception {
        logger.info("Processing insurance quote update request: {}", message);
        InsuranceQuote insuranceQuote = objectMapper.readValue(message, InsuranceQuote.class);
        logger.info("Insurance quote: {}", insuranceQuote);

        if (quoteRepository.findById(insuranceQuote.getId()).isEmpty()) {
            logger.info("Insurance ID {} not found.", insuranceQuote.getId());
            throw new IllegalArgumentException("Insurance ID " + insuranceQuote.getId() + " not found.");
        }

        Quote quote = Quote.builder()
            .id(insuranceQuote.getId())
            .insurance_policy_id(insuranceQuote.getInsurance_policy_id())
            .json_data(objectMapper.writeValueAsString(insuranceQuote))
            .created_at(insuranceQuote.getCreated_at())
            .updated_at(insuranceQuote.getUpdated_at())
            .build();

        logger.info("Updating quote: {}", quote);
        quoteRepository.save(quote);
    }

    public InsuranceQuote findInsuranceQuote(Long id) throws Exception {
        logger.info("Processing insurance quote finding by id: {}", id);

        Quote quote = quoteRepository.findById(id).orElseThrow(() -> {
            logger.error("Quote not found.");
            return new IllegalArgumentException("Quote not found.");
        });

        logger.info("Quote: {}", quote);

        try {
            return objectMapper.readValue(quote.getJson_data(), InsuranceQuote.class);
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON data for quote ID: {}", id, e);
            throw new RuntimeException("Error processing JSON data for quote ID: " + id, e);
        }
    }

    private Quote saveInsuranceQuote(InsuranceQuote insuranceQuote) throws JsonProcessingException {
        Quote quote = Quote.builder()
            .insurance_policy_id(insuranceQuote.getInsurance_policy_id())
            .json_data(objectMapper.writeValueAsString(insuranceQuote))
            .build();
        return quoteRepository.save(quote);
    }

    private void validateProductResponse(Product productResponse) {
        logger.info("Validating product response: {}", productResponse);
        if (productResponse == null) {
            logger.error("Product not found.");
            throw new IllegalArgumentException("Product not found.");
        } else if (!productResponse.isActive()) {
            logger.error("Product is not active.");
            throw new IllegalArgumentException("Product is not active.");
        }
    }

    private void validateOfferResponse(InsuranceQuote body, Offer offerResponse) {
        logger.info("Validating offer response: {}", offerResponse);
        if (offerResponse == null) {
            logger.error("Offer not found.");
            throw new IllegalArgumentException("Offer not found.");
        } else if (!offerResponse.isActive()) {
            logger.error("Offer is not active.");
            throw new IllegalArgumentException("Offer is not active.");
        } else if (!areAllCoveragesFromQuotePresentInOffer(body.getCoverages(), offerResponse.getCoverages())) {
            logger.error("Not all coverages from the request are present in the offer.");
            throw new IllegalArgumentException("Not all coverages from the request are present in the offer.");
        } else if (!sumCoverageValues(body.getCoverages()).equals(body.getTotal_coverage_amount())) {
            logger.error("Sum of coverage values not equals to total amount in the offer.");
            throw new IllegalArgumentException("Sum of coverage values is less than the minimum amount in the offer.");
        } else if (!areAllAssistancesFromQuotePresentInOffer(body.getAssistances(), offerResponse.getAssistances())) {
            logger.error("Not all assistances from the request are present in the offer.");
            throw new IllegalArgumentException("Not all assistances from the request are present in the offer.");
        } else if (body.getTotal_monthly_premium_amount() < offerResponse.getMonthly_premium_amount().getMin_amount()) {
            logger.error("Total monthly premium amount is less than the minimum amount in the offer.");
            throw new IllegalArgumentException("Total monthly premium amount is less than the minimum amount in the offer.");
        } else if (body.getTotal_monthly_premium_amount() > offerResponse.getMonthly_premium_amount().getMax_amount()) {
            logger.error("Total monthly premium amount is more than the maximum amount in the offer.");
            throw new IllegalArgumentException("Total monthly premium amount is more than the maximum amount in the offer.");
        }
    }

    private boolean areAllCoveragesFromQuotePresentInOffer(Map<String, Double> quoteCoverages, Map<String, Double> offerCoverages) {
        for (Map.Entry<String, Double> entry : quoteCoverages.entrySet()) {
            if (!offerCoverages.containsKey(entry.getKey())) {
                logger.warn("Element {} not found in offer coverages", entry);
                return false;
            }
        }
        return true;
    }

    private Double sumCoverageValues(Map<String, Double> coverages) {
        return coverages.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    private boolean areAllAssistancesFromQuotePresentInOffer(List<String> quoteAssistances, List<String> offerAssistances) {
        for (String assistance : quoteAssistances) {
            if (!offerAssistances.contains(assistance)) {
                logger.warn("Element {} not found in offer assistances", assistance);
                return false;
            }
        }
        return true;
    }
}
