package org.store.clothstar.productLine.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/***
 * {
 *   "id": 1,
 *   "code": "#5C88C9",
 *   "value": "중청",
 *   "productOption": 1
 * }
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OptionValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @ManyToOne
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;
}
