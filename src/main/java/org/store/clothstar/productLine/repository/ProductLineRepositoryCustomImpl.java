package org.store.clothstar.productLine.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.domain.QProductLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductLineRepositoryCustomImpl implements ProductLineRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QProductLine qProductLine = QProductLine.productLine;

    @Override
    public Page<ProductLine> getProductLinesWithOptions(Pageable pageable) {
        List<ProductLine> content = getProductLines(pageable, null);

        JPAQuery<Long> totalCount = jpaQueryFactory
                .select(qProductLine.count())
                .from(qProductLine)
                .where(qProductLine.deletedAt.isNull());

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

    @Override
    public Optional<ProductLine> findProductLineWithOptionsById(Long productLineId) {

        ProductLine result = jpaQueryFactory
                .select(qProductLine)
                .from(qProductLine)
                .where(qProductLine.productLineId.eq(productLineId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<ProductLine> findAllOffsetPaging(Pageable pageable, String keyword) {
        List<ProductLine> content = getProductLines(pageable, keyword);

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(content.size() - 1);
            hasNext = true;
        }

        JPAQuery<Long> totalCount = jpaQueryFactory
                .select(qProductLine.countDistinct())
                .from(qProductLine)
                .where(qProductLine.deletedAt.isNull()
                        .and(getSearchCondition(keyword)));

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

    @Override
    public Slice<ProductLine> findAllSlicePaging(Pageable pageable, String keyword) {
        List<ProductLine> content = getProductLines(pageable, keyword);

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(content.size() - 1);
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Page<ProductLine> findEntitiesByCategoryWithOffsetPaging(Long categoryId, Pageable pageable, String keyword) {
        List<ProductLine> content = getProductLineEntitiesByCategory(categoryId, pageable, keyword);

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
    public Slice<ProductLine> findEntitiesByCategoryWithSlicePaging(Long categoryId, Pageable pageable, String keyword) {
        List<ProductLine> content = getProductLineEntitiesByCategory(categoryId, pageable, keyword);

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(content.size() - 1);
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private List<ProductLine> getProductLines(Pageable pageable, String keyword) {
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort());

        // 카테고리별로 ProductLine 엔티티를 가져옴
        return jpaQueryFactory
                .selectDistinct(qProductLine)
                .from(qProductLine)
                .where(qProductLine.deletedAt.isNull()
                        .and(getSearchCondition(keyword)))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private List<ProductLine> getProductLineEntitiesByCategory(Long categoryId, Pageable pageable, String keyword) {
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