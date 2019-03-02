package com.checkout.test.model;

import java.math.BigDecimal;

/**
 * Class to represent an item of stock.
 *
 * It has an SKU, a cost per unit and any associated offer
 */
public class Item {

    /**
     * Stock keeping unit
     *
     * Unique identifier for this product
     */
    private String sku;

    /**
     * The cost per unit of this item
     */
    private BigDecimal unitCost;

    /**
     * Any associated offer, if applicable
     * (otherwise null)
     */
    private Offer offer;

    /**
     * All args constructor
     *
     * @param sku
     * @param unitCost
     * @param offer
     */
    public Item(String sku, BigDecimal unitCost, Offer offer) {
        this.sku = sku;
        this.unitCost = unitCost;
        this.offer = offer;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
