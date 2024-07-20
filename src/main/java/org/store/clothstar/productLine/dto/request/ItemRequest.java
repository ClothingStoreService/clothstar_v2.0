package org.store.clothstar.productLine.dto.request;

import java.util.List;

public class ItemRequest {

    private String name;
    private int price;
    private int stock;
    private String saleStatus;
    private String displayStatus;
    private List<ItemAttributeRequest> itemAttributes;
}
