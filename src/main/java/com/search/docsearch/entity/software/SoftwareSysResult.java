package com.search.docsearch.entity.software;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SoftwareSysResult {

    private Integer code;
    private String msg;
    private Object data;

    public SoftwareSysResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public SoftwareSysResult() {
    }

    public SoftwareSysResult(Throwable e) {
        this.code = 201;
        this.msg = e.getMessage();
    }

    //定义成功的静态方法
    public static SoftwareSysResult ok(String msg, Object obj) {
        return new SoftwareSysResult(200, msg, obj);
    }

    //表示定义成功的静态方法
    public static SoftwareSysResult ok() {
        return new SoftwareSysResult(200, null, null);
    }

    public static SoftwareSysResult ok(Object data) {
        return new SoftwareSysResult(200, null, data);
    }

    //定义一个失败的静态方法
    public static SoftwareSysResult fail(String msg, Object data) {
        return new SoftwareSysResult(201, msg, data);
    }

    public static SoftwareSysResult parameterVerificationFailed(String msg) {
        return new SoftwareSysResult(400, msg, null);
    }

    //
    public static SoftwareSysResult failWithTooManyRequests() {
        return new SoftwareSysResult(429, "Too Many Requests", null);
    }

    public static SoftwareSysResult fail() {
        return new SoftwareSysResult(201, null, null);
    }
}
