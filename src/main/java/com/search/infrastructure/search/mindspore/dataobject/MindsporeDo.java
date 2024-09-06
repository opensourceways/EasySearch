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

import lombok.Data;

@Data
public class MindsporeDo {
    /**
     * 档案
     */
    String archives;


    /**
     * 文章名
     */
    String articleName;

    /**
     * 成分
     */
    String components;


    /**
     * 日期
     */
    String date;


    /**
     * 日期
     */
    String id;

    /**
     * 语言
     */
    String lang;

    /**
     * 路径
     */
    String path;

    /**
     * 子类
     */
    String subclass;


    /**
     * 文本内容
     */
    String textContent;


    /**
     * time
     */
    String time;


    /**
     * 标题
     */
    String title;

    /**
     * 类型
     */
    String type;


    /**
     * 版本
     */
    String version;
}
