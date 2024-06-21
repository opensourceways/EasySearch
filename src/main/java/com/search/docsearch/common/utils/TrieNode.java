package com.search.docsearch.common.utils;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    /**
     * Hash map.
     */
    Map<Character, TrieNode> children;
    /**
     * Is the end of search word.
     */
    boolean isEndOfWord;

    /**
     * The search count.
     */
    long searchCount;

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
        searchCount = 0;
    }

}
