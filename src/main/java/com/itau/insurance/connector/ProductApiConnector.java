package com.itau.insurance.connector;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.insurance.model.InsuranceResponse;

public class ProductApiConnector {

    // Método para conectar e buscar dados da API com ID do produto como parâmetro
    public static InsuranceResponse getInsuranceDataByProductId(String productId) {
        try {
            // Criação do HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // URL com o ID do produto na requisição
            String url = "https://run.mocky.io/v3/2dd8a5c0-eb42-4538-a9a5-bde5de14f435/" + productId;  // Substitua pela URL real da API

            // Requisição GET para a API REST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url)) // Passando a URL com o ID do produto
                    .header("Accept", "application/json")
                    .build();

            // Envio da requisição e obtenção da resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Se o código de status for 200, fazer o parse do JSON para o objeto Java
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.body(), InsuranceResponse.class);
            } else {
                System.out.println("Erro ao buscar dados da API. Código de status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}