package com.example.paymentsubscription;

import com.stripe.Stripe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentSubscriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentSubscriptionApplication.class, args);
        Stripe.apiKey = "sk_test_51NikIwERVyfmnVvzkMFStyD2QZw7YyIZBLryGdnFfKmjJdUOxdPv0sbw2SnpJ8dVAuWib0MP14WgunuIPEPOLK5a00KK5eb8xU";

    }

}
