package com.search.docsearch.common.enums;

import com.search.docsearch.common.utils.Trie;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;

@Getter
@AllArgsConstructor
public enum SoftwareTypeEnum {
    /**
     * ALL serach.
     */
    ALL("", "all", "all", "领域应用", new Trie()),
    /**
     * RPMPKG serach.
     */
    RPMPKG("RPM", "rpmpkg", "rpmpkg", "rpm软件包", new Trie()),
    /**
     * APPLICATION serach.
     */
    APPLICATION("IMAGE", "application", "apppkg", "容器镜像", new Trie()),
    /**
     * EKPG serach.
     */
    EKPG("EPKG", "epkg", "epkgpkg", "openeuler软件包", new Trie()),
    /**
     * The application version from upstream.
     */
    APPVERSION("", "appversion", "appversion", "上游兼容应用全景", new Trie());

    /**
     * The tag on software.
     */
    private final String tag;
    /**
     * Type of software.
     */
    private final String type;
    /**
     * Type of website.
     */
    private final String frontDeskType;
    /**
     * return message.
     */
    private final String message;
    /**
     * trie.
     */
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

    public static String getTagByDataType(String dataType) {
        SoftwareTypeEnum softwareTypeEnum = null;
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            if (value.getType().equals(dataType)) {
                softwareTypeEnum = value;
                break;
            }
        }
        return softwareTypeEnum == null ? null : softwareTypeEnum.getTag();
    }

    public static String getFrontDeskTypeByType(String type) {
        String frontDeskType = null;
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            if (value.getType().equals(type)) {
                frontDeskType = value.getFrontDeskType();
                break;
            }
        }
        return frontDeskType;
    }
}
