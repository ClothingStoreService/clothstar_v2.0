package org.store.clothstar.productLine.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductLineEntity is a Querydsl query type for ProductLineEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductLineEntity extends EntityPathBase<ProductLineEntity> {

    private static final long serialVersionUID = 755215007L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductLineEntity productLineEntity = new QProductLineEntity("productLineEntity");

    public final org.store.clothstar.common.entity.QBaseTimeEntity _super = new org.store.clothstar.common.entity.QBaseTimeEntity(this);

    public final org.store.clothstar.category.domain.QCategory category;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Long> productLineId = createNumber("productLineId", Long.class);

    public final ListPath<org.store.clothstar.product.entity.ProductEntity, org.store.clothstar.product.entity.QProductEntity> products = this.<org.store.clothstar.product.entity.ProductEntity, org.store.clothstar.product.entity.QProductEntity>createList("products", org.store.clothstar.product.entity.ProductEntity.class, org.store.clothstar.product.entity.QProductEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> saleCount = createNumber("saleCount", Long.class);

    public final org.store.clothstar.member.entity.QSellerEntity seller;

    public final EnumPath<org.store.clothstar.productLine.domain.type.ProductLineStatus> status = createEnum("status", org.store.clothstar.productLine.domain.type.ProductLineStatus.class);

    public QProductLineEntity(String variable) {
        this(ProductLineEntity.class, forVariable(variable), INITS);
    }

    public QProductLineEntity(Path<? extends ProductLineEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductLineEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductLineEntity(PathMetadata metadata, PathInits inits) {
        this(ProductLineEntity.class, metadata, inits);
    }

    public QProductLineEntity(Class<? extends ProductLineEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new org.store.clothstar.category.domain.QCategory(forProperty("category")) : null;
        this.seller = inits.isInitialized("seller") ? new org.store.clothstar.member.entity.QSellerEntity(forProperty("seller"), inits.get("seller")) : null;
    }

}

