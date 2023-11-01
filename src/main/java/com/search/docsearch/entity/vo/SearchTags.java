package com.search.docsearch.entity.vo;

import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchTags{

    @NotBlank(message = "lang can not be null")
    @Pattern(regexp = "((^zh$|^en$|^ZH$|^EN$))")
    private String lang;

    @Pattern(regexp = "((^showcase$|^service$|^other$|^news$|^migration$|^forum$|^docs$|^blog$|^events$|^blogs$|^tutorials$|^install$|^information$|^api$|^video$|^activity$)|^$)", message = "Keyword format is invalid")
    private String category;
    
    @NotBlank(message = "want can not be null")
    @Pattern(regexp = "((^showcase$|^service$|^other$|^news$|^migration$|^forum$|^docs$|^blog$|^events$|^blogs$|^tutorials$|^install$|^information$|^api$|^video$|^activity$)|^$)", message = "Keyword format is invalid")
    @Size(max = 20)
    private String want;

    private Map<String, String> condition;

}


