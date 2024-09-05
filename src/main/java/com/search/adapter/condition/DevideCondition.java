package com.search.adapter.condition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class DevideCondition {
    @NotBlank(message = "lang can not be null")
    @Pattern(regexp = "((^zh$|^en$|^ZH$|^EN$))")
    private String lang;

    @Range(min = 1, max = 1000, message = "page must be greater than 0 and less than 1000 ")
    private int page = 1;

    @Range(min = 5, max = 50, message = "pageSize must be greater than 5 and less than 50 ")
    private int pageSize = 10;

    @NotBlank(message = "keyword can not be null")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_ ]+$", message = "Include only letters, digits, and special characters(_-()$.), Contain 1 to 100 characters.")
    @Size(max = 100)
    private String keyword;

    @Pattern(regexp = "^[\\x20a-zA-Z0-9.\\-_ ]*$", message = "Version format is invalid.")
    @Size(max = 50)
    private String version;
}
