package com.search.EaseSearchsearch.service.impl;

import com.search.EaseSearchsearch.service.ParameterVerification;
import com.search.EaseSearchsearch.utils.General;
import com.search.EaseSearchsearch.vo.SearchCondition;
import com.search.EaseSearchsearch.vo.SearchDocs;
import com.search.EaseSearchsearch.vo.SearchTags;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Map;

@Service
public class ParameterVerificationImpl implements ParameterVerification {
    @Override
    public Boolean conditionVerification(SearchCondition condition) {

        if (!langVerification(condition.getLang())) {
            return false;
        }

        if (!paginationVerification(condition.getPage(), condition.getPageSize())) {
            return false;
        }

        if (!StringUtils.hasText(condition.getKeyword())) {
            return false;
        }

        if (condition.getKeyword().length() > 100) {
            return false;
        }

        if (!typeVerification(condition.getType())) {
            return false;
        }

        if (condition.getLimit().size() > 50) {
            return false;
        }

        if (condition.getFilter().size() > 50) {
            return false;
        }

        condition.setKeyword(General.replacementCharacter(condition.getKeyword()));

        return true;
    }

    @Override
    public Boolean langVerification(String lang) {
        if (!String.valueOf(lang).equals("zh") && !String.valueOf(lang).equals("en")) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean searchDocsVerification(SearchDocs searchDocs) {
        if (!langVerification(searchDocs.getLang())) {
            return false;
        }

        if (!paginationVerification(searchDocs.getPage(), searchDocs.getPageSize())) {
            return false;
        }

        if (!StringUtils.hasText(searchDocs.getKeyword())) {
            return false;
        }


        if (searchDocs.getKeyword().length() > 100) {
            return false;
        }

        if (!searchDocs.getVersion().matches("[a-zA-Z0-9\\s_-]+")) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean paginationVerification(int page, int pageSize) {
        if (page < 1) {
            return false;
        }

        if (pageSize > 100 || pageSize < 1) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean typeVerification(String type) {
        if (!StringUtils.hasText(type)) {
            return false;
        }

        if (type.length() > 20) {
            return false;
        }

        if (type.matches("[a-zA-Z0-9\\s_-]+")) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean advancedSearchVerification(Map<String, String> search) throws Exception{
        if (search.size() > 50) {
            return false;
        }

        if (!search.containsKey("lang")) {
            return false;
        }
        if (!"zh".equals(search.get("lang")) && !"en".equals(search.get("lang"))) {
            return false;
        }
        if (!StringUtils.hasText(search.get("keyword"))) {
            return false;
        }

        if (search.get("keyword").length() > 100) {
            return false;
        }

        if (search.containsKey("page")) {
            if (Integer.parseInt(search.get("page")) < 1) {
                return false;
            }
        }

        if (search.containsKey("pageSize")) {
            if (Integer.parseInt(search.get("page")) < 1 || Integer.parseInt(search.get("page")) > 100) {
                return false;
            }
        }

        if (mapVerification(search)) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean searchTagsVerification(SearchTags searchTags) {
        if (!langVerification(searchTags.getLang())) {
            return false;
        }

        if (!typeVerification(searchTags.getCategory())) {
            return false;
        }

        if (!typeVerification(searchTags.getWant())) {
            return false;
        }

        if (searchTags.getCondition().size() > 50) {
            return false;
        }

        if (!mapVerification(searchTags.getCondition())) {
            return false;
        }

        return true;
    }

    @Override
    public Boolean mapVerification(Map<String, String> m) {
        for (Map.Entry<String, String> entry : m.entrySet()) {
            if (!typeVerification(entry.getKey())) {
                return false;
            }
            if (entry.getValue().length() > 20) {
                return false;
            }
        }

        return true;
    }


    @Override
    public String vaildLang(String lang) {
        lang = lang == null ? "zh" : lang;
        if (!lang.equalsIgnoreCase("zh") && !lang.equalsIgnoreCase("en")) {
            throw new IllegalArgumentException("Invalid lang parameter");
        }
        return lang.toLowerCase(Locale.ROOT);
    }

    @Override
    public String vaildCommunity(String community) {
        if (!"mindspore".equalsIgnoreCase(community) && !"opengauss".equalsIgnoreCase(community) && !"openlookeng".equalsIgnoreCase(community)) {
            throw new IllegalArgumentException("Invalid community parameter");
        }
        return community.toLowerCase(Locale.ROOT);
    }

    @Override
    public String vaildEcosystemType(String ecosystemType) {
        ecosystemType = ecosystemType == null ? "Library" : ecosystemType;
        if (!"Library".equalsIgnoreCase(ecosystemType) && !"Tutorial".equalsIgnoreCase(ecosystemType) && !"Model".equalsIgnoreCase(ecosystemType)) {
            throw new IllegalArgumentException("Invalid ecosystemType parameter");
        }
        return ecosystemType;
    }

    @Override
    public String vaildSortType(String sortType) {
        sortType = sortType == null ? "star" : sortType;
        if (!"star".equalsIgnoreCase(sortType) && !"name".equalsIgnoreCase(sortType) && !"repo".equalsIgnoreCase(sortType)) {
            throw new IllegalArgumentException("Invalid sortType parameter");
        }
        return sortType;
    }

    @Override
    public String vaildSortOrder(String sortOrder) {
        sortOrder = sortOrder == null ? "desc" : sortOrder;
        if (!"desc".equalsIgnoreCase(sortOrder) && !"asc".equalsIgnoreCase(sortOrder)) {
            throw new IllegalArgumentException("Invalid sortOrder parameter");
        }
        return sortOrder;
    }

    @Override
    public String vaildPage(String page) {
        page = page == null ? "1" : page;
        if (Integer.parseInt(page) > 100 || Integer.parseInt(page) < 1) {
            throw new IllegalArgumentException("Invalid page parameter");
        }
        return page;
    }

    @Override
    public String vaildPageSize(String pageSize) {
        pageSize = pageSize == null ? "20" : pageSize;
        if (Integer.parseInt(pageSize) > 20 || Integer.parseInt(pageSize) < 5) {
            throw new IllegalArgumentException("Invalid pageSize parameter");
        }
        return pageSize;
    }


}
