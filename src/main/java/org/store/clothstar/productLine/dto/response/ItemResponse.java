package org.store.clothstar.productLine.dto.response;

import lombok.Getter;
import org.store.clothstar.productLine.domain.Item;
import org.store.clothstar.productLine.domain.type.DeliveryType;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ItemResponse {

    private Long id;
    private String name;
    private int price;
    private int finalPrice;
    private String itemCode;
    private DeliveryType deliveryType;
    private Integer remainStock;
    private List<ItemAttributeResponse> itemAttributes;

    public ItemDTO(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.finalPrice = item.getFinalPrice();
        this.itemCode = item.getItemCode();
        this.deliveryType = item.getDeliveryType();
        this.itemAttributes = item.getItemAttributes().stream()
                .map(ItemAttributeResponse::from)
                .collect(Collectors.toList());

        // remainStock이 5개 이하일 경우에만 값을 설정
        if (item.getRemainStock() != null && item.getRemainStock() <= 5) {
            this.remainStock = item.getRemainStock();
        } else {
            this.remainStock = null;
        }
    }
}
