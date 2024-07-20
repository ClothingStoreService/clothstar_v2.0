package org.store.clothstar.productLine.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.store.clothstar.productLine.domain.type.ImageType;

@Embeddable
public class ProductImage {

    private String imgUrl;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;
}
