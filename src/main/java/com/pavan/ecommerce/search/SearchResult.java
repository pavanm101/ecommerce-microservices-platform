package com.pavan.ecommerce.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Search result DTO with products, pagination, and facets.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {

    private List<ProductDocument> products;
    private long totalHits;
    private int page;
    private int size;
    private Map<String, List<FacetValue>> facets;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacetValue {
        private String value;
        private long count;
    }
}
