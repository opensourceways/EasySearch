package com.search.docsearch.enums;

import com.search.docsearch.utils.Trie;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;

@Getter
@AllArgsConstructor
public enum SoftwareTypeEnum {
    APPLICATION("application", "apppkg", "容器镜像", new Trie()),
    ALL("application", "all", "容器镜像", new Trie()),
    EKPG("epkg", "epkgpkg", "openeuler软件包", new Trie()),
    RPMPKG("rpmpkg", "rpmpkg", "rpm软件包", new Trie());
    private final String type;
    private final String frontDeskType;
    private final String message;
    private final Trie trie;

    public static String getTypeByfrontDeskType(String frontDeskType) {
        String type = null;
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            if (value.getFrontDeskType().equals(frontDeskType)) {
                type = value.getType();
                break;
            }
        }
        return type;
    }


    public static List<String> getTypeList() {
        HashSet<String> typeSet = new HashSet<>();
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            typeSet.add(value.type);
        }
        return typeSet.stream().toList();
    }

    public static SoftwareTypeEnum getEnumByfrontDeskType(String frontDeskType) {
        SoftwareTypeEnum softwareTypeEnum = null;
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            if (value.getFrontDeskType().equals(frontDeskType)) {
                softwareTypeEnum = value;
                break;
            }
        }
        return softwareTypeEnum;
    }
}
