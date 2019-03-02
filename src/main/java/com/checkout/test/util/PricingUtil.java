package com.checkout.test.util;

import com.checkout.test.model.Item;
import com.checkout.test.model.Offer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Util class for utility methods
 */
public class PricingUtil {

    /**
     * Method to calculate the totat cost of a quantity of an item
     *
     * If there is no offer it is simply quantity * unitCost
     *
     * If there is an offer, it applies the offer to the as many of the quantity of items as possible,
     * and then add any remainder
     *
     * E.g. there is a quantity of 5 items with a unit price of £1 and an offer of 2 for £1.50,
     * this method returns (2 * 1.50) + (1 * £1) = £4
     *
     * @param item
     * @param quantity
     * @return
     */
    public static BigDecimal calculatePrice(Item item, Integer quantity) {
        if (item.getOffer() == null) {
            return item.getUnitCost().multiply(new BigDecimal(quantity));
        }
        BigDecimal total = new BigDecimal(0.0);

        // Calculate offer count by dividing quantity by offer item count,
        // java will return the nearest whole number, ignoring remainders
        int offerCount = quantity / item.getOffer().getItemCount();
        total = total.add(item.getOffer().getOfferPrice().multiply(new BigDecimal(offerCount)));

        // Use mod operator to calculate remainder
        int remainder = quantity % item.getOffer().getItemCount();
        total = total.add(item.getUnitCost().multiply(new BigDecimal(remainder)));
        return total;
    }

    /**
     * product data stub
     *
     * @return basic products
     */
    public static Map<String, Item> basicProductData() {
        Map<String, Item> productData = new HashMap<>();
        Item item1 = new Item("a", new BigDecimal(1), null);
        Item item2 = new Item("b", new BigDecimal(2), null);
        Item item3 = new Item("c", new BigDecimal(3), new Offer(3,new BigDecimal(6)));
        Item item4 = new Item("d", new BigDecimal(4), new Offer(2,new BigDecimal(5)));
        Item item5 = new Item("e", new BigDecimal(5), new Offer(5,new BigDecimal(20)));
        productData.put(item1.getSku(), item1);
        productData.put(item2.getSku(), item2);
        productData.put(item3.getSku(), item3);
        productData.put(item4.getSku(), item4);
        productData.put(item5.getSku(), item5);
        return productData;
    }
}
