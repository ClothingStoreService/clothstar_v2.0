package org.store.clothstar.productLine.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductLineStatus {
    COMING_SOON,
    FOR_SALE,
    ON_SALE,
    SOLD_OUT,
    HIDDEN,
    DISCONTINUED
}