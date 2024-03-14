package com.search.docsearch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SoftwareTypeEnum {
    APPLICATION("application", "apppkg", "容器镜像"),
    ALL("application", "all", "容器镜像"),
    EKPG("epkg", "epkgpkg", "openeuler软件包"),
    RPMPKG("rpmpkg", "rpmpkg", "rpm软件包");
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
}
