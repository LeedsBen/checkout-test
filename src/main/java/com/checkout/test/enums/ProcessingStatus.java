package com.checkout.test.enums;

/**
 * Enum for the processing status of the checkout service
 *
 * It is either 'processing' (i.e. checking out items)
 * or 'Idle' and has not yet started processing items for the next customer
 */
public enum ProcessingStatus {
    PROCESSING, IDLE;
}
