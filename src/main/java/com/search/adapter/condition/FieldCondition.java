package com.search.adapter.condition;

import com.search.common.constant.SearchConstant;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class FieldCondition {
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REG, message = SearchConstant.VALID_MESSAGE)
    String type;
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REG, message = SearchConstant.VALID_MESSAGE)
    String components;
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REG, message = SearchConstant.VALID_MESSAGE)
    String name;
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REG, message = SearchConstant.VALID_MESSAGE)
    String docsType;
    @Size(max = 10)
    @Pattern(regexp = SearchConstant.SOFTWARE_VALID_DATATYPE)
    private String dataType;
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REG, message = SearchConstant.VALID_MESSAGE)
    private String version;
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REG, message = SearchConstant.VALID_MESSAGE)
    private String os;
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REG, message = SearchConstant.VALID_MESSAGE)
    private String arch;
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REG, message = SearchConstant.VALID_MESSAGE)
    private String category;
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REG, message = SearchConstant.VALID_MESSAGE)
    private String keywordType;
    @Size(max = 5)
    @Pattern(regexp = SearchConstant.SOFTWARE_VALID_NAMEORDER)
    private String nameOrder;
    @Size(max = 30)
    @Pattern(regexp = SearchConstant.VALID_STR_REG, message = SearchConstant.VALID_MESSAGE)
    private String eulerOsVersion;


    private String tags;
}
