package org.store.clothstar.order.dto.reponse;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * org.store.clothstar.order.dto.reponse.QOrderResponse is a Querydsl Projection type for OrderResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QOrderResponse extends ConstructorExpression<OrderResponse> {

    private static final long serialVersionUID = -1258133913L;

    public QOrderResponse(com.querydsl.core.types.Expression<? extends org.store.clothstar.order.entity.OrderEntity> orderEntity, com.querydsl.core.types.Expression<? extends org.store.clothstar.orderDetail.entity.OrderDetailEntity> orderDetailEntity, com.querydsl.core.types.Expression<? extends org.store.clothstar.member.entity.MemberEntity> memberEntity, com.querydsl.core.types.Expression<? extends org.store.clothstar.member.entity.AddressEntity> addressEntity, com.querydsl.core.types.Expression<? extends org.store.clothstar.productLine.entity.ProductLineEntity> productLineEntity) {
        super(OrderResponse.class, new Class<?>[]{org.store.clothstar.order.entity.OrderEntity.class, org.store.clothstar.orderDetail.entity.OrderDetailEntity.class, org.store.clothstar.member.entity.MemberEntity.class, org.store.clothstar.member.entity.AddressEntity.class, org.store.clothstar.productLine.entity.ProductLineEntity.class}, orderEntity, orderDetailEntity, memberEntity, addressEntity, productLineEntity);
    }

}

