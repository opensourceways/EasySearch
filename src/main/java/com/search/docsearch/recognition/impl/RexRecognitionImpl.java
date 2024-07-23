package com.search.docsearch.recognition.impl;

import com.search.docsearch.DocSearchApplication;
import com.search.docsearch.entity.software.SoftwareSearchCondition;
import com.search.docsearch.enums.QueryTyepEnum;
import com.search.docsearch.except.ServiceException;
import com.search.docsearch.recognition.RecognitionService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.boot.SpringApplication;

@Service
public class RexRecognitionImpl implements RecognitionService {

    @Override
    public QueryTyepEnum ClassifyQuery(SoftwareSearchCondition condition) throws ServiceException {
        String query = condition.getKeyword();
        String lowerCaseQuery = query.toLowerCase();
        if (lowerCaseQuery.contains("command")) {
            return QueryTyepEnum.ERROR_QUERY;
        }

        if (lowerCaseQuery.contains(" ")) {
            String[] split = lowerCaseQuery.split(" ");
            if (split.length > 1) {
                String version = split[1];
                if (version.matches("^[\\d\\w.-]+$")) {
                    condition.setVersion(version);
                    condition.setKeyword(split[0]);
                    return QueryTyepEnum.VERSION_QERTY;
                }
            }

        }
        return QueryTyepEnum.KEYWORD_QUERY;
    }

    @Override
    public SoftwareSearchCondition ProcessQuery(SoftwareSearchCondition condition, QueryTyepEnum queryType)
            throws ServiceException {

        String query = condition.getKeyword();
        if (query.contains("command") && query.contains(":")) {
            condition.setKeyword(query.split(":")[0]);
            return condition;
        }
        if (queryType == QueryTyepEnum.ERROR_QUERY) {
            Pattern pattern = Pattern.compile("'([^']+)'");
            Matcher matcher = pattern.matcher(query);

            while (matcher.find()) {
                // matcher.group(1) 获取第一个括号内的内容
                String matchedContent = matcher.group(1);
                condition.setKeyword(matchedContent);
                break;
            }
        }
        return condition;
    }

}
