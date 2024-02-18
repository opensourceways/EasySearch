package com.search.docsearch.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Trie {
    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word, long count) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            node = node.children.computeIfAbsent(currentChar, k -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.searchCount = count;
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
