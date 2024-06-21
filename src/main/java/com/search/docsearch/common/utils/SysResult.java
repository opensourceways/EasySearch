package com.search.docsearch.common.utils;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SysResult {
    /**
     * The response status.
     */
    private Integer status;
    /**
     * The response msg.
     */
    private String msg;
    /**
     * The response obeject.
     */
    private Object obj;

    public SysResult(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public SysResult() {
    }

    public SysResult(Throwable e) {
        this.status = 201;
        this.msg = e.getMessage();
    }

    // 定义成功的静态方法
    public static SysResult ok(String msg, Object obj) {
        return new SysResult(200, msg, obj);
    }

    // 表示定义成功的静态方法
    public static SysResult ok() {
        return new SysResult(200, null, null);
    }

    public static SysResult ok(Object data) {
        return new SysResult(200, null, data);
    }

    // 定义一个失败的静态方法
    public static SysResult fail(String msg, Object data) {
        return new SysResult(201, msg, data);
    }

    public static SysResult parameterVerificationFailed(String msg) {
        return new SysResult(400, msg, null);
    }

    public static SysResult fail() {
        return new SysResult(201, null, null);
    }
}