package com.interview.shoppingbasket;

public class PromotionCheckCheckoutStep implements CheckoutStep {

    PromotionsService promotionsService;

    public PromotionCheckCheckoutStep(PromotionsService promotionsService) { this.promotionsService = promotionsService; }

    @Override
    public void execute(CheckoutContext checkoutContext) {
        Basket basket = checkoutContext.getBasket();

        checkoutContext.setPromotions(promotionsService.getPromotions(basket));
    }

}
