package com.checkout.test.service;

import com.checkout.test.model.Item;
import com.checkout.test.util.PricingUtil;
import com.checkout.test.enums.ProcessingStatus;
import com.checkout.test.exception.UnknownItemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Checkout Service
 *
 * This models a basic shopping basket with an ability to add items and calculate a total.
 *
 */
@Service
public class CheckoutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutService.class);

    /**
     * Pricing Data
     *
     * Key is the SKU
     * Value is the Item data
     */
    private Map<String, Item> productData;

    /**
     * Shopping basket
     *
     * Key is the SKU
     * Value is the quantity
     */
    private Map<String, Integer> shoppingBasket;

    /**
     * Status to indicate current state - always starts IDLE
     */
    private ProcessingStatus processingStatus = ProcessingStatus.IDLE;

    /**
     * Method to initialise a new customer checking out.
     *
     * This method must be called before checking out can begin.
     *
     * @param productData
     */
    public void start(Map<String, Item> productData) {
        if (productData == null || productData.isEmpty()) {
            throw new IllegalStateException("No pricing data for items");
        }
        this.productData = productData;
        shoppingBasket = new HashMap<String, Integer>();
        this.processingStatus = ProcessingStatus.PROCESSING;

        // If debug enabled, write out product data
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Checkout started, status is processing");
            productData.entrySet().stream().forEach(x -> {
                LOGGER.debug("Item: " + x.getKey() + " price " + x.getValue().getUnitCost());
            });
        }
    }

    /**
     * Adds an item to the list of checkout items being purchased by a customer
     *
     * @param sku
     * @param quantity
     * @throws UnknownItemException
     */
    public void addItem(String sku, Integer quantity) {
        LOGGER.debug("Attempting to add item: " + sku);
        if (ProcessingStatus.IDLE.equals(processingStatus)) {
            throw new IllegalStateException("Cannot add item to basket without first calling start()");
        }
        if (!productData.containsKey(sku)) {
            throw new UnknownItemException("Item " + sku + " is not recognised");
        }

        if (shoppingBasket.containsKey(sku)) {
            // if this item is already in the basket, update the total
            Integer newTotal = shoppingBasket.get(sku) + quantity;
            shoppingBasket.replace(sku, newTotal);
        } else {
            // if not, add it as a new item
            shoppingBasket.put(sku, quantity);
        }
    }

    /**
     * Calculates the total value of the current basket
     *
     * @return monetary value of the current basket
     */
    public BigDecimal calculateTotal() {
        LOGGER.debug("Attempting to calculate total");
        if (ProcessingStatus.IDLE.equals(processingStatus)) {
            throw new IllegalStateException("Cannot calculate total if checkout has not started");
        }
        // if called on an empty basket, return 0
        if (shoppingBasket.isEmpty()) {
            return new BigDecimal(0);
        }
        // calculate the cost of each item by quantity and use reduce() to sum it.
        return shoppingBasket.entrySet().stream()
                .map(item -> PricingUtil.calculatePrice(
                        productData.get(item.getKey()), item.getValue()))
                .reduce((x, y) -> x.add(y)).get();
    }

    /**
     * Once checkout has finished, everything needs to be reset and the status set to idle
     */
    public void finishAndReset() {
        // could do something fancier here, but setting to null works fine
        shoppingBasket = null;
        productData = null;
        this.processingStatus = ProcessingStatus.IDLE;
        LOGGER.debug("Processing finished");
    }

    /**
     * Get method to peek at processing status if required.
     *
     * @return current status
     */
    public ProcessingStatus getProcessingStatus() {
        return processingStatus;
    }
}
