package org.store.clothstar.productLine.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "goods_option")
public class GoodsOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private List<String> optionNames;
    private int originalPrice;
    private int price;
    private int stock;
    private String deliveryType;

    @ManyToOne
    @JoinColumn(name = "product_option_id")
    private Option option;
}
