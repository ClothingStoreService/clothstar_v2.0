package org.store.clothstar.productLine.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * {
 *   "id": 1,
 *   "name": "색상",
 *   "order": 0,
 *   "required": true,
 *   "optionType": "BASIC",
 *   "catalogProduct": 1,
 *   "optionValues": [
 *     { "id": 1, "code": "#5C88C9", "value": "중청" },
 *     { "id": 2, "code": "#778899", "value": "애쉬블루" },
 *     { "id": 3, "code": null, "value": "연청" },
 *     { "id": 4, "code": null, "value": "(썸머)연청" },
 *     { "id": 5, "code": null, "value": "(썸머)중청" }
 *   ]
 * }
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int order;
    private boolean required;
    private String optionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_line_id")
    private ProductLine productLine;

    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL)
    private List<OptionValue> optionValues;

}
