/* Copyright (c) 2024 openEuler Community
 EasySoftware is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/
package com.search.common.util;

import com.search.domain.base.vo.TagsVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Trie {
    /**
     * root nood.
     */
    private final TrieNode root;
    /**
     * searchCountMap.
     */
    private final Map<String, Long> searchCountMap = new HashMap<>();
    /**
     * searchWorldSortList.
     */
    private List<String> searchWorldSortList = new ArrayList<>();

    /**
     * 无参构造，Trie.
     */
    public Trie() {
        root = new TrieNode();
    }

    /**
     * insert Trie.
     *
     * @param word  word.
     * @param count count.
     */
    public void insert(String word, long count) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            node = node.children.computeIfAbsent(currentChar, k -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.searchCount = count;
        this.searchCountMap.put(word, count);
    }

    /**
     * sort SearchWorld.
     */
    public void sortSearchWorld() {
        this.searchWorldSortList = searchCountMap.entrySet().stream()
                //sort a Map by key and stored in resultSortedKey
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(x -> x.getKey())
                .collect(Collectors.toList());
    }

    /**
     * suggestCorrection.
     *
     * @param input inputword.
     * @return String.
     */
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

    /**
     * Calculate the direct distance between the two.
     *
     * @param word1 inputword1.
     * @param word2 inputword2.
     * @return int.
     */
    private int editDistance(String word1, String word2) {
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

    /**
     * searchTopKWithPrefix.
     *
     * @param prefix prefix.
     * @param k      limit num.
     * @return int.
     */
    public List<TagsVo> searchTopKWithPrefix(String prefix, int k) {
        List<TagsVo> words = new ArrayList<>();
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
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .limit(k)
                .collect(Collectors.toList());
    }

    /**
     * find All Words From Node.
     *
     * @param node   node.
     * @param prefix prefix.
     * @param words  resultlist.
     */
    private void findAllWordsFromNode(TrieNode node, String prefix, List<TagsVo> words) {
        if (node.isEndOfWord) {
            words.add(new TagsVo(prefix, node.searchCount));
        }

        for (char ch : node.children.keySet()) {
            findAllWordsFromNode(node.children.get(ch), prefix + ch, words);
        }
    }

    @Data
    public class TrieNode {
        /**
         * children map .
         */
        Map<Character, TrieNode> children;
        /**
         * isEndOfWord .
         */
        boolean isEndOfWord;
        /**
         * search Count .
         */
        long searchCount;

        /**
         * 无参构造，TrieNode.
         */
        public TrieNode() {
            children = new HashMap<>();
            isEndOfWord = false;
            searchCount = 0;
        }

    }

}
