package org.store.clothstar.order.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalPrice {
    @Column(name = "total_shipping_price")
    private int shipping;
    @Column(name = "total_products_price")
    private int products;
    @Column(name = "total_payment_price")
    private int payment;

    public void updatePrices(int totalProductsPrice, int totalPaymentPrice) {
        this.products = totalProductsPrice;
        this.payment = totalPaymentPrice;
    }
}
