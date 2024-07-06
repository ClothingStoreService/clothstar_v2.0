DROP TABLE IF EXISTS `order_detail`;

CREATE TABLE `order_detail`
(
    `order_detail_id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `product_line_id`     BIGINT       NOT NULL,
    `order_id`            BIGINT       NOT NULL,
    `product_id`          BIGINT       NOT NULL,
    `quantity`            int          NOT NULL,
    `fixed_price`         int          NOT NULL,
    `onekind_total_price` int          NOT NULL,

    PRIMARY KEY (`order_detail_id`)
);

select *
from order_detail;

ALTER TABLE `order_detail`
    ADD CONSTRAINT `FK_orders_TO_orderDetail_1` FOREIGN KEY (`order_id`)
        REFERENCES `orders` (`order_id`);

ALTER TABLE `order_detail`
    ADD CONSTRAINT `FK_ProductLine_TO_orderDetail_1` FOREIGN KEY (`product_line_id`)
        REFERENCES product_line (`product_line_id`);

ALTER TABLE order_detail
    DROP FOREIGN KEY `FK_ProductLine_TO_orderDetail_1`;

ALTER TABLE `order_detail`
    ADD CONSTRAINT `FK_Product_TO_orderDetail_1` FOREIGN KEY (`product_id`)
        REFERENCES `product` (`product_id`);

ALTER TABLE order_detail
    DROP FOREIGN KEY `FK_Product_TO_orderDetail_1`;