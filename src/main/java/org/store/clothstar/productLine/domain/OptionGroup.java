package org.store.clothstar.productLine.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 제품의 옵션 그룹을 관리한다.
 * ex) 색상, 기장, 사이즈
 *
 * {
 *     "id": 1,
 *     "productLine": 1,
 *     "name": "색상",
 *     "depth": 1
 * },
 * {
 *     "id": 2,
 *     "productLine": 1,
 *     "name": "기장",
 *     "depth": 2
 * }
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "option_group")
public class OptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionGroupId;
    private String name;
    private int depth;

    @ManyToOne
    @JoinColumn(name = "product_line_id")
    private ProductLine productLine;

    @OneToMany(mappedBy = "OptionGroup", cascade = CascadeType.ALL)
    private List<Option> options;
}
