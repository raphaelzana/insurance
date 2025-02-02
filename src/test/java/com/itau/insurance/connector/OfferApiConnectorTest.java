package com.itau.insurance.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.insurance.model.Offer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class OfferApiConnectorTest {

    private HttpClient httpClient;
    private HttpResponse<String> httpResponse;
    private ObjectMapper objectMapper;
    private Logger logger;

    @BeforeEach
    public void setUp() {
        httpClient = mock(HttpClient.class);
        httpResponse = mock(HttpResponse.class);
        objectMapper = mock(ObjectMapper.class);
        logger = mock(Logger.class);
    }

    @Test
    public void testGetOfferDataByOfferId_Success() throws Exception {
        String offerId = "123";
        String url = "https://run.mocky.io/v3/6b4207d8-d766-4bf9-9e68-ea3224ca513e/" + offerId;
        String jsonResponse = "{\"id\":\"123\",\"name\":\"Test Offer\"}";
        Offer expectedResponse = new Offer();
        expectedResponse.setId("123");
        expectedResponse.setName("Test Offer");

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(jsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(objectMapper.readValue(jsonResponse, Offer.class)).thenReturn(expectedResponse);

        try (MockedStatic<HttpClient> mockedHttpClient = Mockito.mockStatic(HttpClient.class);
             MockedStatic<ObjectMapper> mockedObjectMapper = Mockito.mockStatic(ObjectMapper.class)) {
            mockedHttpClient.when(HttpClient::newHttpClient).thenReturn(httpClient);
            mockedObjectMapper.when(ObjectMapper::new).thenReturn(objectMapper);

            OfferApiConnector offerApiConnector = new OfferApiConnector();
            Offer actualResponse = offerApiConnector.getOfferDataByOfferId(offerId);
            assertNotNull(actualResponse);
            assertEquals(expectedResponse.getId(), actualResponse.getId());
            assertEquals(expectedResponse.getName(), actualResponse.getName());
        }
    }

    @Test
    public void testGetOfferDataByOfferId_Failure() throws Exception {
        String offerId = "123";
        String url = "https://run.mocky.io/v3/6b4207d8-d766-4bf9-9e68-ea3224ca513e/" + offerId;

        when(httpResponse.statusCode()).thenReturn(404);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        try (MockedStatic<HttpClient> mockedHttpClient = Mockito.mockStatic(HttpClient.class)) {
            mockedHttpClient.when(HttpClient::newHttpClient).thenReturn(httpClient);

            OfferApiConnector offerApiConnector = new OfferApiConnector();
            Offer actualResponse = offerApiConnector.getOfferDataByOfferId(offerId);
            assertNull(actualResponse);
            verify(logger).error("Error fetching data from API. Status code: {}", 404);
        }
    }

    @Test
    public void testGetOfferDataByOfferId_Exception() throws Exception {
        String offerId = "123";
        String url = "https://run.mocky.io/v3/6b4207d8-d766-4bf9-9e68-ea3224ca513e/" + offerId;

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new RuntimeException("Test Exception"));

        try (MockedStatic<HttpClient> mockedHttpClient = Mockito.mockStatic(HttpClient.class)) {
            mockedHttpClient.when(HttpClient::newHttpClient).thenReturn(httpClient);

            OfferApiConnector offerApiConnector = new OfferApiConnector();
            Offer actualResponse = offerApiConnector.getOfferDataByOfferId(offerId);
            assertNull(actualResponse);
            verify(logger).error(eq("Exception occurred while fetching data from API"), any(RuntimeException.class));
        }
    }
}