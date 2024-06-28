package org.store.clothstar.order.repository.orderSeller;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.entity.QOrderEntity;
import org.store.clothstar.order.type.Status;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderEntityRepositoryCustomImpl implements OrderEntityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderEntity> findWaitingOrders() {
        QOrderEntity qOrderEntity = QOrderEntity.orderEntity;

        return jpaQueryFactory.select(qOrderEntity)
                .from(qOrderEntity)
                .where(qOrderEntity.status.eq(Status.WAITING))
                .fetch();
    }
}
