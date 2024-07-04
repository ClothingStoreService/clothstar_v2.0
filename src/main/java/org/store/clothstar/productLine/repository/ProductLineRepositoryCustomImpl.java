package org.store.clothstar.productLine.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.store.clothstar.member.entity.QSellerEntity;
import org.store.clothstar.member.entity.SellerEntity;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.entity.QProductEntity;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.dto.response.QProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.entity.QProductLineEntity;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductLineRepositoryCustomImpl implements ProductLineRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QProductLineEntity qProductLine = QProductLineEntity.productLineEntity;
    QProductEntity qProduct = QProductEntity.productEntity;
    QSellerEntity qSeller = QSellerEntity.sellerEntity;
    QMemberEntity qMember = QMemberEntity.memberEntity;

    @Override
    public Page<ProductLineWithProductsJPAResponse> getProductLinesWithOptions(Pageable pageable) {
        List<ProductLineWithProductsJPAResponse> content = getProductLines(pageable, null);

        JPAQuery<Long> totalCount = jpaQueryFactory
                .select(qProductLine.count())
                .from(qProductLine)
                .where(qProductLine.deletedAt.isNull());

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

    @Override
    public Optional<ProductLineWithProductsJPAResponse> findProductLineWithOptionsById(Long productLineId) {
        NumberExpression<Long> totalStockExpression = qProduct.stock.sum();

        ProductLineWithProductsJPAResponse result = jpaQueryFactory
                .select(new QProductLineWithProductsJPAResponse(
                        qProductLine,
                        qSeller,
                        totalStockExpression
                ))
                .from(qProductLine)
                .innerJoin(qProductLine.seller, qSeller)
                .leftJoin(qProductLine.products, qProduct)
                .where(qProductLine.productLineId.eq(productLineId)
                        .and(qProductLine.deletedAt.isNull()))
                .groupBy(qProductLine.productLineId, qSeller)
                .fetchOne();

        if (result != null) {
            List<ProductEntity> products = jpaQueryFactory
                    .selectFrom(qProduct)
                    .where(qProduct.productLine.productLineId.eq(productLineId))
                    .fetch();

            result.setProductList(products.stream()
                    .map(ProductResponse::from)
                    .collect(Collectors.toList()));
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Page<ProductLineWithProductsJPAResponse> findAllOffsetPaging(Pageable pageable, String keyword) {
        List<ProductLineWithProductsJPAResponse> content = getProductLines(pageable, keyword);

        JPAQuery<Long> totalCount = jpaQueryFactory
                .select(qProductLine.count())
                .from(qProductLine)
                .where(qProductLine.deletedAt.isNull().and(getSearchCondition(keyword)));

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

    @Override
    public Slice<ProductLineWithProductsJPAResponse> findAllSlicePaging(Pageable pageable, String keyword) {
        List<ProductLineWithProductsJPAResponse> content = getProductLines(pageable, keyword);

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(content.size() - 1);
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private List<ProductLineWithProductsJPAResponse> getProductLines(Pageable pageable, String keyword) {
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort());
        BooleanExpression searchCondition = getSearchCondition(keyword);

        JPAQuery<ProductLineWithProductsJPAResponse> query = jpaQueryFactory
                .select(new QProductLineWithProductsJPAResponse(
                        qProductLine,
                        qSeller,
                        qProduct.stock.sum()
                ))
                .from(qProductLine)
                .innerJoin(qProductLine.seller, qSeller)
                .leftJoin(qProductLine.products, qProduct)
                .where(qProductLine.deletedAt.isNull().and(searchCondition))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .groupBy(qProductLine.productLineId, qSeller);

        List<ProductLineWithProductsJPAResponse> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        content.forEach(response -> {
            List<ProductEntity> products = jpaQueryFactory
                    .selectFrom(qProduct)
                    .where(qProduct.productLine.productLineId.eq(response.getProductLineId()))
                    .fetch();
            response.setProductList(products.stream()
                    .map(ProductResponse::from)
                    .collect(Collectors.toList()));
        });

        return content;
    }

    private BooleanExpression getSearchCondition(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return qProductLine.isNotNull();  // 조건이 없을 경우 항상 true를 반환
        }
        return qProductLine.name.containsIgnoreCase(keyword)
                .or(qProductLine.content.containsIgnoreCase(keyword));
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