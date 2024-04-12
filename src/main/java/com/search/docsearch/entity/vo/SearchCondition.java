package com.search.docsearch.entity.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchCondition {

    @NotBlank(message = "lang can not be null")
    @Pattern(regexp = "((^zh$|^en$|^ZH$|^EN$))")
    private String lang;

    @Range(min = 1, max = 1000, message = "page must be greater than 0 and less than 1000 ")
    private int page = 1;

    @Range(min = 5, max = 50, message = "page must be greater than 5 and less than 50 ")
    private int pageSize = 10;

    @NotBlank(message = "keyword can not be null")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_ ]+$", message = "Include only letters, digits, and special characters(_-()$.), Contain 1 to 100 characters.")
    @Size(max = 100)
    private String keyword;
    @Size(max = 15)
    private String type;
    @Size(max = 30)
    private List<Map<String, String>> limit;
    @Size(max = 30)
    private List<Map<String, String>> filter;
}
