package com.search.domain.mindspore.dto;

import com.search.adapter.condition.WordConditon;
import com.search.domain.base.dto.SearchWordBaseCondition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class WordUbmcConditon extends SearchWordBaseCondition {
    /**
     * 有参构造，初始化ordUbmcConditon.
     *
     * @param index        数据索引 .
     * @param wordConditon 前台请求封装条件.
     */
    public WordUbmcConditon(WordConditon wordConditon, String index) {
        this.query = wordConditon.getQuery();
        this.index = index;
    }
}
