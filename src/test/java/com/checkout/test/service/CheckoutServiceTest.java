package com.checkout.test.service;

import com.checkout.test.enums.ProcessingStatus;
import com.checkout.test.exception.UnknownItemException;
import com.checkout.test.util.PricingUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckoutServiceTest {

    /**
     * Test to call total on empty basket
     */
    @Test
    public void noItemsTest() {
        CheckoutService cos = new CheckoutService();
        cos.start(PricingUtil.basicProductData());
        assertEquals(new BigDecimal(0), cos.calculateTotal());
    }

    /**
     * Very simple test with 3 items each of 1 quantity
     */
    @Test
    public void simpleTest() throws UnknownItemException {
        CheckoutService cos = new CheckoutService();
        cos.start(PricingUtil.basicProductData());
        cos.addItem("a", 1); // £1
        cos.addItem("b", 1); // £2
        cos.addItem("c", 1); // £3
        assertEquals(new BigDecimal(6), cos.calculateTotal());
    }

    /**
     * Simple test with multiple quantities but no offers
     */
    @Test
    public void multipleQuatityTest() throws UnknownItemException {
        CheckoutService cos = new CheckoutService();
        cos.start(PricingUtil.basicProductData());
        cos.addItem("a", 3); // £3
        cos.addItem("b", 2); // £4
        cos.addItem("c", 1); // £3
        assertEquals(new BigDecimal(10), cos.calculateTotal());
    }

    /**
     * Test with a bunch of items that have offers
     */
    @Test
    public void offerTest() throws UnknownItemException {
        CheckoutService cos = new CheckoutService();
        cos.start(PricingUtil.basicProductData());
        cos.addItem("c", 2); // £6
        cos.addItem("d", 4); // £10 2(2 for 5) = 10
        cos.addItem("e", 7); // £30 (5 for 20) + (2 * 5) = 30
        assertEquals(new BigDecimal(46), cos.calculateTotal());
    }

    /**
     * Test for item that is not in the product data
     */
    @Test
    public void badItemTest() {
        CheckoutService cos = new CheckoutService();
        cos.start(PricingUtil.basicProductData());
        assertThrows(UnknownItemException.class, () -> {
            cos.addItem("k", 3);
        });
    }

    /**
     * Test to ensure item cannot be added without calling start
     */
    @Test
    public void addItemWithoutStartingTest() {
        CheckoutService cos = new CheckoutService();
        assertThrows(IllegalStateException.class, () -> {
            cos.addItem("c", 2);
        });
    }

    /**
     * Test to ensure total cannot be calculated without calling start
     */
    @Test
    public void calculateTotalWithoutStartingTest() {
        CheckoutService cos = new CheckoutService();
        assertThrows(IllegalStateException.class, () -> {
            cos.calculateTotal();
        });
    }

    /**
     * Test for multiple checkouts
     */
    @Test
    public void multipleCustomersTest() {
        CheckoutService cos = new CheckoutService();
        cos.start(PricingUtil.basicProductData());
        cos.addItem("a", 3); // £3
        cos.addItem("b", 2); // £4
        cos.addItem("c", 1); // £3
        assertEquals(new BigDecimal(10), cos.calculateTotal());
        cos.finishAndReset();
        Assertions.assertEquals(ProcessingStatus.IDLE, cos.getProcessingStatus());
        cos.start(PricingUtil.basicProductData());
        cos.addItem("c", 2); // £6
        cos.addItem("d", 4); // £10 2(2 for 5) = 10
        cos.addItem("e", 7); // £30 (5 for 20) + (2 * 5) = 30
        assertEquals(new BigDecimal(46), cos.calculateTotal());
        Assertions.assertEquals(ProcessingStatus.PROCESSING, cos.getProcessingStatus());
        cos.finishAndReset();
        Assertions.assertEquals(ProcessingStatus.IDLE, cos.getProcessingStatus());
    }
}
