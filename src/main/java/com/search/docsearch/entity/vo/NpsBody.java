package com.search.docsearch.entity.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
public class NpsBody {
    @NotBlank(message = "PageUrl can not be null")
    @Size(max = 200)
    private String feedbackPageUrl;
    
    @Range(min = 1, max = 10, message = "score must be greater than 0 and less than 10 ")
    private int feedbackValue;
    
    @Size(max =500)
    private String feedbackText;
}