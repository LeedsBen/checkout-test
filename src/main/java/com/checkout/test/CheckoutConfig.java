package com.checkout.test;

import com.checkout.test.controller.CheckoutController;
import com.checkout.test.model.Item;
import com.checkout.test.model.Offer;
import com.checkout.test.service.CheckoutService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.checkout.test")
public class CheckoutConfig {

}
