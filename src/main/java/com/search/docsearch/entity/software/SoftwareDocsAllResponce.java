package com.search.docsearch.entity.software;

import com.search.docsearch.dto.software.SoftwareNameDocsDto;
import lombok.Data;

import java.util.List;

@Data
public class SoftwareDocsAllResponce extends SoftwareSearchCountResponce {
    List<SoftwareNameDocsDto> nameDocs;

    public SoftwareDocsAllResponce( String key, Long docCount,List<SoftwareNameDocsDto> nameDocs){
        this.key=key;
        this.docCount=docCount;
        this.nameDocs=nameDocs;
    }


}
