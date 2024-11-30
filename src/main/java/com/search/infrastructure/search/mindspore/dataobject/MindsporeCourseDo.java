/* Copyright (c) 2024 openEuler Community
 EasySoftware is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/
package com.search.infrastructure.search.mindspore.dataobject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MindsporeCourseDo {

    /**
     * 课程目录名称.
     */
    private String courseCatalogName;

    /**
     * 二级子课程id.
     */
    private String courseListId;

    /**
     * 视频路径.
     */
    private String path;

    /**
     * 语言.
     */
    private String lang;

    /**
     * 课程目录.
     */
    private String courseCatalog;

    /**
     * 文本内容.
     */
    private String textContent;

    /**
     * 标题.
     */
    private String title;

    /**
     * 类型.
     */
    private String type;

    /**
     * 课程描述.
     */
    private String courseDescription;

    /**
     * 一级课程id.
     */
    private String childrenId;

    /**
     * 课程类别.
     */
    private String courseClasses;

    /**
     * 二级课程封面.
     */
    private String courseCover;

}
