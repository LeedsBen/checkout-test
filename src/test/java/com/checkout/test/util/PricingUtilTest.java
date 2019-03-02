package com.checkout.test.util;

import com.checkout.test.model.Item;
import com.checkout.test.model.Offer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PricingUtilTest {

    /**
     * Test calculating the total where there is no offer
     *
     * 5 items for £10 each
     */
    @Test
    public void noOfferTest() {
        Item noOffers = new Item("a", new BigDecimal(10), null);
        BigDecimal totalCost = PricingUtil.calculatePrice(noOffers, 5);
        assertEquals(50d, totalCost.doubleValue());
    }

    /**
     * Test calculating total where there is an offer and the quantity fits exactly
     *
     * 75p each or 5 for £2.50
     *
     * 10 items for £5
     */
    @Test
    public void exactAmountOfferTest() {
        Offer testOffer = new Offer(5, new BigDecimal(2.5));
        Item itemWithOffer = new Item("b", new BigDecimal(0.75), testOffer);
        BigDecimal totalCost = PricingUtil.calculatePrice(itemWithOffer, 10);
        assertEquals(5d, totalCost.doubleValue());
    }

    /**
     * Test calculating total where there is an offer but a remainder from the quantity
     *
     * £1 each or 7 for £5
     *
     * 20 items, 14 for £10, 6 for £1, £16 total
     */
    @Test
    public void remainderOfferTest() {
        Offer testOffer = new Offer(7, new BigDecimal(5));
        Item itemWithOffer = new Item("c", new BigDecimal(1), testOffer);
        BigDecimal totalCost = PricingUtil.calculatePrice(itemWithOffer, 20);
        assertEquals(16d, totalCost.doubleValue());
    }
}
