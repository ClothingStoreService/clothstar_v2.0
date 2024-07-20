package org.store.clothstar.productLine.dto.request;

public class ProductOptionRequest {

    private String name;
    private int order;
    private boolean required;
    private String optionType;
    private List<OptionValueRequest> optionValues;
}
