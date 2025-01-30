package com.itau.insurance.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.insurance.connector.OfferApiConnector;
import com.itau.insurance.connector.ProductApiConnector;
import com.itau.insurance.message.producer.RabbitMQProducer;
import com.itau.insurance.model.InsuranceQuoteRequest;
import com.itau.insurance.model.InsuranceResponse;
import com.itau.insurance.model.OfferResponse;
import com.itau.insurance.model.Quote;
import com.itau.insurance.repository.QuoteRepository;

import jakarta.transaction.Transactional;

@Service
public class InsuranceService {


    @Autowired
    private QuoteRepository cotacaoRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RabbitMQProducer producer;

    public void quoteInsurace(InsuranceQuoteRequest body) throws Exception{

        InsuranceResponse response = ProductApiConnector.getInsuranceDataByProductId(body.getProduct_id());
        System.out.println("API PRODUCT RESPONSE: " + response);
        if (!response.isActive()){
            throw new Exception();
        }

        OfferResponse offerResponse = OfferApiConnector.getOfferDataByOfferId(body.getOffer_id());
        System.out.println("API OFFER RESPONSE: " + offerResponse.toString());
        if (!offerResponse.isActive() ||
                !areAllElementsFromMap1PresentInMap2(body.getCoverages(), offerResponse.getCoverages()) ||
                !sumCoverageValues(body.getCoverages()).equals(body.getTotal_coverage_amount()) ||
                !areAllElementsFromList1PresentInList2(body.getAssistances(), offerResponse.getAssistances()) ||
                body.getTotal_monthly_premium_amount() < offerResponse.getMonthly_premium_amount().getMin_amount() ||
                body.getTotal_monthly_premium_amount() > offerResponse.getMonthly_premium_amount().getMax_amount() 
            )
        {
            throw new Exception();
        }

        Quote quote = new Quote();
        quote.setJsonData(objectMapper.writeValueAsString(body));
        System.out.println(cotacaoRepository.findAll());
        quote = cotacaoRepository.save(quote);
        System.out.println("Entity saved: " + quote.toString());
        body.setId(quote.getId());

        producer.sendMessage(objectMapper.writeValueAsString(body));

    }
    
    private boolean areAllElementsFromMap1PresentInMap2(Map<String, Double> map1, Map<String, Double> map2){
        for (Map.Entry<String, Double> entry : map1.entrySet()) {
            String key = entry.getKey();

            // Verifica se map2 contém a chave e se o valor é igual
            if (!map2.containsKey(key)) {
                System.out.println("areAllElementsFromMap1PresentInMap2 returning false");
                return false; // Se não contém a chave ou os valores não são iguais
            }
        }

        // Todos os elementos do map1 estão presentes no map2
        System.out.println("areAllElementsFromMap1PresentInMap2 returning true");
        return true;
    }

    private Double sumCoverageValues(Map<String, Double> coverages){
        double sum = 0.0;
        // Itera sobre cada entrada do mapa e soma os valores
        for (Map.Entry<String, Double> entry : coverages.entrySet()) {
            sum += entry.getValue(); // Soma o valor associado a cada chave
        }
        System.out.println("sumCoverageValues result is " + sum);
        return sum;
    }

    private static boolean areAllElementsFromList1PresentInList2(List<String> list1, List<String> list2) {
        // Itera sobre cada elemento de list1 e verifica se ele está presente em list2
        for (String element : list1) {
            if (!list2.contains(element)) {
                System.out.println("areAllElementsFromList1PresentInList2 returning false");
                return false;  // Se algum elemento não estiver em list2, retorna false
            }
        }
        System.out.println("areAllElementsFromList1PresentInList2 returning true");
        return true;  // Se todos os elementos de list1 estão em list2, retorna true
    }
    
}
