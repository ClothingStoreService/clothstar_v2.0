package org.store.clothstar.order.repository.orderSeller;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.store.clothstar.member.entity.QAddressEntity;
import org.store.clothstar.member.entity.QMemberEntity;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.reponse.QOrderResponse;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.entity.QOrderEntity;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.dto.OrderDetailDTO;
import org.store.clothstar.orderDetail.dto.QOrderDetailDTO;
import org.store.clothstar.orderDetail.entity.QOrderDetailEntity;
import org.store.clothstar.product.entity.QProductEntity;
import org.store.clothstar.productLine.entity.QProductLineEntity;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderEntityRepositoryCustomImpl implements OrderEntityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QOrderEntity qOrderEntity = QOrderEntity.orderEntity;
    QOrderDetailEntity qOrderDetailEntity = QOrderDetailEntity.orderDetailEntity;
    QMemberEntity qMemberEntity = QMemberEntity.memberEntity;
    QAddressEntity qAddressEntity = QAddressEntity.addressEntity;
    QProductEntity qProductEntity = QProductEntity.productEntity;
    QProductLineEntity qProductLineEntity = QProductLineEntity.productLineEntity;

    @Override
    public List<OrderResponse> findWaitingOrders() {
//        return jpaQueryFactory.select(qOrderEntity)
//                .from(qOrderEntity)
//                .where(qOrderEntity.status.eq(Status.WAITING))
//                .fetch();
        List<OrderResponse> results = jpaQueryFactory
                .select(new QOrderResponse(
                        qOrderEntity,
                        qOrderDetailEntity,
                        qMemberEntity,
                        qAddressEntity,
                        qProductLineEntity))
                .from(qOrderEntity)
                .innerJoin(qOrderEntity.member, qMemberEntity)
                .innerJoin(qOrderEntity.address, qAddressEntity)
                .innerJoin(qOrderEntity.orderDetails, qOrderDetailEntity)
                .innerJoin(qOrderDetailEntity.product, qProductEntity)
                .innerJoin(qProductEntity.productLine, qProductLineEntity)
                .where(qOrderEntity.status.eq(Status.WAITING))
                .groupBy(qOrderEntity.orderId)
                .fetch();

        if (results != null) {
            results.forEach(result -> {
                List<OrderDetailDTO> orderDetailList = jpaQueryFactory
                        .select(new QOrderDetailDTO(
                                qOrderDetailEntity
                        ))
                        .from(qOrderDetailEntity)
                        .where(qOrderDetailEntity.order.orderId.eq(result.getOrderId()))
                        .fetch();

                result.setterOrderDetailList(orderDetailList);
            });
        }
        return results;
    }

    @Override
    public OrderResponse findOrderWithDetails(Long orderId){

                OrderResponse result = jpaQueryFactory
                .select(new QOrderResponse(
                        qOrderEntity,
                        qOrderDetailEntity,
                        qMemberEntity,
                        qAddressEntity,
                        qProductLineEntity))
                .from(qOrderEntity)
                .innerJoin(qOrderEntity.member, qMemberEntity)
                .innerJoin(qOrderEntity.address, qAddressEntity)
                .innerJoin(qOrderEntity.orderDetails, qOrderDetailEntity)
                .innerJoin(qOrderDetailEntity.product, qProductEntity)
                .innerJoin(qProductEntity.productLine, qProductLineEntity)
                .where(qOrderEntity.orderId.eq(orderId))
                .groupBy(qOrderEntity.orderId)
                .fetchOne();

        if (result != null) {
            List<OrderDetailDTO> orderDetailList = jpaQueryFactory
                    .select(new QOrderDetailDTO(
                            qOrderDetailEntity
                    ))
                    .from(qOrderDetailEntity)
                    .where(qOrderDetailEntity.order.orderId.eq(orderId))
                    .fetch();

            result.setterOrderDetailList(orderDetailList);
        }
        return result;
    }
}
