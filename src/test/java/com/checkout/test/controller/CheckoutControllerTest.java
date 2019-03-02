package com.checkout.test.controller;

import com.checkout.test.model.Item;
import com.checkout.test.model.Offer;
import com.checkout.test.service.CheckoutService;
import com.checkout.test.util.PricingUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test class for the controller
 */
@ExtendWith(MockitoExtension.class)
public class CheckoutControllerTest {

    @Mock
    private CheckoutService checkoutService;

    /**
     * Test start()
     */
    @Test
    public void checkoutStartTest() {
        CheckoutController controller = new CheckoutController(checkoutService);
        ResponseEntity<Void> response = controller.start();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test addItem()
     */
    @Test
    public void checkoutAddItemTest() {
        CheckoutController controller = new CheckoutController(checkoutService);
        ResponseEntity<Void> response = controller.addItem("a", 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test calculateBasketTotal()
     */
    @Test
    public void checkoutCalcTotalTest() {
        CheckoutController controller = new CheckoutController(checkoutService);
        BigDecimal response = controller.calculateBasketTotal();
        // if we get to here, it's ok
        assertTrue(true);
    }

    /**
     * Test finish()
     */
    @Test
    public void checkoutFinishTest() {
        CheckoutController controller = new CheckoutController(checkoutService);
        ResponseEntity<Void> response = controller.finish();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test updateProductData()
     */
    @Test
    public void checkoutNewProductDataTest() {
        CheckoutController controller = new CheckoutController(checkoutService);
        ResponseEntity<Void> response = controller.updateProductData(getNewProductData());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Item> newData = controller.getProductData();
        assertEquals(3, newData.size());
    }

    private Map<String, Item> getNewProductData() {
        Map<String, Item> productData = new HashMap<>();
        Item item1 = new Item("a", new BigDecimal(10), null);
        Item item2 = new Item("c", new BigDecimal(15), new Offer(2,new BigDecimal(25)));
        Item item3 = new Item("d", new BigDecimal(20), new Offer(3,new BigDecimal(50)));
        productData.put(item1.getSku(), item1);
        productData.put(item2.getSku(), item2);
        productData.put(item3.getSku(), item3);
        return productData;
    }
}
