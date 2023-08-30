package com.example.paymentsubscription.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.PriceCollection;
import com.stripe.model.PriceSearchResult;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceListParams;
import com.stripe.param.PriceSearchParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.DefaultUriBuilderFactory;

@RestController
public class SubscriptionController {

    final String YOUR_DOMAIN = "http://localhost:4242";

    @PostMapping("/create-checkout-session")
    public ModelAndView createCheckoutSession(@RequestParam("lookup_key") String lookupKey) throws StripeException {
        PriceListParams priceParams = PriceListParams.builder().addLookupKeys(lookupKey).build();
        PriceCollection prices = Price.list(priceParams);
        PriceSearchParams paramsTemp =
                PriceSearchParams
                        .builder()
                        .setQuery("active:'true'")
                        .build();

        PriceSearchResult result = Price.search(paramsTemp);
        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(result.getData().get(0).getId()).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(YOUR_DOMAIN + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(YOUR_DOMAIN + "/cancel.html")
                .build();
        Session session = Session.create(params);
        return new ModelAndView("redirect:" + session.getUrl());
    }

    @PostMapping("/create-portal-session")
    public ModelAndView createPortalSession(@RequestParam("session_id") String sessionId) throws StripeException {
        // For demonstration purposes, we're using the Checkout session to retrieve the customer ID.
        // Typically, this is stored alongside the authenticated user in your database.
        Session checkoutSession = Session.retrieve(sessionId);
        String customer = checkoutSession.getCustomer();
        // Authenticate your user.
        com.stripe.param.billingportal.SessionCreateParams params =
                com.stripe.param.billingportal.SessionCreateParams.builder()
                        .setReturnUrl(YOUR_DOMAIN)
                        .setCustomer(customer)
                        .build();

        com.stripe.model.billingportal.Session portalSession = com.stripe.model.billingportal.Session.create(params);

        return new ModelAndView("redirect:" + portalSession.getUrl());
    }
}
