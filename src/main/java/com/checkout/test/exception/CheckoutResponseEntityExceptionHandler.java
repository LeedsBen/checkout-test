package com.checkout.test.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.math.BigDecimal;

/**
 * Exception handling - map exception to HTTP code
 */
@ControllerAdvice
@RestController
public class CheckoutResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutResponseEntityExceptionHandler.class);

    /**
     * An unknown item is a bad request
     *
     * @param uie
     * @return
     */
    @ExceptionHandler(UnknownItemException.class)
    public final ResponseEntity<String> handleUnknownItemException(UnknownItemException uie) {
        LOGGER.error("Unknown Item: " + uie.getMessage());
        return new ResponseEntity<String>(uie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * An illegal state is a conflict
     *
     * @param ise
     * @return
     */
    @ExceptionHandler(IllegalStateException.class)
    public final ResponseEntity<?> handleIllegalStateException(IllegalStateException ise) {
        LOGGER.error("Illegal State: Checkout must start before items can be added or totals calculated.");
        // check error message as Illegal State can happen when handling multiple types
        if (ise.getMessage().startsWith("Cannot add item")) {
            return new ResponseEntity<String>(ise.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<BigDecimal>(HttpStatus.CONFLICT);
    }
}
