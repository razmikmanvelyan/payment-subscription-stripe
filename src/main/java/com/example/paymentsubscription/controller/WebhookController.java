package com.example.paymentsubscription.controller;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class WebhookController {

    @Value("${stripe.endpoint.secret}")
    private String endpointSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            System.out.println("⚠️  Webhook error while validating signature.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
        Subscription subscription;

        switch (event.getType()) {
            case "customer.subscription.deleted":
                subscription = (Subscription) stripeObject;
                // Handle the event customer.subscription.deleted
                // handleSubscriptionTrialEnding(subscription);
                break;
            case "customer.subscription.trial_will_end":
                subscription = (Subscription) stripeObject;
                // Handle the event customer.subscription.trial_will_end
                // handleSubscriptionDeleted(subscription);
                break;
            case "customer.subscription.created":
                subscription = (Subscription) stripeObject;
                // Handle the event customer.subscription.created
                // handleSubscriptionCreated(subscription);
                break;
            case "customer.subscription.updated":
                subscription = (Subscription) stripeObject;
                // Handle the event customer.subscription.updated
                // handleSubscriptionUpdated(subscription);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}