package com.pavan.ecommerce.controllers;

import com.pavan.ecommerce.search.ProductDocument;
import com.pavan.ecommerce.search.ProductSearchService;
import com.pavan.ecommerce.search.SearchRequest;
import com.pavan.ecommerce.search.SearchResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for product search operations.
 * Provides full-text search, autocomplete, and filtered search.
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Product search and autocomplete APIs")
public class SearchController {

    private final ProductSearchService productSearchService;

    @GetMapping("/products")
    @Operation(summary = "Search products", description = "Full-text search with optional filters")
    public ResponseEntity<SearchResult> searchProducts(
            @Parameter(description = "Search query text") @RequestParam(required = false) String q,

            @Parameter(description = "Category ID filter") @RequestParam(required = false) Long categoryId,

            @Parameter(description = "Minimum price filter") @RequestParam(required = false) BigDecimal minPrice,

            @Parameter(description = "Maximum price filter") @RequestParam(required = false) BigDecimal maxPrice,

            @Parameter(description = "Filter by in-stock status") @RequestParam(required = false) Boolean inStock,

            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") Integer page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") Integer size) {
        SearchRequest request = SearchRequest.builder()
                .query(q)
                .categoryId(categoryId)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .inStock(inStock)
                .page(page)
                .size(size)
                .build();

        SearchResult result = productSearchService.search(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/autocomplete")
    @Operation(summary = "Autocomplete suggestions", description = "Get product name suggestions for search")
    public ResponseEntity<List<String>> autocomplete(
            @Parameter(description = "Search prefix", required = true) @RequestParam String prefix,

            @Parameter(description = "Maximum suggestions to return") @RequestParam(defaultValue = "10") Integer limit) {
        List<String> suggestions = productSearchService.autocomplete(prefix, limit);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "Get product by ID from search index")
    public ResponseEntity<ProductDocument> getProduct(@PathVariable Long id) {
        ProductDocument product = productSearchService.getProduct(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
}
