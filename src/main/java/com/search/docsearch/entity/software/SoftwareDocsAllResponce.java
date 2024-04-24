package com.search.docsearch.entity.software;

import lombok.Data;

import java.util.List;

@Data
public class SoftwareDocsAllResponce extends SoftwareSearchCountResponce {
    List<String> nameDocs;

    public SoftwareDocsAllResponce( String key, Long docCount,List<String> nameDocs){
        this.key=key;
        this.docCount=docCount;
        this.nameDocs=nameDocs;
    }
}
