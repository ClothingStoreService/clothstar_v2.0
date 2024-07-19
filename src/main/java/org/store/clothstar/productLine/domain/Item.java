package org.store.clothstar.productLine.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.productLine.domain.type.DeliveryType;
<<<<<<< Updated upstream
=======
import org.store.clothstar.productLine.domain.type.DisplayStatus;
import org.store.clothstar.productLine.domain.type.SaleStatus;
>>>>>>> Stashed changes

import java.util.List;

/**
 * {
 *   "id": 1,
 *   "name": "중청/롱/S",
 *   "price": 29900,
 *   "finalPrice": 19900,
 *   "itemCode": null,
 *   "deliveryType": "GENERAL",
 *   "remainStock": 4,
 *   "productLine": 1,
 *   "itemAttributes": [
 *     { "optionId": 1, "name": "색상", "value": "중청", "valueId": 1 },
 *     { "optionId": 2, "name": "기장", "value": "롱", "valueId": 6 },
 *     { "optionId": 3, "name": "사이즈", "value": "S", "valueId": 8 }
 *   ]
 * }
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;
    private Integer stock;
    private SaleStatus saleStatus;
<<<<<<< Updated upstream
    private displayStatus displayStatus;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;


=======
    private DisplayStatus displayStatus;
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

>>>>>>> Stashed changes
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_line_id")
    private ProductLine productLine;

    @ElementCollection
    @CollectionTable(name = "item_attributes", joinColumns = @JoinColumn(name = "item_id"))
    private List<ItemAttribute> itemAttributes;
}
