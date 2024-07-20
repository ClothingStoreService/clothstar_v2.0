package org.store.clothstar.productLine.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ItemAttribute {

    private Long optionId;
    private String name;
    private String value;
    private Long valueId;
}
