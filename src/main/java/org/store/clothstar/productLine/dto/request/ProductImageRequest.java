package org.store.clothstar.productLine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.productLine.domain.type.ImageType;

@Getter
@AllArgsConstructor
@Builder
public class ProductImageRequest {

    private String imgUrl;
    private ImageType imageType;
}
