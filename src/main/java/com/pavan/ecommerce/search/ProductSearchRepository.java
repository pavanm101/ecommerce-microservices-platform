package com.pavan.ecommerce.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Elasticsearch repository for ProductDocument.
 * Provides basic CRUD and custom search methods.
 */
@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {

    // Find by name containing (case-insensitive search)
    List<ProductDocument> findByNameContainingIgnoreCase(String name);

    // Find by category
    List<ProductDocument> findByCategoryId(Long categoryId);

    // Find by price range
    List<ProductDocument> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Find in stock products
    List<ProductDocument> findByInStockTrue();

    // Find by category and in stock
    List<ProductDocument> findByCategoryIdAndInStockTrue(Long categoryId);
}
