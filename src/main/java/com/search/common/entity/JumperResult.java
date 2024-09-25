package com.search.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class JumperResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 状态码.
     */
    private Integer code;
    /**
     * 响应消息.
     */
    private String msg;
    /**
     * 响应数据.
     */
    private Object data;

    /**
     * 有参构造，初始JumperResult.
     *
     * @param code 状态码 .
     * @param msg  响应消息.
     * @param obj  响应数据.
     */
    public JumperResult(Integer code, String msg, Object obj) {
        this.code = code;
        this.msg = msg;
        this.data = obj;
    }

    /**
     * 无参构造，初始JumperResult.
     */
    public JumperResult() {
    }

    /**
     * 无参构造，初始JumperResult.
     *
     * @param e 错误类型.
     */
    public JumperResult(Throwable e) {
        this.code = 201;
        this.msg = e.getMessage();
    }

    /**
     * 定义成功的静态方法.
     *
     * @param msg 响应消息.
     * @param obj 响应数据.
     * @return JumperResult.
     */
    public static JumperResult ok(String msg, Object obj) {
        return new JumperResult(200, msg, obj);
    }

    /**
     * 定义成功的静态方法.
     *
     * @return JumperResult.
     */
    public static JumperResult ok() {
        return new JumperResult(200, null, null);
    }

    /**
     * 定义成功的静态方法.
     *
     * @param data 数据.
     * @return JumperResult.
     */
    public static JumperResult ok(Object data) {
        return new JumperResult(200, null, data);
    }

    /**
     * 定义失败的静态方法.
     *
     * @param msg  信息.
     * @param data 数据.
     * @return JumperResult.
     */
    public static JumperResult fail(String msg, Object data) {
        return new JumperResult(201, msg, data);
    }

    /**
     * 定义失败的静态方法.
     *
     * @return JumperResult.
     */
    public static JumperResult fail() {
        return new JumperResult(201, null, null);
    }
}
