package org.store.clothstar.productLine.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * org.store.clothstar.productLine.dto.response.QProductLineWithProductsJPAResponse is a Querydsl Projection type for ProductLineWithProductsJPAResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProductLineWithProductsJPAResponse extends ConstructorExpression<ProductLineWithProductsJPAResponse> {

    private static final long serialVersionUID = -438244439L;

    public QProductLineWithProductsJPAResponse(com.querydsl.core.types.Expression<? extends org.store.clothstar.productLine.entity.ProductLineEntity> productLine, com.querydsl.core.types.Expression<? extends org.store.clothstar.category.domain.Category> category, com.querydsl.core.types.Expression<? extends org.store.clothstar.member.entity.SellerEntity> seller, com.querydsl.core.types.Expression<? extends org.store.clothstar.member.entity.MemberEntity> member, com.querydsl.core.types.Expression<Long> totalStock) {
        super(ProductLineWithProductsJPAResponse.class, new Class<?>[]{org.store.clothstar.productLine.entity.ProductLineEntity.class, org.store.clothstar.category.domain.Category.class, org.store.clothstar.member.entity.SellerEntity.class, org.store.clothstar.member.entity.MemberEntity.class, long.class}, productLine, category, seller, member, totalStock);
    }

}

