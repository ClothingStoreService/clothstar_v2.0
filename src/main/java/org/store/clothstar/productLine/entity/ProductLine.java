package org.store.clothstar.productLine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.category.entity.Category;
import org.store.clothstar.common.entity.BaseTimeEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductLine extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productLineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String name;

    private String content;

    private int price;

    private Long totalStock;

    @Enumerated(EnumType.STRING)
    private ProductLineStatus status;

    private Long saleCount;
}
