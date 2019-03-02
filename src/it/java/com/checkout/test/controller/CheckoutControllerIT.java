package com.checkout.test.controller;

import com.checkout.test.model.Item;
import com.checkout.test.model.Offer;
import com.checkout.test.service.CheckoutService;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration test class for the controller
 *
 * Test methods named alphabetically to ensure running order
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CheckoutControllerIT {

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private CheckoutService checkoutService;

    /**
     * Test to check the Spring Context loads correctly
     */
    @Test
    public void aIT() {
        assertTrue(true);
    }

    /**
     * Test to hit one method and check response
     */
    @Test
    public void bIT() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>(null, new HttpHeaders());
        ResponseEntity<Void> aliveResponse = restTemplate.exchange("http://localhost:" + localServerPort +"/checkout/start", HttpMethod.GET, entity, Void.class);
        assertEquals(200, aliveResponse.getStatusCodeValue());
    }

    @Test
    public void cIT() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:" + localServerPort;

        // Call start
        HttpEntity<String> entity = new HttpEntity<String>(null, new HttpHeaders());
        ResponseEntity<Void> startResponse = restTemplate.exchange(url +"/checkout/start", HttpMethod.GET, entity, Void.class);
        assertEquals(200, startResponse.getStatusCodeValue());

        // Add an item
        UriComponentsBuilder uriAddItem = UriComponentsBuilder.fromHttpUrl(url + "/checkout/addItem")
                .queryParam("sku", "b")
                .queryParam("quantity", 3);
        ResponseEntity<Void> addItemResponse = restTemplate.exchange(uriAddItem.build().toUri(), HttpMethod.POST, entity, Void.class);
        assertEquals(200, addItemResponse.getStatusCodeValue());

        // Check total
        ResponseEntity<BigDecimal> calcTotalResponse = restTemplate.exchange(url +"/checkout/calculateBasketTotal", HttpMethod.GET, entity, BigDecimal.class);
        assertEquals(200, calcTotalResponse.getStatusCodeValue());
        assertTrue(6d == calcTotalResponse.getBody().doubleValue());

        // Add an invalid item
        UriComponentsBuilder uriAddBadItem = UriComponentsBuilder.fromHttpUrl(url + "/checkout/addItem")
                .queryParam("sku", "k")
                .queryParam("quantity", 7);
        ResponseEntity<Void> addBadItemResponse = restTemplate.exchange(uriAddBadItem.build().toUri(), HttpMethod.POST, entity, Void.class);
        assertEquals(400, addBadItemResponse.getStatusCodeValue());

        // Check total is still what it was
        ResponseEntity<BigDecimal> calcTotalResponse2 = restTemplate.exchange(url +"/checkout/calculateBasketTotal", HttpMethod.GET, entity, BigDecimal.class);
        assertEquals(200, calcTotalResponse2.getStatusCodeValue());
        assertTrue(6d == calcTotalResponse2.getBody().doubleValue());

        // Test finish
        ResponseEntity<Void> finishResponse = restTemplate.exchange(url +"/checkout/finish", HttpMethod.POST, entity, Void.class);
        assertEquals(200, finishResponse.getStatusCodeValue());
    }

    @Test
    public void dIT() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:" + localServerPort;

        // Add an item - not started, should get 409 CONFLICT
        UriComponentsBuilder uriAddItem = UriComponentsBuilder.fromHttpUrl(url + "/checkout/addItem")
                .queryParam("sku", "b")
                .queryParam("quantity", 3);
        HttpEntity<String> entity = new HttpEntity<String>(null, new HttpHeaders());
        ResponseEntity<Void> addItemResponse = restTemplate.exchange(uriAddItem.build().toUri(), HttpMethod.POST, entity, Void.class);
        assertEquals(409, addItemResponse.getStatusCodeValue());

        // Check total - not started, should get 409 CONFLICT
        ResponseEntity<BigDecimal> calcTotalResponse = restTemplate.exchange(url +"/checkout/calculateBasketTotal", HttpMethod.GET, entity, BigDecimal.class);
        assertEquals(409, calcTotalResponse.getStatusCodeValue());
    }

    @Test
    public void eIT() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:" + localServerPort;

        // Post in new product data
        HttpEntity<Map<String, Item>> productEntity = new HttpEntity<Map<String, Item>>(getNewProductData(), new HttpHeaders());
        restTemplate.put(url + "/checkout/updateProductData", productEntity);

        // Call start
        HttpEntity<String> entity = new HttpEntity<String>(null, new HttpHeaders());
        ResponseEntity<Void> startResponse = restTemplate.exchange(url +"/checkout/start", HttpMethod.GET, entity, Void.class);
        assertEquals(200, startResponse.getStatusCodeValue());

        // Add an old item - should error
        UriComponentsBuilder uriAddItem = UriComponentsBuilder.fromHttpUrl(url + "/checkout/addItem")
                .queryParam("sku", "b")
                .queryParam("quantity", 3);
        ResponseEntity<Void> addItemResponse = restTemplate.exchange(uriAddItem.build().toUri(), HttpMethod.POST, entity, Void.class);
        assertEquals(400, addItemResponse.getStatusCodeValue());

        // Add a new item
        UriComponentsBuilder uriNewAddItem = UriComponentsBuilder.fromHttpUrl(url + "/checkout/addItem")
                .queryParam("sku", "z")
                .queryParam("quantity", 2);
        ResponseEntity<Void> addNewItemResponse = restTemplate.exchange(uriNewAddItem.build().toUri(), HttpMethod.POST, entity, Void.class);
        assertEquals(200, addNewItemResponse.getStatusCodeValue());

        // Check total
        ResponseEntity<BigDecimal> calcTotalResponse2 = restTemplate.exchange(url +"/checkout/calculateBasketTotal", HttpMethod.GET, entity, BigDecimal.class);
        assertEquals(200, calcTotalResponse2.getStatusCodeValue());
        assertTrue(40d == calcTotalResponse2.getBody().doubleValue());

        // Test finish
        ResponseEntity<Void> finishResponse = restTemplate.exchange(url +"/checkout/finish", HttpMethod.POST, entity, Void.class);
        assertEquals(200, finishResponse.getStatusCodeValue());
    }

    private Map<String, Item> getNewProductData() {
        Map<String, Item> productData = new HashMap<>();
        Item item1 = new Item("x", new BigDecimal(10), null);
        Item item2 = new Item("y", new BigDecimal(15), new Offer(2,new BigDecimal(25)));
        Item item3 = new Item("z", new BigDecimal(20), new Offer(3,new BigDecimal(50)));
        productData.put(item1.getSku(), item1);
        productData.put(item2.getSku(), item2);
        productData.put(item3.getSku(), item3);
        return productData;
    }
}
