package com.dino.back_end_for_TTECH.features.ordering.application.model;

import com.dino.back_end_for_TTECH.features.product.application.model.ProductDataLean;

public record OrderLineData(
        ProductDataLean product,
        int quantity,
        int mainPrice,
        int sidePrice
) {
}
