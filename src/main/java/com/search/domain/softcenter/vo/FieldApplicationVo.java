package com.search.domain.softcenter.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FieldApplicationVo extends FieldBaseVo {
    List tags;
    Map pkgIds;
    String iconUrl;
}