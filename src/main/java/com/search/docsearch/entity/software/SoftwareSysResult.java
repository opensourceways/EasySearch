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


    //定义成功的静态方法
    public static SoftwareSysResult ok(String msg, Object obj) {
        return new SoftwareSysResult(200, msg, obj);
    }

    //定义一个失败的静态方法
    public static SoftwareSysResult fail(String msg, Object data) {
        return new SoftwareSysResult(201, msg, data);
    }


    //dos攻击响应错误码
    public static SoftwareSysResult failWithTooManyRequests() {
        return new SoftwareSysResult(429, "Too Many Requests", null);
    }

    public static SoftwareSysResult fail() {
        return new SoftwareSysResult(201, null, null);
    }
}
