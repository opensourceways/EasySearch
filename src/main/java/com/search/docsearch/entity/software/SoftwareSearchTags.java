package com.search.docsearch.entity.software;


import jakarta.validation.constraints.NotBlank;
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
public class SoftwareSearchTags {



    private String dataType;

    @NotBlank(message = "want can not be null")
    @Size(max = 20)
    private String want;
    @Size(max = 50)
    private Map<String, String> condition;

}


