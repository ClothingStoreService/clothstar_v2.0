package org.store.clothstar.productLine.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 옵션 그룹 내의 개별 옵션을 관리한.
 * ex) 색상의 경우 “연청”, “중청”
 *
 * {
 *     "id": 1,
 *     "productOptionGroup": 1,
 *     "name": "연청",
 *     "depth": 1,
 *     "goodsOptionSno": 157877069
 * },
 * {
 *     "id": 2,
 *     "productOptionGroup": 1,
 *     "name": "중청",
 *     "depth": 1,
 *     "goodsOptionSno": 157877085
 * }
 *
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "option_group")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;
    private String name;
    private int depth;
    private Long goodsOptionSno;

    @ManyToOne
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;

    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL)
    private List<GoodsOption> goodsOptions;
}
