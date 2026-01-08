package com.dino.back_end_for_TTECH.features.ordering.application.model;

import com.dino.back_end_for_TTECH.features.product.application.model.ProductDataLean;

public record CartLineData(
        Long id,
        int quantity,
        ProductDataLean product
) {
}