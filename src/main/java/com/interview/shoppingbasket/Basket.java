package com.interview.shoppingbasket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Basket {
    private List<BasketItem> items = new ArrayList<>();

    public void add(String productCode, String productName, int quantity) {
        BasketItem basketItem = new BasketItem();
        basketItem.setProductCode(productCode);
        basketItem.setProductName(productName);
        basketItem.setQuantity(quantity);

        items.add(basketItem);
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void consolidateItems() {
        if (items.isEmpty()) return;

        // group all product codes
        Map<String, Integer> consolidatedItemsMap =
                items.stream()
                        .collect(
                                Collectors.groupingBy(BasketItem::getProductCode,
                                        Collectors.reducing(0, BasketItem::getQuantity, Integer::sum)));

        // rebuild the list of basket items
        List<BasketItem> consolidatedItems = consolidatedItemsMap.entrySet().stream()
                .map(e -> {
                    BasketItem bI = new BasketItem();
                    bI.setProductCode(e.getKey());
                    bI.setQuantity(e.getValue());
                    bI.setProductName(items.stream()
                            .filter(i -> i.getProductCode() == e.getKey())
                            .findFirst().get().getProductName());
                    return bI;
                }).collect(Collectors.toList());

        // move to original list
        items.clear();
        items.addAll(consolidatedItems);
    }
}
