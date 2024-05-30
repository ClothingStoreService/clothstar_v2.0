package org.store.clothstar.product.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.store.clothstar.category.entity.QCategory;
import org.store.clothstar.member.entity.QMemberEntity;
import org.store.clothstar.product.entity.QProduct;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;
import org.store.clothstar.productLine.entity.QProductLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductLineRepositoryCustomImpl implements ProductLineRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QProductLine productLine = QProductLine.productLine;
    QCategory category = QCategory.category;
    QProduct product = QProduct.product;
    QSeller seller = QSeller.seller;
    QMemberEntity member = QMemberEntity.memberEntity;


    @Override
    public Optional<ProductLineWithProductsResponse> findProductLineWithOptions(Long productLineId, Sort sort) {

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : sort) {
            switch (order.getProperty()) {
                case "saleCount":
                    orderSpecifiers.add(order.isAscending() ? productLine.saleCount.asc() : productLine.saleCount.desc());
                    break;
                case "createdAt":
                    orderSpecifiers.add(order.isAscending() ? productLine.createdAt.asc() : productLine.createdAt.desc());
                    break;
                case "price":
                    orderSpecifiers.add(order.isAscending() ? productLine.price.asc() : productLine.price.desc());
                    break;
                default:
                    break;
            }
        }

        return Optional.empty();
    }
}
