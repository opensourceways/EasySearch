
package com.search.docsearch.recognition;

import com.search.docsearch.entity.software.SoftwareSearchCondition;
import com.search.docsearch.enums.QueryTyepEnum;
import com.search.docsearch.except.ServiceException;

public interface RecognitionService {
    QueryTyepEnum ClassifyQuery(SoftwareSearchCondition condition) throws ServiceException;

    SoftwareSearchCondition ProcessQuery(SoftwareSearchCondition condition, QueryTyepEnum queryType)
            throws ServiceException;
}