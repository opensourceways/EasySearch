package com.search.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponceResult {

    private Integer status;
    private String msg;
    private Object obj;

    public ResponceResult(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public ResponceResult() {
    }

    public ResponceResult(Throwable e) {
        this.status = 201;
        this.msg = e.getMessage();
    }

    //定义成功的静态方法
    public static ResponceResult ok(String msg, Object obj) {
        return new ResponceResult(200, msg, obj);
    }

    //表示定义成功的静态方法
    public static ResponceResult ok() {
        return new ResponceResult(200, null, null);
    }

    public static ResponceResult ok(Object data) {
        return new ResponceResult(200, "查询成功", data);
    }

    //定义一个失败的静态方法
    public static ResponceResult fail(String msg, Object data) {
        return new ResponceResult(201, msg, data);
    }

    public static ResponceResult parameterVerificationFailed(String msg) {
        return new ResponceResult(400, msg, null);
    }

    public static ResponceResult fail() {
        return new ResponceResult(201, null, null);
    }
}
