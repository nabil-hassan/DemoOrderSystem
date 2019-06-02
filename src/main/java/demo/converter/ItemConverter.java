package demo.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import demo.entity.dto.BasketItem;
import demo.entity.persistent.Basket;
import demo.entity.persistent.Item;

public class ItemConverter {

    public Map<Item, Integer> toItemCountMap(List<Item> items) {
        return items.stream().collect(Collectors.groupingBy(i -> i, Collectors.summingInt(x -> 1)));
    }

    public List<BasketItem> toBasketItems(List<Item> itemList) {
        List<BasketItem> basketItems = new ArrayList<>();
        Map<Item, Integer> itemMap = toItemCountMap(itemList);
        itemMap.forEach((item,count) -> {
            //ensure 2 dp - should really use BigDecimal
            double d = count * item.getCost();
            double total = Math.round(d * 100.00) / 100.00;
            basketItems.add(new BasketItem(item, count, total));
        });
        return basketItems;
    }

}
