package com.search.docsearch.dto.software;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SoftwareAppDto {
    List<SoftwareAppChildrenDto> children;
    String name;

    public SoftwareAppDto(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

}
