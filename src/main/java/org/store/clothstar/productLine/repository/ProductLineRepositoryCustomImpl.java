package org.store.clothstar.productLine.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.category.entity.QCategoryEntity;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.QMember;
import org.store.clothstar.member.domain.QSeller;
import org.store.clothstar.member.domain.Seller;
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
    QCategoryEntity qCategory = QCategoryEntity.categoryEntity;
    QProductEntity qProduct = QProductEntity.productEntity;
    QSeller qSeller = QSeller.seller;
    QMember qMember = QMember.member;


    public Page<ProductLineWithProductsJPAResponse> getProductLinesWithOptions(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort());

        List<Tuple> results = jpaQueryFactory
                .selectDistinct(qProductLine, qCategory, qSeller, qMember, qProduct)
                .from(qProductLine)
                .innerJoin(qProductLine.seller, qSeller).fetchJoin()
                .innerJoin(qSeller.member, qMember).fetchJoin()
                .leftJoin(qProductLine.products, qProduct).fetchJoin()
                .leftJoin(qProductLine.category, qCategory).fetchJoin()
                .where(qProductLine.deletedAt.isNull())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Map<Long, ProductLineWithProductsJPAResponse> productLineMap = new HashMap<>();
        for (Tuple tuple : results) {
            ProductLineEntity productLine = tuple.get(qProductLine);
            CategoryEntity category = tuple.get(qCategory);
            Seller seller = tuple.get(qSeller);
            Member member = tuple.get(qMember);
            ProductEntity product = tuple.get(qProduct);

            ProductLineWithProductsJPAResponse response = productLineMap.computeIfAbsent(productLine.getProductLineId(),
                    id -> new ProductLineWithProductsJPAResponse(productLine, seller, productLine.getProducts().stream().mapToLong(ProductEntity::getStock).sum()));

            if (product != null) {
                response.getProductList().add(ProductResponse.from(product));
            }
        }


        List<ProductLineWithProductsJPAResponse> content = new ArrayList<>(productLineMap.values());

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
                .groupBy(qProductLine.productLineId, qCategory, qSeller, qMember)
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