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
package com.search.domain.base.vo;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommunityBaseVo {
    /**
     * 标题.
     */
    private String title;
    /**
     * 档案.
     */
    private String archives;
    /**
     * 文章名.
     */
    private String articleName;
    /**
     * 日期.
     */
    private String date;
    /**
     * es数据id,唯一.
     */
    private String id;
    /**
     * 语言.
     */
    private String lang;
    /**
     * 路径,用于前台跳转.
     */
    private String path;
    /**
     * 文本内容.
     */
    private String textContent;

    /**
     * 类型.
     */
    private String type;
    /**
     * 版本.
     */
    private String version;
    /**
     * 类别.
     */
    private String category;
}
