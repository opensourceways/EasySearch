package com.search.docsearch.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MySystem {
    /**
     * system.
     */
    public String system;
    /**
     * index.
     */
    public String index;
    /**
     * the index of search word.
     */
    public String searchWordIndex;

    /**
     * the index of pageviews.
     */
    public String pageViewsIndex;
}
