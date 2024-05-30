package org.store.clothstar.product.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.store.clothstar.category.entity.QCategory;
import org.store.clothstar.member.entity.QMemberEntity;
import org.store.clothstar.member.entity.QSellerEntity;
import org.store.clothstar.product.entity.QProduct;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.dto.response.QProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.entity.QProductLine;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductLineRepositoryCustomImpl implements ProductLineRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QProductLine qProductLine = QProductLine.productLine;
    QCategory qCategory = QCategory.category;
    QProduct qProduct = QProduct.product;
    QSellerEntity qSeller = QSellerEntity.sellerEntity;
    QMemberEntity qMember = QMemberEntity.memberEntity;


    @Override
    public Page<ProductLineWithProductsJPAResponse> findProductLineWithOptions(Pageable pageable) {

        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort());

        List<ProductLineWithProductsJPAResponse> content = jpaQueryFactory
                .select(new QProductLineWithProductsJPAResponse(qProductLine, qCategory, qSeller, qMember))
                .from(qProductLine)
                .leftJoin(qProductLine.seller)
                .leftJoin(qSeller.member)
                .leftJoin(qProductLine.products)
                .where(qProductLine.deletedAt.isNull())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> totalCount = jpaQueryFactory
                .select(qProductLine.count())
                .from(qProductLine)
                .where(qProductLine.deletedAt.isNull());

//        long totalCount2 = jpaQueryFactory
//                .selectFrom(qProductLine)
//                .where(qProductLine.deletedAt.isNull())
//                .fetchCount();

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sort != null) {
            for (Sort.Order order : sort) {
                switch (order.getProperty()) {
                    case "saleCount" ->
                            orderSpecifiers.add(order.isAscending() ? qProductLine.saleCount.asc() : qProductLine.saleCount.desc());
                    case "createdAt" ->
                            orderSpecifiers.add(order.isAscending() ? qProductLine.createdAt.asc() : qProductLine.createdAt.desc());
                    case "price" ->
                            orderSpecifiers.add(order.isAscending() ? qProductLine.price.asc() : qProductLine.price.desc());
                    default -> {
                    }
                }
            }
        }

        return orderSpecifiers;
    }
}
