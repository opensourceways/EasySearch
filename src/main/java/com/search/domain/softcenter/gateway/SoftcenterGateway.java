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
package com.search.domain.softcenter.gateway;

import com.search.adapter.vo.CountResponceVo;
import com.search.domain.softcenter.dto.DocsSoftcenterCondition;

import com.search.domain.softcenter.vo.DocsAllVo;
import com.search.adapter.vo.SoftWareVo;

import java.util.List;


public interface SoftcenterGateway {
    /**
     * Search for different types of data.
     *
     * @param docsSoftcenterCondition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    SoftWareVo searchByCondition(DocsSoftcenterCondition docsSoftcenterCondition);

    /**
     * Search the number of data.
     *
     * @param docsSoftcenterCondition The search condition for querying different types of data.
     * @return ResponceResult.
     */
    CountResponceVo getSearchCountByCondition(DocsSoftcenterCondition docsSoftcenterCondition);

    /**
     * Search for the number of specific fields that meet the criteria.
     *
     * @param docsSoftcenterCondition The search condition for querying different types of data.
     * @return List<DocsAllVo>.
     */
    List<DocsAllVo> getSearchAllByCondition(DocsSoftcenterCondition docsSoftcenterCondition);

}
