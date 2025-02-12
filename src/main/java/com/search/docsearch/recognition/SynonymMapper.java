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
package com.search.docsearch.recognition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import jakarta.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SynonymMapper {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SynonymMapper.class);

    /**
     * Synonym map that used to storing the data
     */
    private Map<String, Set<String>> synonymsMap;
    
    /**
     * synonym path that storing the data
     */
    @Value("${synonym.xmlPath}")
    public String xmlPath;

    /**
     * synonym path that storing the data
     */
    @Value("${synonym.threshold}")
    public int threshold;
    
    /**
     * intialize the SynonymMapper
     * 
     */
    @PostConstruct
    public void init() {
        synonymsMap = new HashMap<>();
        loadSynonymsFromXml(xmlPath);
    }

    /**
     * intialize the SynonymMapper
     * 
     * @param xmlFilePath the storing data
     */
    private void loadSynonymsFromXml(String xmlFilePath)  {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setExpandEntityReferences(false);
            dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
            dbFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFilePath);
            doc.getDocumentElement().normalize();
 
            NodeList synonymList = doc.getElementsByTagName("Synonym");
 
            for (int i = 0; i < synonymList.getLength(); i++) {
                Node synonymNode = synonymList.item(i);
 
                if (synonymNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element synonymElement = (Element) synonymNode;
                    String term = synonymElement.getAttribute("term");
                    Set<String> variants = new HashSet<>();
 
                    NodeList variantList = synonymElement.getElementsByTagName("Variant");
                    for (int j = 0; j < variantList.getLength(); j++) {
                        Node variantNode = variantList.item(j);
 
                        if (variantNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element variantElement = (Element) variantNode;
                            String variant = variantElement.getTextContent().trim();
                            variants.add(variant);
                        }
                    }
                    synonymsMap.put(term, variants);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed init SynonymMapper.");
        }
    }

    /**
     * return the synonyms by query word
     * 
     * @param query the synonyms key word
     * @return the synonyms set of query word
     */
    public Set<String> getSynonyms(String query) {
        return synonymsMap.getOrDefault(query, new HashSet<>());
    }

    /**
     * return the synonyms by query word fuzzy, find the min distance between two words
     * if two the distance between two word less than
     * 
     * @param query the synonyms key word
     * @return the synonyms set of query word
     */
    public Set<String> getSynonymsFuzzy(String query) {

        int mindis = Integer.MAX_VALUE;
        String fuzzySynonym = "";

        for (String word : this.synonymsMap.keySet()) {
            int distance = minDistance(query, word);
            if (distance < mindis) {
                mindis = distance;
                fuzzySynonym = word;
            }
        }

        if(mindis <= threshold){
            return synonymsMap.getOrDefault(fuzzySynonym, new HashSet<>());
        } else {
            return synonymsMap.getOrDefault(query, new HashSet<>());
        }
    }

    /**
     * return the synonyms by query word
     * 
     * @param word1 the synonyms key word
     * @param word2 the synonyms key word
     * @return the distance between two words
     */
    private int minDistance(String word1, String word2) {
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
}
