package org.store.clothstar.productLine.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.store.clothstar.member.domain.QMember;
import org.store.clothstar.member.domain.QSeller;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.product.entity.QProductEntity;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.dto.response.QProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.entity.QProductLineEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductLineRepositoryCustomImpl implements ProductLineRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QProductLineEntity qProductLine = QProductLineEntity.productLineEntity;
    QProductEntity qProduct = QProductEntity.productEntity;
    QSeller qSeller = QSeller.seller;

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
        // 1. ProductLine과 관련된 Seller와 총 재고량을 가져옴
        ProductLineWithProductsJPAResponse result = jpaQueryFactory
                .select(new QProductLineWithProductsJPAResponse(
                        qProductLine,
                        qSeller,
                        qProduct.stock.sum()
                ))
                .from(qProductLine)
                .innerJoin(qProductLine.seller, qSeller).fetchJoin()
                .leftJoin(qProductLine.products, qProduct)
                .where(qProductLine.productLineId.eq(productLineId)
                        .and(qProductLine.deletedAt.isNull()))
                .groupBy(qProductLine.productLineId, qSeller)
                .fetchOne();

        // 2. ProductLine에 속한 Product들을 가져옴
        if (result != null) {
            List<ProductResponse> productResponses = jpaQueryFactory
                    .selectFrom(qProduct)
                    .where(qProduct.productLine.productLineId.eq(productLineId))
                    .fetch()
                    .stream()
                    .map(ProductResponse::from)
                    .collect(Collectors.toList());
            result.setProductList(productResponses);
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

    @Override
    public Page<ProductLineEntity> findEntitiesByCategoryWithOffsetPaging(Long categoryId, Pageable pageable, String keyword) {
        List<ProductLineEntity> content = getProductLineEntitiesByCategory(categoryId, pageable, keyword);

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(content.size() - 1);
            hasNext = true;
        }

        JPAQuery<Long> totalCount = jpaQueryFactory
                .select(qProductLine.countDistinct())
                .from(qProductLine)
                .where(qProductLine.category.categoryId.eq(categoryId)
                        .and(qProductLine.deletedAt.isNull())
                        .and(getSearchCondition(keyword)));

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

    @Override
    public Slice<ProductLineEntity> findEntitiesByCategoryWithSlicePaging(Long categoryId, Pageable pageable, String keyword) {
        List<ProductLineEntity> content = getProductLineEntitiesByCategory(categoryId, pageable, keyword);

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

        // 1. 모든 ProductLine을 가져옴
        List<ProductLineWithProductsJPAResponse> productLines = jpaQueryFactory
                .select(new QProductLineWithProductsJPAResponse(
                        qProductLine,
                        qSeller,
                        qProduct.stock.sum()
                ))
                .from(qProductLine)
                .innerJoin(qProductLine.seller, qSeller).fetchJoin()
                .leftJoin(qProductLine.products, qProduct)
                .where(qProductLine.deletedAt.isNull().and(searchCondition))
                .groupBy(qProductLine.productLineId, qSeller)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        // 2. 모든 Product를 가져옴
        Map<Long, List<ProductResponse>> productMap = jpaQueryFactory
                .selectFrom(qProduct)
                .where(qProduct.productLine.productLineId.in(
                        productLines.stream()
                                .map(ProductLineWithProductsJPAResponse::getProductLineId)
                                .collect(Collectors.toList())
                ))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        p -> p.getProductLine().getProductLineId(),
                        Collectors.mapping(ProductResponse::from, Collectors.toList())
                ));

        // 3. ProductLine에 Product를 매핑
        productLines.forEach(productLine ->
                productLine.setProductList(productMap.get(productLine.getProductLineId())));

        return productLines;
    }

    private List<ProductLineEntity> getProductLineEntitiesByCategory(Long categoryId, Pageable pageable, String keyword) {
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort());

        // 카테고리별로 ProductLine 엔티티를 가져옴
        return jpaQueryFactory
                .selectDistinct(qProductLine)
                .from(qProductLine)
                .where(qProductLine.category.categoryId.eq(categoryId)
                        .and(qProductLine.deletedAt.isNull())
                        .and(getSearchCondition(keyword)))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
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