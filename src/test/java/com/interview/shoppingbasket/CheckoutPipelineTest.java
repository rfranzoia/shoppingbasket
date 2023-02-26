package com.interview.shoppingbasket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckoutPipelineTest {

    CheckoutPipeline checkoutPipeline;

    @Mock
    Basket basket;

    @Mock
    CheckoutStep checkoutStep1;

    @Mock
    CheckoutStep checkoutStep2;

    List<Promotion> promotions = new ArrayList<>();

    PromotionsService promotionsService;

    PricingService pricingService;

    @BeforeEach
    void setup() {
        checkoutPipeline = new CheckoutPipeline();
        promotionsService = Mockito.mock(PromotionsService.class);

        pricingService = Mockito.mock(PricingService.class);

        Promotion p1 = new Promotion("productCode", Promotion.PromotionType.RETAIL_PRICE_OFF_10);
        Promotion p2 = new Promotion("productCode2", Promotion.PromotionType.RETAIL_PRICE_OFF_50);

        promotions.add(p1);
        promotions.add(p2);
    }

    @Test
    void returnZeroPaymentForEmptyPipeline() {
        PaymentSummary paymentSummary = checkoutPipeline.checkout(basket);

        assertEquals(paymentSummary.getRetailTotal(), 0.0);
    }

    @Test
    void executeAllPassedCheckoutSteps() {
        basket = new Basket();
        basket.add("productCode", "myProduct", 10);
        basket.add("productCode", "myProduct", 5);
        basket.add("productCode2", "myProduct2", 10);
        basket.add("productCode2", "myProduct2", 5);

        PromotionCheckCheckoutStep promotionCheckCheckoutStep = new PromotionCheckCheckoutStep(promotionsService);
        when(promotionsService.getPromotions(basket)).thenReturn(promotions);

        BasketConsolidationCheckoutStep basketConsolidationCheckoutStep = new BasketConsolidationCheckoutStep();

        RetailPriceCheckoutStep retailPriceCheckoutStep = new RetailPriceCheckoutStep(pricingService);
        when(pricingService.getPrice("productCode")).thenReturn(5.0);
        when(pricingService.getPrice("productCode2")).thenReturn(2.0);

        checkoutPipeline.addStep(promotionCheckCheckoutStep);
        checkoutPipeline.addStep(basketConsolidationCheckoutStep);
        checkoutPipeline.addStep(retailPriceCheckoutStep);

        PaymentSummary paymentSummary = checkoutPipeline.checkout(basket);

        assertEquals(paymentSummary.getRetailTotal(), 82.5);

    }

}
