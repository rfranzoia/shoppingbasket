package com.interview.shoppingbasket;

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

            // apply promotions associated to the productCode of this item
            checkoutContext.getPromotions().stream()
                            .filter(promotion -> basketItem.getProductCode().equals(promotion.getProductCode()))
                            .forEach(promotion -> basketItem.setProductRetailPrice(applyPromotion(promotion, basketItem, price)));

            retailTotal += quantity*basketItem.getProductRetailPrice();
        }

        checkoutContext.setRetailPriceTotal(retailTotal);
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
