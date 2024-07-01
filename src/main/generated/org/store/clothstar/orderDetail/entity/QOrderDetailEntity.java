package org.store.clothstar.orderDetail.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderDetailEntity is a Querydsl query type for OrderDetailEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderDetailEntity extends EntityPathBase<OrderDetailEntity> {

    private static final long serialVersionUID = 2101270815L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderDetailEntity orderDetailEntity = new QOrderDetailEntity("orderDetailEntity");

    public final StringPath brandName = createString("brandName");

    public final NumberPath<Integer> fixedPrice = createNumber("fixedPrice", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> oneKindTotalPrice = createNumber("oneKindTotalPrice", Integer.class);

    public final StringPath optionName = createString("optionName");

    public final org.store.clothstar.order.entity.QOrderEntity order;

    public final NumberPath<Long> orderDetailId = createNumber("orderDetailId", Long.class);

    public final org.store.clothstar.product.entity.QProductEntity product;

    public final org.store.clothstar.productLine.entity.QProductLineEntity productLine;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final NumberPath<Long> stock = createNumber("stock", Long.class);

    public QOrderDetailEntity(String variable) {
        this(OrderDetailEntity.class, forVariable(variable), INITS);
    }

    public QOrderDetailEntity(Path<? extends OrderDetailEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderDetailEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderDetailEntity(PathMetadata metadata, PathInits inits) {
        this(OrderDetailEntity.class, metadata, inits);
    }

    public QOrderDetailEntity(Class<? extends OrderDetailEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new org.store.clothstar.order.entity.QOrderEntity(forProperty("order"), inits.get("order")) : null;
        this.product = inits.isInitialized("product") ? new org.store.clothstar.product.entity.QProductEntity(forProperty("product"), inits.get("product")) : null;
        this.productLine = inits.isInitialized("productLine") ? new org.store.clothstar.productLine.entity.QProductLineEntity(forProperty("productLine"), inits.get("productLine")) : null;
    }

}

