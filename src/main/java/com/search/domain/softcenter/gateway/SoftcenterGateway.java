package com.search.domain.softcenter.gateway;

import com.search.adapter.vo.CountResponceVo;
import com.search.domain.softcenter.dto.DocsSoftcenterCondition;

import com.search.domain.softcenter.vo.DocsAllVo;
import com.search.adapter.vo.SoftWareVo;

import java.util.List;


public interface SoftcenterGateway {

    SoftWareVo searchByCondition(DocsSoftcenterCondition docsOpeneulerCondition);


    CountResponceVo getSearchCountByCondition(DocsSoftcenterCondition docsOpeneulerCondition);

    List<DocsAllVo> getSearchAllByCondition(DocsSoftcenterCondition docsOpeneulerCondition);

}
