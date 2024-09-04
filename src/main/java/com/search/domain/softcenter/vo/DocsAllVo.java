package com.search.domain.softcenter.vo;

import com.search.domain.base.vo.CountVo;
import lombok.Data;

import java.util.List;

@Data
public class DocsAllVo extends CountVo {
    List<NameDocsVo> nameDocs;

    public DocsAllVo(String key, Long docCount, List<NameDocsVo> nameDocs) {
        this.key = key;
        this.doc_count = docCount;
        this.nameDocs = nameDocs;
    }


}