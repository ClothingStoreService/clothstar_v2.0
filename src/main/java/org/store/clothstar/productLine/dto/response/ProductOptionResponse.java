package org.store.clothstar.productLine.dto.response;

import java.util.List;

public class ProductOptionResponse {

    private Long id;
    private String name;
    private int order;  // 옵션 순서
    private boolean required;
    private String optionType;
    private List<OptionValueResponse> optionValues;
}
