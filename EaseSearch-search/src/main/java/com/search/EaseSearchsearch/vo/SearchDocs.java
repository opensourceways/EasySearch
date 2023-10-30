package com.search.EaseSearchsearch.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchDocs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String lang;

    private int page = 1;

    private int pageSize = 10;

    private String keyword;
    
    private String version;
}
