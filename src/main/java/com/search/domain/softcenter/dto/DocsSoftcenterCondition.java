package com.search.domain.softcenter.dto;


import com.search.domain.base.dto.SearchDocsBaseCondition;
import lombok.Data;

@Data
public class DocsSoftcenterCondition extends SearchDocsBaseCondition implements Cloneable {


    private String dataType;
    private String version;
    private String os;
    private String arch;
    private String category;
    private String keywordType;
    private String nameOrder;
    private String eulerOsVersion;

    @Override
    public DocsSoftcenterCondition clone() {
        DocsSoftcenterCondition condition = null;
        try {
            condition = (DocsSoftcenterCondition) super.clone();
        } catch (CloneNotSupportedException e) {

        }
        return condition;
    }

}
