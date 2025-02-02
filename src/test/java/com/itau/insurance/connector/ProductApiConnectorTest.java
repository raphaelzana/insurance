package com.itau.insurance.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.insurance.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProductApiConnectorTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @Mock
    private Logger logger;

    @InjectMocks
    private ProductApiConnector productApiConnector;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetInsuranceDataByProductId_Success() throws Exception {
        String productId = "123";
        String url = "https://run.mocky.io/v3/2dd8a5c0-eb42-4538-a9a5-bde5de14f435/" + productId;
        String jsonResponse = "{\"id\":\"123\",\"name\":\"Test Insurance\"}";

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(jsonResponse);

        Product expectedResponse = objectMapper.readValue(jsonResponse, Product.class);
        Product actualResponse = productApiConnector.getInsuranceDataByProductId(productId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getName(), actualResponse.getName());
    }

    @Test
    public void testGetInsuranceDataByProductId_Failure() throws Exception {
        String productId = "123";
        String url = "https://run.mocky.io/v3/2dd8a5c0-eb42-4538-a9a5-bde5de14f435/" + productId;

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(404);

        Product actualResponse = productApiConnector.getInsuranceDataByProductId(productId);

        assertNull(actualResponse);
        verify(logger).error("Error fetching data from API. Status code: {}", 404);
    }

    @Test
    public void testGetInsuranceDataByProductId_Exception() throws Exception {
        String productId = "123";
        String url = "https://run.mocky.io/v3/2dd8a5c0-eb42-4538-a9a5-bde5de14f435/" + productId;

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new RuntimeException("API error"));

        Product actualResponse = productApiConnector.getInsuranceDataByProductId(productId);

        assertNull(actualResponse);
        verify(logger).error(eq("Exception occurred while fetching data from API"), any(RuntimeException.class));
    }
}