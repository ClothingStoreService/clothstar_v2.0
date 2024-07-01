package org.store.clothstar.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSellerEntity is a Querydsl query type for SellerEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSellerEntity extends EntityPathBase<SellerEntity> {

    private static final long serialVersionUID = 251707698L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSellerEntity sellerEntity = new QSellerEntity("sellerEntity");

    public final org.store.clothstar.common.entity.QBaseEntity _super = new org.store.clothstar.common.entity.QBaseEntity(this);

    public final StringPath bizNo = createString("bizNo");

    public final StringPath brandName = createString("brandName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final QMemberEntity member;

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Integer> totalSellPrice = createNumber("totalSellPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSellerEntity(String variable) {
        this(SellerEntity.class, forVariable(variable), INITS);
    }

    public QSellerEntity(Path<? extends SellerEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSellerEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSellerEntity(PathMetadata metadata, PathInits inits) {
        this(SellerEntity.class, metadata, inits);
    }

    public QSellerEntity(Class<? extends SellerEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMemberEntity(forProperty("member")) : null;
    }

}

