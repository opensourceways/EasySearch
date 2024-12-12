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
package com.search.infrastructure.search.ubmc;

import com.search.adapter.vo.*;
import com.search.common.util.General;
import com.search.common.util.Trie;
import com.search.domain.base.dto.DivideDocsBaseCondition;
import com.search.domain.base.vo.TagsVo;
import com.search.domain.mindspore.dto.SuggUbmcCondition;
import com.search.domain.mindspore.dto.TagsUbmcCondition;
import com.search.domain.mindspore.dto.WordUbmcConditon;
import com.search.domain.ubmc.dto.DocsUbmcCondition;
import com.search.domain.ubmc.dto.SortOpenmindCondition;
import com.search.domain.ubmc.gateway.UbmcGateway;
import com.search.domain.ubmc.vo.UbmcVo;
import com.search.infrastructure.search.ubmc.dataobject.UbmcDo;
import com.search.infrastructure.support.action.BaseFounctionGateway;
import com.search.infrastructure.support.converter.CommonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class UbmcGatewayImpl extends BaseFounctionGateway implements UbmcGateway {
    /**
     * current community trie.
     */
    private final Map<String, Trie> trieMap = new HashMap<>();

    /**
     * Search for different types of data.
     *
     * @param searchDocsCondition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    @Override
    public DocsResponceVo<UbmcVo> searchByCondition(DocsUbmcCondition searchDocsCondition) {
        SearchRequest defaultSearchRequest = requestBuilder.getDefaultDocsSearchRequest(searchDocsCondition);
        SearchResponse searchResponse = executeDefaultEsSearch(defaultSearchRequest);
        List<Map<String, Object>> dateMapList = responceHandler.getDefaultsHightResponceToMapList(
                searchResponse, Arrays.asList("title"), "textContent");
        List<UbmcDo> ubmcDos = CommonConverter.toDoList(dateMapList, UbmcDo.class);
        List<UbmcVo> ubmcVos = CommonConverter.toBaseVoList(ubmcDos, UbmcVo.class);
        // 默认v0.0.0版本显示为“”
        for (UbmcVo ubmcVo : ubmcVos) {
            if ("v0.0.0".equals(ubmcVo.getVersion())) {
                ubmcVo.setVersion("");
            }
        }
        if (("docs").equals(searchDocsCondition.getType()) && ("desc").equals(searchDocsCondition.getOrderTime())) {
            Collections.sort(ubmcVos, new Comparator<UbmcVo>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/d");

                @Override
                public int compare(UbmcVo o1, UbmcVo o2) {
                    try {
                        LocalDate date1 = LocalDate.parse(o1.getDate(), formatter);
                        LocalDate date2 = LocalDate.parse(o2.getDate(), formatter);
                        return date1.compareTo(date2);
                    } catch (DateTimeParseException e) {
                        throw new IllegalArgumentException("Invalid date format in UbmcVo objects", e);
                    }
                }
            });
        }
        DocsResponceVo docsResponceVo = new DocsResponceVo(ubmcVos,
                searchDocsCondition.getPageSize(),
                searchDocsCondition.getPage(),
                searchDocsCondition.getKeyword());
        docsResponceVo.setCount(searchResponse.getHits().getTotalHits().value);
        return docsResponceVo;
    }

    /**
     * Search the number of data.
     *
     * @param searchDocsCondition The search condition of ubmc.
     * @return ResponceResult.
     */
    @Override
    public CountResponceVo getSearchCountByCondition(DocsUbmcCondition searchDocsCondition) {
        return super.getDefaultSearchCountByCondition(searchDocsCondition);
    }

    /**
     * Search for sort  of  Ubmc data.
     *
     * @param sortCondition The search condition for  Ubmc.
     * @return SortResponceVo<UbmcVo>.
     */
    @Override
    public SortResponceVo<UbmcVo> getSearchSortByCondition(SortOpenmindCondition sortCondition) {
        return null;
    }

    /**
     * Search the tags of    Ubmc data.
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    @Override
    public TagsResponceVo getSearchTagsByCondition(TagsUbmcCondition tagsCondition) {
        return super.getDefaultSearchTagsByCondition(tagsCondition);
    }

    /**
     * get Dvide Search Sort  of    Ubmc data.
     *
     * @param sortCondition The search condition for querying .
     * @return SortResponceVo<UbmcVo>.
     */
    @Override
    public SortResponceVo<UbmcVo> getDvideSearchSortByCondition(SortOpenmindCondition sortCondition) {
        return null;
    }

    /**
     * Search for Ubmc data.
     *
     * @param DivideDocsBaseCondition The search condition .
     * @return SortResponceVo<UbmcVo>.
     */
    @Override
    public SortResponceVo<UbmcVo> searchDocByType(DivideDocsBaseCondition DivideDocsBaseCondition) {
        return null;
    }

    /**
     * Implement search suggestions.
     *
     * @param suggUbmcCondition SuggBaseCondition.
     * @return SuggResponceVo.
     */
    @Override
    public SuggResponceVo getSuggByCondition(SuggUbmcCondition suggUbmcCondition) {
        String keywordTrim = suggUbmcCondition.getKeyword().trim();
        SuggResponceVo suggResponceVo = new SuggResponceVo();
        List<String> suggestList = new ArrayList<>();
        suggResponceVo.setSuggestList(suggestList);
        System.out.println(keywordTrim);
        List<TagsVo> tagsVoList = getTrie(keywordTrim, suggUbmcCondition.getIndex(), this.trieMap);
        System.out.println(tagsVoList);
        if (!CollectionUtils.isEmpty(tagsVoList)) {
            for (int i = 0; i < tagsVoList.size(); i++) {
                if (tagsVoList.get(i).getKey().equals(keywordTrim)) {
                    return suggResponceVo;
                }
                StringBuilder originBuilder = new StringBuilder();
                originBuilder.append("<em>").append(tagsVoList.get(i).getKey()).append("</em>").append(" ");
                suggestList.add(originBuilder.toString());
            }
        }
        if (suggestList.size() > 0) {
            return suggResponceVo;
        }
        suggUbmcCondition.setAnalyzer("ik_smart");
        suggUbmcCondition.setFieldname("textContent");
        suggUbmcCondition.setMinWordLength(1);
        suggUbmcCondition.setPrefixLength(0);
        suggUbmcCondition.setSize(3);
        return super.getDefaultSuggByCondition(suggUbmcCondition);
    }

    /**
     * Implement search hint.
     *
     * @param wordConditon wordConditon.
     * @return WordResponceVo.
     */
    @Override
    public WordResponceVo getWordByConditon(WordUbmcConditon wordConditon) {
        WordResponceVo wordResponceVo = new WordResponceVo();
        List<TagsVo> keyCountResultList = new ArrayList<>();
        Trie trie = this.trieMap.get(wordConditon.getIndex());
        if (trie == null || trie.getSearchCountMap().size() == 0) {
            trie = initTrie(wordConditon.getIndex(), this.trieMap);
        }
        String prefix = wordConditon.getQuery();
        int preLength = prefix.length() < 3 ? prefix.length() : 3;
        for (int i = 0; i < preLength; i++) {
            String substring = prefix.substring(0, prefix.length() - i);
            keyCountResultList = trie.searchTopKWithPrefix(substring, 10);
            if (!CollectionUtils.isEmpty(keyCountResultList)) {
                break;
            }
        }
        if (CollectionUtils.isEmpty(keyCountResultList)) {
            String newPrefix = General.replacementCharacter(prefix);
            keyCountResultList = trie.searchTopKWithPrefix(newPrefix, 10);
        }
        //没查到根据相似度匹配
        if (CollectionUtils.isEmpty(keyCountResultList)) {
            String suggestCorrection = trie.suggestCorrection(prefix);
            keyCountResultList.addAll(trie.searchTopKWithPrefix(suggestCorrection, 10));
        }
        wordResponceVo.getWord().addAll(keyCountResultList);

        return wordResponceVo;
    }

}
