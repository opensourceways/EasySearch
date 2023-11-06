package com.search.docsearch.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchDocs {

    private String lang;

    private int page = 1;

    private int pageSize = 10;

    private String keyword;

    private String version;
}
