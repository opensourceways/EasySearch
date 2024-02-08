package com.search.docsearch.utils;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    Map<Character, TrieNode> children;

    boolean isEndOfWord;

    long searchCount;

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
        searchCount = 0;
    }

}
