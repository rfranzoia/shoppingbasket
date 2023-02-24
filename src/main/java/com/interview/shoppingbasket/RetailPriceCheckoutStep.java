package com.interview.shoppingbasket;

import java.util.List;
import java.util.stream.Collectors;

public class RetailPriceCheckoutStep implements CheckoutStep {
    private PricingService pricingService;
    private double retailTotal;

    public RetailPriceCheckoutStep(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Override
    public void execute(CheckoutContext checkoutContext) {
        Basket basket = checkoutContext.getBasket();
        retailTotal = 0.0;

        for (BasketItem basketItem: basket.getItems()) {
            int quantity = basketItem.getQuantity();
            double price = pricingService.getPrice(basketItem.getProductCode());
            price = checkPromotions(checkoutContext, basketItem, price);
            basketItem.setProductRetailPrice(price);
            retailTotal += quantity*price;
        }

        checkoutContext.setRetailPriceTotal(retailTotal);
    }

    public double checkPromotions(CheckoutContext checkoutContext, BasketItem basketItem, double price) {
        List<Promotion> promotions = checkoutContext.getPromotions().stream()
                .filter(promotion -> promotion.getProductCode().equals(basketItem.getProductCode()))
                .collect(Collectors.toList());
        for (Promotion promotion : promotions) {
            price = applyPromotion(promotion, basketItem, price);
        }
        return price;
    }

    public double applyPromotion(Promotion promotion, BasketItem item, double price) {
        switch (promotion.getPromotionType()) {
            case RETAIL_PRICE_OFF_10:
                return price * 0.9;

            case RETAIL_PRICE_OFF_50:
                return price * 0.5;

            case BUY_TWO_PAY_ONE:
                if (item.getQuantity() > 1) {
                    int oddItem = item.getQuantity() % 2;
                    price = ((price / item.getQuantity()) / 2) + (price * oddItem);
                }
            default:
                return price;
        }
    }
}
