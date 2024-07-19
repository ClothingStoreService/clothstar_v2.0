package org.store.clothstar.productLine.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.productLine.domain.ItemAttribute;

@Getter
@Builder
public class ItemAttributeResponse {

    private Long optionId;
    private String name;
    private String value;
    private Long valueId;

    public ItemAttributeDTO(ItemAttribute itemAttribute) {
        this.optionId = itemAttribute.getOptionId();
        this.name = itemAttribute.getName();
        this.value = itemAttribute.getValue();
        this.valueId = itemAttribute.getValueId();
    }

    public static ItemAttributeResponse from(ItemAttribute itemAttribute) {
        return ItemAttributeResponse.builder()
                .optionId(itemAttribute.getOptionId())
                .name(itemAttribute.getName())
                .value(itemAttribute.getValue())
                .valueId(itemAttribute.getValueId())
                .build();
    }
}
