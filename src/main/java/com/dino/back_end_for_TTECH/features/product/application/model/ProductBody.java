package com.dino.back_end_for_TTECH.features.product.application.model;

import com.dino.back_end_for_TTECH.features.product.domain.model.ProductSpecification;
import com.dino.back_end_for_TTECH.shared.application.utils.ObjectId;

import java.util.List;

public record ProductBody(
    String name,
    String thumb,
    String version,
    String color,

    List<String> photos,
    String description,
    int guaranteeMonths,

    ObjectId category,
    ObjectId series,
    PriceBody price,
    StockBody stock,

    List<ProductSpecification> specifications) {
}
