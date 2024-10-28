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
package com.search.infrastructure.search.mindspore;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.DocsResponceVo;
import com.search.adapter.vo.SuggResponceVo;
import com.search.adapter.vo.TagsResponceVo;
import com.search.adapter.vo.WordResponceVo;
import com.search.common.util.General;
import com.search.common.util.Trie;
import com.search.domain.base.vo.TagsVo;
import com.search.domain.mindspore.dto.DocsMindsporeCondition;
import com.search.domain.mindspore.dto.SuggMindsporeCondition;
import com.search.domain.mindspore.dto.TagsMindsporeCondition;
import com.search.domain.mindspore.dto.WordMindsporeConditon;
import com.search.domain.mindspore.gateway.MindSporeGateway;
import com.search.domain.mindspore.vo.MindSporeVo;
import com.search.infrastructure.support.action.BaseFounctionGateway;
import com.search.infrastructure.support.config.EsPopwordConfig;
import com.search.infrastructure.support.converter.CommonConverter;
import com.search.infrastructure.search.mindspore.dataobject.MindsporeDo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class MindsporeGatewayImpl extends BaseFounctionGateway implements MindSporeGateway {
    /**
     * current community trie.
     */
    private final Map<String, Trie> trieMap = new HashMap<>();
    /**
     * search popular word.
     */
    private final EsPopwordConfig esPopwordConfig;

    /**
     * Search for different types of data.
     *
     * @param searchBaseCondition The search condition for querying MindSpore.
     * @return ResponceResult.
     */
    @Override
    public DocsResponceVo<MindSporeVo> searchByCondition(DocsMindsporeCondition searchBaseCondition) {
        String keyword = searchBaseCondition.getKeyword();
        String keywordSpan = "<span>" + keyword + "</span>";
        List<Map<String, Object>> dateMapList = super.getDefaultSearchByCondition(searchBaseCondition);
        if (dateMapList != null) {
            for (int i = 0; i < dateMapList.size(); i++) {
                Map<String, Object> map = dateMapList.get(i);
                String title = String.valueOf(map.get("title"));
                title = title.replace("<span>", "").replace("</span>", "");
                map.put("title", title.replace(keyword, keywordSpan));

            }
        }
        List<MindsporeDo> mindsporeDos = CommonConverter.toDoList(dateMapList, MindsporeDo.class);
        List<MindSporeVo> mindSporeVos = CommonConverter.toBaseVoList(mindsporeDos, MindSporeVo.class);
        DocsResponceVo<MindSporeVo> docsResponceVo = new DocsResponceVo(mindSporeVos,
                searchBaseCondition.getPageSize(),
                searchBaseCondition.getPage(),
                searchBaseCondition.getKeyword());
        return docsResponceVo;
    }

    /**
     * Search the number of   MindSpore data.
     *
     * @param condition The search condition for querying MindSpore.
     * @return CountResponceVo.
     */
    @Override
    public CountResponceVo getSearchCountByCondition(DocsMindsporeCondition condition) {
        return super.getDefaultSearchCountByCondition(condition);
    }


    /**
     * Search the tags of   MindSpore data..
     *
     * @param tagsCondition The search condition for querying tags.
     * @return TagsResponceVo.
     */
    @Override
    public TagsResponceVo getSearchTagsByCondition(TagsMindsporeCondition tagsCondition) {
        return super.getDefaultSearchTagsByCondition(tagsCondition);
    }

    /**
     * Implement search suggestions.
     *
     * @param suggMindsporeCondition The search condition for querying different types of data.
     * @return SuggResponceVo.
     */
    @Override
    public SuggResponceVo getSuggByCondition(SuggMindsporeCondition suggMindsporeCondition) {
        String keywordTrim = suggMindsporeCondition.getKeyword().trim();
        SuggResponceVo suggResponceVo = new SuggResponceVo();
        List<String> suggestList = new ArrayList<>();
        suggResponceVo.setSuggestList(suggestList);
        List<TagsVo> tagsVoList = getTrie(keywordTrim, suggMindsporeCondition.getIndex());
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
        suggMindsporeCondition.setAnalyzer("ik_smart");
        suggMindsporeCondition.setFieldname("title");
        suggMindsporeCondition.setMinWordLength(2);
        suggMindsporeCondition.setPrefixLength(0);
        suggMindsporeCondition.setSize(3);
        return super.getDefaultSuggByCondition(suggMindsporeCondition);
    }

    /**
     * Implement search hint.
     *
     * @param wordConditon wordConditon.
     * @return WordResponceVo.
     */
    @Override
    public WordResponceVo getWordByConditon(WordMindsporeConditon wordConditon) {
        WordResponceVo wordResponceVo = new WordResponceVo();
        List<TagsVo> keyCountResultList = new ArrayList<>();
        Trie trie = this.trieMap.get(wordConditon.getIndex());
        if (trie == null || trie.getSearchCountMap().size() == 0) {
            trie = initTrie(wordConditon.getIndex());
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
        /*if (keyCountResultList.size() == 0) {
            SuggMindsporeCondition suggMindsporeCondition = new SuggMindsporeCondition();
            suggMindsporeCondition.setIndex(wordConditon.getIndex());
            suggMindsporeCondition.setKeyword(wordConditon.getQuery());
            SuggResponceVo suggByCondition = getSuggByCondition(suggMindsporeCondition);
            if (suggByCondition.getSuggestList() != null) {
                suggByCondition.getSuggestList().forEach(a -> {
                    TagsVo tagsVo = new TagsVo();
                    tagsVo.setKey(a);
                    tagsVo.setCount(1L);
                    wordResponceVo.getWord().add(tagsVo);
                });
            }
        }*/

        return wordResponceVo;
    }

    /**
     * Implement search Hotwords.
     *
     * @param lang language.
     * @return List<String>.
     */
    @Override
    public List<String> getHotwords(String lang) {
        List<String> wordList = new ArrayList<>();
        List<EsPopwordConfig.Pop> popwordConfigPop = esPopwordConfig.getPop();
        if (popwordConfigPop != null) {
            List<EsPopwordConfig.Pop> mindsporePopWord = popwordConfigPop.stream().filter(a ->
                    a.getLang().equals(lang) && a.getSource().equals("mindspore")).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(mindsporePopWord)) {
                EsPopwordConfig.Pop pop = mindsporePopWord.get(0);
                Integer num = pop.getNum();
                List<String> origin = Arrays.asList(pop.getWord().split(","));
                Collections.shuffle(origin);
                wordList = origin.subList(0, num);
            }
        }
        return wordList;
    }

    /**
     * init Trie.
     *
     * @param index index.
     * @return Trie.
     */
    private Trie initTrie(String index) {
        List<TagsVo> tagsVoList = aggFieldCount("title", index);
        Trie trie = new Trie();
        for (TagsVo a : tagsVoList) {
            trie.insert(a.getKey(), a.getCount().intValue());
        }
        this.trieMap.put(index, trie);
        trie.sortSearchWorld();
        return trie;
    }

    private List<TagsVo> getTrie(String input, String index) {
        List<TagsVo> keyCountResultList = new ArrayList<>();
        Trie trie = this.trieMap.get(index);
        if (trie == null || trie.getSearchCountMap().size() == 0) {
            trie = initTrie(index);
        }
        String prefix = input;
        for (int i = 0; i < 3 && i < prefix.length(); i++) {
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
        return keyCountResultList;
    }
}
