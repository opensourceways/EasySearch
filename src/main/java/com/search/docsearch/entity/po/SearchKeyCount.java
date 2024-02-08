package com.search.docsearch.entity.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class SearchKeyCount implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String searchWord;

    private long searchCount;
}
