package com.search.adapter.condition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class TagsCondition {

    @NotBlank(message = "lang can not be null")
    @Pattern(regexp = "((^zh$|^en$|^ZH$|^EN$))")
    private String lang;

    private String category;

    @NotBlank(message = "want can not be null")
    @Size(max = 20)
    private String want;

    private FieldCondition condition;
}
