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
package com.search.infrastructure.support.action;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class BaseFounctionResponceHandler {
    /**
     * Convert an SearchResponse object to an list.
     *
     * @param response SearchResponse.
     * @return An list of map.
     */
    public List<Map<String, Object>> handResponceHitsToMapList(SearchResponse response) {
        List<Map<String, Object>> dateMapList = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            dateMapList.add(map);
        }
        return dateMapList;
    }

    /**
     * Convert an  sugg SearchResponse object to an list.
     *
     * @param suggResponse SearchResponse.
     * @param name         suggname.
     * @return An list of sring.
     */
    public List<String> handSuggResponceToList(SearchResponse suggResponse, String name) {
        ArrayList<String> suggList = new ArrayList<>();
        if (suggResponse.getSuggest() != null && suggResponse.getSuggest().getSuggestion(name) != null) {
            suggResponse.getSuggest().getSuggestion(name).forEach(a -> {
                suggList.add(a.getText().toString());
            });
        }
        return suggList;
    }

    /**
     * Convert an SearchResponse object to an list.
     *
     * @param response SearchResponse.
     * @param term     term.
     * @return An list of map.
     */
    public List<Map<String, Object>> handAggregationToCountList(SearchResponse response, String term) {
        List<Map<String, Object>> numberList = new ArrayList<>();
        ParsedTerms aggregation = response.getAggregations().get(term);
        Map<String, Object> numberMap = new HashMap<>();
        numberMap.put("doc_count", response.getHits().getTotalHits().value);
        numberMap.put("key", "all");
        numberList.add(numberMap);
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            Map<String, Object> countMap = new HashMap<>();
            countMap.put("key", bucket.getKeyAsString());
            countMap.put("doc_count", bucket.getDocCount());
            numberList.add(countMap);
        }
        return numberList;
    }

    /**
     * Convert an SearchResponse object to an list.
     *
     * @param response           SearchResponse.
     * @param highlightFieldList highlightFieldList.
     * @param subStringField     subStringField.
     * @return An list of map.
     */
    public List<Map<String, Object>> getDefaultsHightResponceToMapList(SearchResponse response,
                                                                       List<String> highlightFieldList, String subStringField) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            map.put("score", hit.getScore());
            String subString = String.valueOf(map.getOrDefault(subStringField, ""));
            if (null != subStringField && subStringField.length() > 200) {
                subString = subString.substring(0, 200) + "......";
            }
            map.put(subStringField, subString);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields.containsKey(subStringField)) {
                StringBuilder highLight = new StringBuilder();
                for (Text textContent : highlightFields.get(subStringField).getFragments()) {
                    highLight.append(textContent.toString()).append("<br>");
                }
                map.put(subStringField, highLight.toString());
            }
            for (String str : highlightFieldList) {
                if (highlightFields.containsKey(str)) {
                    map.put(str, highlightFields.get(str).getFragments()[0].toString());
                }
            }
            if (highlightFields.containsKey("title")) {
                map.put("title", highlightFields.get("title").getFragments()[0].toString());
            }

            data.add(map);
        }
        return data;
    }
}
