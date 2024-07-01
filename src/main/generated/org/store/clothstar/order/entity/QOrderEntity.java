package org.store.clothstar.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderEntity is a Querydsl query type for OrderEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderEntity extends EntityPathBase<OrderEntity> {

    private static final long serialVersionUID = 880822783L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderEntity orderEntity = new QOrderEntity("orderEntity");

    public final org.store.clothstar.member.entity.QAddressEntity address;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final org.store.clothstar.member.entity.QMemberEntity member;

    public final ListPath<org.store.clothstar.orderDetail.entity.OrderDetailEntity, org.store.clothstar.orderDetail.entity.QOrderDetailEntity> orderDetails = this.<org.store.clothstar.orderDetail.entity.OrderDetailEntity, org.store.clothstar.orderDetail.entity.QOrderDetailEntity>createList("orderDetails", org.store.clothstar.orderDetail.entity.OrderDetailEntity.class, org.store.clothstar.orderDetail.entity.QOrderDetailEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final EnumPath<org.store.clothstar.order.type.PaymentMethod> paymentMethod = createEnum("paymentMethod", org.store.clothstar.order.type.PaymentMethod.class);

    public final EnumPath<org.store.clothstar.order.type.Status> status = createEnum("status", org.store.clothstar.order.type.Status.class);

    public final NumberPath<Integer> totalPaymentPrice = createNumber("totalPaymentPrice", Integer.class);

    public final NumberPath<Integer> totalProductsPrice = createNumber("totalProductsPrice", Integer.class);

    public final NumberPath<Integer> totalShippingPrice = createNumber("totalShippingPrice", Integer.class);

    public QOrderEntity(String variable) {
        this(OrderEntity.class, forVariable(variable), INITS);
    }

    public QOrderEntity(Path<? extends OrderEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderEntity(PathMetadata metadata, PathInits inits) {
        this(OrderEntity.class, metadata, inits);
    }

    public QOrderEntity(Class<? extends OrderEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new org.store.clothstar.member.entity.QAddressEntity(forProperty("address"), inits.get("address")) : null;
        this.member = inits.isInitialized("member") ? new org.store.clothstar.member.entity.QMemberEntity(forProperty("member")) : null;
    }

}

