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
package com.search.common.entity;

import lombok.Data;

@Data
public class ResponceResult {
    /**
     * 状态码.
     */
    private Integer status;
    /**
     * 响应消息.
     */
    private String msg;
    /**
     * 响应数据.
     */
    private Object obj;
    /**
     * 响应数据.
     */
    private Object data;

    /**
     * 有参构造，初始ResponceResult.
     *
     * @param status 状态码 .
     * @param msg    响应消息.
     * @param obj    响应数据.
     * @param data   响应数据.
     */
    public ResponceResult(Integer status, String msg, Object obj, Object data) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
        this.data = data;
    }

    /**
     * 定义成功的静态方法.
     *
     * @param msg 响应消息.
     * @param obj 响应数据.
     * @return ResponceResult.
     */
    public static ResponceResult ok(String msg, Object obj) {
        return new ResponceResult(200, msg, obj, null);
    }


    /**
     * 定义成功的静态方法,默认状态码200，数据为空.
     *
     * @return ResponceResult.
     */
    public static ResponceResult ok() {
        return new ResponceResult(200, null, null, null);
    }

    /**
     * 定义成功的静态方法,默认状态码200，msg为‘查询成功’.
     *
     * @param data 响应数据.
     * @return ResponceResult.
     */
    public static ResponceResult ok(Object data) {
        return new ResponceResult(200, "查询成功", data, null);
    }

    /**
     * 定义成功的静态方法,默认状态码200，msg为‘查询成功’.
     *
     * @param data 响应数据.
     * @return ResponceResult.
     */
    public static ResponceResult openmind(Object data) {
        return new ResponceResult(200, "查询成功", null, data);
    }

    /**
     * 定义一个失败的静态方法,默认状态码201，msg为‘查询成功’.
     *
     * @param data 响应数据.
     * @param msg  响应消息.
     * @return ResponceResult.
     */
    public static ResponceResult fail(String msg, Object data) {
        return new ResponceResult(201, msg, data, null);
    }

    /**
     * 定义一个失败的静态方法,默认状态码201.
     *
     * @return ResponceResult.
     */
    public static ResponceResult fail() {
        return new ResponceResult(201, null, null, null);
    }
}
