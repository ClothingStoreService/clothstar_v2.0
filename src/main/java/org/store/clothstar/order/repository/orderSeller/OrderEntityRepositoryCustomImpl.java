package org.store.clothstar.order.repository.orderSeller;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.store.clothstar.member.domain.QAddress;
import org.store.clothstar.member.domain.QMember;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.reponse.QOrderResponse;
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
    QMember qMember = QMember.member;
    QAddress qAddress = QAddress.address;
    QProductEntity qProductEntity = QProductEntity.productEntity;
    QProductLineEntity qProductLineEntity = QProductLineEntity.productLineEntity;

    @Override
    public Page<OrderResponse> findAllOffsetPaging(Pageable pageable) {
        JPAQuery<OrderResponse> query = jpaQueryFactory
                .select(new QOrderResponse(
                        qOrderEntity,
                        qOrderDetailEntity,
                        qMember,
                        qAddress,
                        qProductLineEntity))
                .from(qOrderEntity)
                .innerJoin(qOrderEntity.member, qMember)
                .innerJoin(qOrderEntity.address, qAddress)
                .innerJoin(qOrderEntity.orderDetails, qOrderDetailEntity)
                .innerJoin(qOrderDetailEntity.product, qProductEntity)
                .innerJoin(qProductEntity.productLine, qProductLineEntity)
                .where(qOrderEntity.status.eq(Status.WAITING))
                .groupBy(qOrderEntity.orderId);

        // 페이징 적용
        long total = query.fetchCount(); // 전체 레코드 수
        List<OrderResponse> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 추가 데이터 처리
        if (results != null) {
            results.forEach(result -> {
                List<OrderDetailDTO> orderDetailList = jpaQueryFactory
                        .select(new QOrderDetailDTO(
                                qOrderDetailEntity
                        ))
                        .from(qOrderDetailEntity)
                        .where(qOrderDetailEntity.order.orderId.eq(result.getOrderId()))
                        .fetch();

                result.setterOrderDetailList(orderDetailList); // setter 메서드 이름 수정
            });
        }

        // Page 객체로 변환하여 반환
        return new PageImpl<>(results, pageable, total);
    }


    @Override
    public Slice<OrderResponse> findAllSlicePaging(Pageable pageable) {
        JPAQuery<OrderResponse> query = jpaQueryFactory
                .select(new QOrderResponse(
                        qOrderEntity,
                        qOrderDetailEntity,
                        qMember,
                        qAddress,
                        qProductLineEntity))
                .from(qOrderEntity)
                .innerJoin(qOrderEntity.member, qMember)
                .innerJoin(qOrderEntity.address, qAddress)
                .innerJoin(qOrderEntity.orderDetails, qOrderDetailEntity)
                .innerJoin(qOrderDetailEntity.product, qProductEntity)
                .innerJoin(qProductEntity.productLine, qProductLineEntity)
                .where(qOrderEntity.status.eq(Status.WAITING))
                .groupBy(qOrderEntity.orderId);

        // 페이징 적용
        List<OrderResponse> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // 페이지 크기보다 1개 더 가져옴
                .fetch();

        // 추가 데이터 처리
        if (results != null && !results.isEmpty()) {
            results.forEach(result -> {
                List<OrderDetailDTO> orderDetailList = jpaQueryFactory
                        .select(new QOrderDetailDTO(
                                qOrderDetailEntity
                        ))
                        .from(qOrderDetailEntity)
                        .where(qOrderDetailEntity.order.orderId.eq(result.getOrderId()))
                        .fetch();

                result.setterOrderDetailList(orderDetailList); // setter 메서드 이름 수정
            });
        }

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            results.remove(results.size() - 1); // 마지막 요소 제거
            hasNext = true;
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }


    @Override
    public List<OrderResponse> findWaitingOrders() {
        List<OrderResponse> results = jpaQueryFactory
                .select(new QOrderResponse(
                        qOrderEntity,
                        qOrderDetailEntity,
                        qMember,
                        qAddress,
                        qProductLineEntity))
                .from(qOrderEntity)
                .innerJoin(qOrderEntity.member, qMember)
                .innerJoin(qOrderEntity.address, qAddress)
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
                        .where(qOrderDetailEntity.order.orderId.eq(result.getOrderId())
                                .and(qOrderDetailEntity.deletedAt.isNull()))
                        .fetch();

                result.setterOrderDetailList(orderDetailList);
            });
        }
        return results;
    }

    @Override
    public OrderResponse findOrderWithDetails(Long orderId) {

        OrderResponse result = jpaQueryFactory
                .select(new QOrderResponse(
                        qOrderEntity,
                        qOrderDetailEntity,
                        qMember,
                        qAddress,
                        qProductLineEntity))
                .from(qOrderEntity)
                .innerJoin(qOrderEntity.member, qMember)
                .innerJoin(qOrderEntity.address, qAddress)
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
                    .where(qOrderDetailEntity.order.orderId.eq(orderId)
                            .and(qOrderDetailEntity.deletedAt.isNull()))
                    .fetch();

            result.setterOrderDetailList(orderDetailList);
        }
        return result;
    }
}
