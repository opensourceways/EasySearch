package com.search.infrastructure.search.softcenter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;

@Getter
@AllArgsConstructor
public enum SoftwareTypeEnum {
    ALL("", "all", "all", "领域应用"),
    RPMPKG("RPM", "rpmpkg", "rpmpkg", "rpm软件包"),
    APPLICATION("IMAGE", "application", "apppkg", "容器镜像"),
    EKPG("EPKG", "epkg", "epkgpkg", "openeuler软件包"),
    APPVERSION("", "appversion", "appversion", "上游兼容应用全景");
    private final String tag;
    private final String type;
    private final String frontDeskType;
    private final String message;

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
