package com.itau.insurance.connector;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.insurance.model.OfferResponse;

public class OfferApiConnector {

    // Método para obter os dados de uma oferta a partir da ID da oferta
    public static OfferResponse getOfferDataByOfferId(String offerId) {
        try {
            // Criação do HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // URL com o ID da oferta para buscar os detalhes da oferta
            String url = "https://run.mocky.io/v3/6b4207d8-d766-4bf9-9e68-ea3224ca513e/" + offerId;  // Substitua pela URL real da API

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
                return objectMapper.readValue(response.body(), OfferResponse.class);
            } else {
                System.out.println("Erro ao buscar dados da API. Código de status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
