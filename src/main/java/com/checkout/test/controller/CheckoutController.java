package com.checkout.test.controller;

import com.checkout.test.exception.UnknownItemException;
import com.checkout.test.model.Item;
import com.checkout.test.service.CheckoutService;
import com.checkout.test.util.PricingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * REST Controller to expose functionality via HTTP REST
 *
 * This configuration supports one checkout with one customer at a time
 * (It could be easily extended to manage multiple checkouts)
 *
 */
@RestController
public class CheckoutController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutController.class);

    private CheckoutService checkoutService;

    // initialise with stub for now
    private Map<String, Item> productData = PricingUtil.basicProductData();

    public CheckoutController(CheckoutService service) {
        this.checkoutService = service;
    }

    /**
     * @see com.checkout.test.service.CheckoutService#start(Map)
     */
    @GetMapping("/checkout/start")
    public ResponseEntity<Void> start() {
        LOGGER.info("Starting checkout...");
        checkoutService.start(productData);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * @see com.checkout.test.service.CheckoutService#addItem(String, Integer)
     */
    @PostMapping("/checkout/addItem")
    public ResponseEntity<Void> addItem(@RequestParam String sku, @RequestParam Integer quantity) {
        checkoutService.addItem(sku, quantity);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * @see CheckoutService#calculateTotal()
     */
    @GetMapping("/checkout/calculateBasketTotal")
    public BigDecimal calculateBasketTotal() {
        return checkoutService.calculateTotal();
    }

    /**
     * @see CheckoutService#finishAndReset()
     */
    @PostMapping("/checkout/finish")
    public ResponseEntity<Void> finish() {
        LOGGER.info("Finishing checkout");
        checkoutService.finishAndReset();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * Endpoint to push new product data to
     *
     * @param productData
     * @return
     */
    @PutMapping("/checkout/updateProductData")
    public ResponseEntity<Void> updateProductData(@RequestBody Map<String, Item> productData) {
        LOGGER.info("Adding new product data with item count of " + productData.size());
        this.productData = productData;
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * Protected get method for testing
     *
     * @return
     */
    protected Map<String, Item> getProductData() {
        return this.productData;
    }
}
