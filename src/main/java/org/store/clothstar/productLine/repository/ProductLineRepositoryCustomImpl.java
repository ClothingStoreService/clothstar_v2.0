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
import org.store.clothstar.category.domain.Category;
import org.store.clothstar.category.domain.QCategory;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.entity.QMemberEntity;
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
    QCategory qCategory = QCategory.category;
    QProductEntity qProduct = QProductEntity.productEntity;
    QSellerEntity qSeller = QSellerEntity.sellerEntity;
    QMemberEntity qMember = QMemberEntity.memberEntity;

/*
    @Override
    public Page<ProductLineWithProductsJPAResponse> getProductLinesWithOptions(Pageable pageable) {

        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort());

        List<ProductLineWithProductsJPAResponse> content = jpaQueryFactory
                .select(new QProductLineWithProductsJPAResponse(
                        qProductLine, qCategory, qSeller, qMember, qProduct.stock.sum()))
                .from(qProductLine)
                .innerJoin(qProductLine.seller, qSeller)
                .innerJoin(qSeller.member, qMember)
                .leftJoin(qProductLine.products, qProduct).fetchJoin()
//                .innerJoin(qProduct).on()
                .where(qProductLine.deletedAt.isNull())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(qProductLine.productLineId)
                .fetch();

        JPAQuery<Long> totalCount = jpaQueryFactory
                .select(qProductLine.count())
                .from(qProductLine)
                .where(qProductLine.deletedAt.isNull());

        return PageableExecutionUtils.getPage(content, pageable, totalCount::fetchOne);
    }

 */

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

        Map<Long, ProductLineWithProductsJPAResponse> productLineMap = new HashMap<>();for (Tuple tuple : results) {
            ProductLineEntity productLine = tuple.get(qProductLine);
            Category category = tuple.get(qCategory);
            SellerEntity seller = tuple.get(qSeller);
            MemberEntity member = tuple.get(qMember);
            ProductEntity product = tuple.get(qProduct);

            ProductLineWithProductsJPAResponse response = productLineMap.computeIfAbsent(productLine.getProductLineId(),
                    id -> new ProductLineWithProductsJPAResponse(productLine, category, seller, member, productLine.getProducts().stream().mapToLong(ProductEntity::getStock).sum()));

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

        return Optional.ofNullable(jpaQueryFactory
                .select(new QProductLineWithProductsJPAResponse(qProductLine, qCategory, qSeller, qMember, totalStockExpression))
                .from(qProductLine)
                .innerJoin(qProductLine.seller)
                .innerJoin(qSeller.member)
                .innerJoin(qProductLine.products, qProduct)
                .where(qProductLine.productLineId.eq(productLineId)
                        .and(qProductLine.deletedAt.isNull()))
                .fetchOne());
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
