package com.interview.shoppingbasket;

public class Promotion {

    public enum PromotionType {
        BUY_TWO_PAY_ONE, RETAIL_PRICE_OFF_50, RETAIL_PRICE_OFF_10
    }

    private String productCode;
    private PromotionType promotionType;

    public Promotion(String productCode, PromotionType promotionType) {
        this.productCode = productCode;
        this.promotionType = promotionType;
    }

    public String getProductCode() {
        return productCode;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }
}
