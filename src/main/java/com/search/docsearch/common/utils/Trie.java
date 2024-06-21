package com.search.docsearch.common.utils;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Trie {
    /**
     * Root node.
     */
    private final TrieNode root;
    /**
     * Count map for counting.
     */
    private final Map<String, Long> searchCountMap = new HashMap<>();
    /**
     * Sort list.
     */
    private List<String> searchWorldSortList = new ArrayList<>();

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word, long count) {
        if (count < 5)
            return;
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            node = node.children.computeIfAbsent(currentChar, k -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.searchCount = count;
        this.searchCountMap.put(word, count);
    }

    public void sortSearchWorld() {
        this.searchWorldSortList = searchCountMap.entrySet().stream()
                // sort a Map by key and stored in resultSortedKey
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(x -> x.getKey())
                .collect(Collectors.toList());
    }

    public String suggestCorrection(String input) {

        int minDistance = Integer.MAX_VALUE;
        String suggestion = "";

        for (String word : this.searchWorldSortList) {
            int distance = editDistance(input, word);
            if (distance < minDistance) {
                minDistance = distance;
                suggestion = word;
            }
        }

        return suggestion;
    }

    private static int editDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        return dp[m][n];
    }

    public static class KeyCountResult {
        String key;
        long count;

        public KeyCountResult(String key, long count) {
            this.key = key;
            this.count = count;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    public List<KeyCountResult> searchTopKWithPrefix(String prefix, int k) {
        List<KeyCountResult> words = new ArrayList<>();
        TrieNode node = root;
        if (prefix.isBlank()) {
            return words;
        }
        for (char ch : prefix.toCharArray()) {
            node = node.children.get(ch);
            if (node == null) {
                return words;
            }
        }
        findAllWordsFromNode(node, prefix, words);

        return words.stream()
                .sorted((a, b) -> Long.compare(b.count, a.count))
                .limit(k)
                .collect(Collectors.toList());
    }

    private void findAllWordsFromNode(TrieNode node, String prefix, List<KeyCountResult> words) {
        if (node.isEndOfWord) {
            words.add(new KeyCountResult(prefix, node.searchCount));
        }

        for (char ch : node.children.keySet()) {
            findAllWordsFromNode(node.children.get(ch), prefix + ch, words);
        }
    }

}
