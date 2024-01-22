package com.search.docsearch.entity.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchTags {

    @NotBlank(message = "lang can not be null")
    @Pattern(regexp = "((^zh$|^en$|^ZH$|^EN$))")
    private String lang;

    private String category;

    @NotBlank(message = "want can not be null")
    @Size(max = 20)
    private String want;

    private Map<String, String> condition;

}


