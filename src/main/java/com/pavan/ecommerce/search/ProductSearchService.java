package com.pavan.ecommerce.search;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.pavan.ecommerce.models.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for Elasticsearch product search operations.
 * Provides full-text search, faceted navigation, and autocomplete.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * Index a product into Elasticsearch
     */
    public ProductDocument indexProduct(Product product) {
        ProductDocument doc = ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice() != null ? new BigDecimal(product.getPrice()) : null)
                .categoryId(product.getGroup() != null ? product.getGroup().getId() : null)
                .categoryName(product.getGroup() != null ? product.getGroup().getGroupName() : null)
                .userId(product.getUserId())
                .createdAt(LocalDateTime.now())
                .inStock(true)
                .stockQuantity(100) // Default stock
                .suggest(product.getName())
                .build();

        return productSearchRepository.save(doc);
    }

    /**
     * Bulk index multiple products
     */
    public void indexAllProducts(List<Product> products) {
        List<ProductDocument> docs = products.stream()
                .map(p -> ProductDocument.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .price(p.getPrice() != null ? new BigDecimal(p.getPrice()) : null)
                        .categoryId(p.getGroup() != null ? p.getGroup().getId() : null)
                        .categoryName(p.getGroup() != null ? p.getGroup().getGroupName() : null)
                        .userId(p.getUserId())
                        .createdAt(LocalDateTime.now())
                        .inStock(true)
                        .stockQuantity(100)
                        .suggest(p.getName())
                        .build())
                .collect(Collectors.toList());

        productSearchRepository.saveAll(docs);
        log.info("Indexed {} products to Elasticsearch", docs.size());
    }

    /**
     * Full-text search with optional filters
     */
    public SearchResult search(SearchRequest request) {
        // Build query
        NativeQuery query = buildSearchQuery(request);

        // Execute search
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(query, ProductDocument.class);

        // Map results
        List<ProductDocument> products = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return SearchResult.builder()
                .products(products)
                .totalHits(searchHits.getTotalHits())
                .page(request.getPage())
                .size(request.getSize())
                .build();
    }

    /**
     * Build Elasticsearch query from search request
     */
    private NativeQuery buildSearchQuery(SearchRequest request) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        // Full-text search on name and description
        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            boolQuery.must(Query.of(q -> q
                    .multiMatch(m -> m
                            .query(request.getQuery())
                            .fields("name^3", "description")
                            .fuzziness("AUTO"))));
        }

        // Category filter
        if (request.getCategoryId() != null) {
            boolQuery.filter(Query.of(q -> q
                    .term(t -> t.field("categoryId").value(request.getCategoryId()))));
        }

        // Price range filter
        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            boolQuery.filter(Query.of(q -> q
                    .range(r -> {
                        r.field("price");
                        if (request.getMinPrice() != null) {
                            r.gte(co.elastic.clients.json.JsonData.of(request.getMinPrice()));
                        }
                        if (request.getMaxPrice() != null) {
                            r.lte(co.elastic.clients.json.JsonData.of(request.getMaxPrice()));
                        }
                        return r;
                    })));
        }

        // In stock filter
        if (request.getInStock() != null && request.getInStock()) {
            boolQuery.filter(Query.of(q -> q
                    .term(t -> t.field("inStock").value(true))));
        }

        Pageable pageable = PageRequest.of(
                request.getPage() != null ? request.getPage() : 0,
                request.getSize() != null ? request.getSize() : 20);

        return NativeQuery.builder()
                .withQuery(Query.of(q -> q.bool(boolQuery.build())))
                .withPageable(pageable)
                .build();
    }

    /**
     * Autocomplete suggestions
     */
    public List<String> autocomplete(String prefix, int limit) {
        List<ProductDocument> results = productSearchRepository
                .findByNameContainingIgnoreCase(prefix);

        return results.stream()
                .map(ProductDocument::getName)
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Delete product from index
     */
    public void deleteProduct(Long productId) {
        productSearchRepository.deleteById(productId);
    }

    /**
     * Get product by ID
     */
    public ProductDocument getProduct(Long productId) {
        return productSearchRepository.findById(productId).orElse(null);
    }
}
