package com.search.docsearch.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SysCode implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Integer code;
    private String msg;
    private Object data;

    public SysCode(Integer code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.data = obj;
    }

    public SysCode() {
    }

    public SysCode(Throwable e) {
        this.code = 201;
        this.msg = e.getMessage();
    }

    //定义成功的静态方法
    public static SysCode ok(String msg, Object obj) {
        return new SysCode(200, msg, obj);
    }

    //表示定义成功的静态方法
    public static SysCode ok() {
        return new SysCode(200, null, null);
    }

    public static SysCode ok(Object data) {
        return new SysCode(200, null, data);
    }

    //定义一个失败的静态方法
    public static SysCode fail(String msg, Object data) {
        return new SysCode(201, msg, data);
    }

    public static SysCode fail() {
        return new SysCode(201, null, null);
    }
}
