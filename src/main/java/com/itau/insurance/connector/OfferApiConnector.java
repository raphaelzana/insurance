package com.itau.insurance.connector;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.insurance.model.Offer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OfferApiConnector {

    private static final Logger logger = LoggerFactory.getLogger(OfferApiConnector.class);

    @Value("${offer.api.base.url}")
    private String baseUrl;

    // Método para obter os dados de uma oferta a partir da ID da oferta
    public Offer getOfferDataByOfferId(String offerId) {
        try {
            // Criação do HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // URL com o ID da oferta para buscar os detalhes da oferta
            String url = baseUrl + offerId;

            // Requisição GET para a API REST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url)) // Passando a URL com o ID da oferta
                    .header("Accept", "application/json")
                    .build();

            // Envio da requisição e obtenção da resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Se o código de status for 200, fazer o parse do JSON para o objeto Java
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.body(), Offer.class);
            } else {
                logger.error("Error fetching data from API. Status code: {}", response.statusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while fetching data from API", e);
        }
        return null;
    }
}
