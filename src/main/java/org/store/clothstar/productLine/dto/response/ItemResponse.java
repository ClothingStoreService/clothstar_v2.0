package org.store.clothstar.productLine.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.productLine.domain.Item;
import org.store.clothstar.productLine.domain.type.DeliveryType;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ItemResponse {

    private Long id;
    private String name;
    private int price;
    private int finalPrice;
    private String itemCode;
    private DeliveryType deliveryType;
    private Integer remainStock;
    private List<ItemAttributeResponse> itemAttributes;

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.deliveryType = item.getDeliveryType();
        this.itemAttributes = item.getItemAttributes().stream()
                .map(ItemAttributeResponse::from)
                .collect(Collectors.toList());

        // remainStock이 5개 이하일 경우에만 값을 설정
        if (item.getStock() != null && item.getStock() <= 5) {
            this.remainStock = item.getStock();
        } else {
            this.remainStock = null;
        }
    }
}
