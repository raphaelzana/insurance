package com.itau.insurance.connector;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.insurance.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductApiConnector {

    private static final Logger logger = LoggerFactory.getLogger(ProductApiConnector.class);

    @Value("${product.api.base.url}")
    private String baseUrl;

    public Product getInsuranceDataByProductId(String productId) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String url = baseUrl + productId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.body(), Product.class);
            } else {
                logger.error("Error fetching data from API. Status code: {}", response.statusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while fetching data from API", e);
        }
        return null;
    }
}