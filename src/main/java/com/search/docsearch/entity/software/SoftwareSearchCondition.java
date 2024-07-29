package com.search.docsearch.entity.software;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Slf4j
public class SoftwareSearchCondition implements Cloneable {

    @Range(min = 1, max = 1000, message = "page must be greater than 0 and less than 1000 ")
    private int pageNum = 1;

    @Range(min = 2, max = 100, message = "page must be greater than 5 and less than 50 ")
    private int pageSize = 10;

    @NotBlank(message = "keyword can not be null")
    @Size(max = 100)
  //  @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_ ]+$", message = "Include only letters, digits, and special characters(_-()$.), Contain 1 to 100 characters.")
    private String keyword;
    @Size(max = 10)
    @Pattern(regexp = "((^oepkg$|^all$|^rpmpkg$|^apppkg$|^epkgpkg$|^appversion$|^$))")
    private String dataType;
    @Size(max = 30)
    @Pattern(regexp = "^$|^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_+, ]+$", message = "Null or string. String includes only letters, digits, and special ")
    private String version;
    @Size(max = 100)
    @Pattern(regexp = "^$|^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_+, ]+$", message = "Null or string. String includes only letters, digits, and special ")
    private String os;
    @Size(max = 30)
    @Pattern(regexp = "^$|^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_+, ]+$", message = "Null or string. String includes only letters, digits, and special ")
    private String arch;
    @Size(max = 30)
    @Pattern(regexp = "^$|^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_+, ]+$", message = "Null or string. String includes only letters, digits, and special ")
    private String category;
    @Size(max = 30)
    @Pattern(regexp = "^$|^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_+, ]+$", message = "Null or string. String includes only letters, digits, and special ")
    private String keywordType;
    @Size(max = 5)
    @Pattern(regexp = "((^desc$|^asc$|^$))")
    private String nameOrder;
    @Size(max = 30)
    @Pattern(regexp = "^$|^[\\u4E00-\\u9FA5A-Za-z0-9.()$\\-_+, ]+$", message = "Null or string. String includes only letters, digits, and special ")
    private String eulerOsVersion;
    @Size(max = 5)
    @Pattern(regexp = "((^desc$|^asc$|^$))")
    private String timeOrder;
    @Override
    public SoftwareSearchCondition clone() {
        SoftwareSearchCondition condition = null;
        try {
            condition = (SoftwareSearchCondition) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("clone failed");
        }
        return condition;
    }
}
