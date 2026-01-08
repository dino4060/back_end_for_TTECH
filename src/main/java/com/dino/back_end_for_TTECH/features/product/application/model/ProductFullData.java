package com.dino.back_end_for_TTECH.features.product.application.model;

import java.util.List;

import com.dino.back_end_for_TTECH.features.product.domain.model.ProductSpecification;

public record ProductFullData(
    long id,
    String name,
    String thumb,
    String version,
    String color,
    String status,

    List<String> photos,
    String description,
    int guaranteeMonths,

    PriceData price,
    StockData stock,
    CategoryData category,
    SeriesData series,

    List<ProductSpecification> specifications) {
}
