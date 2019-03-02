package com.checkout.test.model;


import java.math.BigDecimal;

/**
 * Class to represent an offer.
 *
 * Presented in the format of @itemCount items for price @offerPrice
 *
 * E.g. 3 for £1.00 or 5 for £2.50
 */
public class Offer {

    /**
     * The number of items for the offer
     */
    private Integer itemCount;

    /**
     * The price for the itemCount number of items
     * (i.e. the total, not per unit)
     */
    private BigDecimal offerPrice;

    /**
     * All args constructor
     *
     * @param itemCount
     * @param offerPrice
     */
    public Offer(Integer itemCount, BigDecimal offerPrice) {
        this.itemCount = itemCount;
        this.offerPrice = offerPrice;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public BigDecimal getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(BigDecimal offerPrice) {
        this.offerPrice = offerPrice;
    }
}
