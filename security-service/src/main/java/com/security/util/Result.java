package com.security.util;


import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * return result
 *
 * @author Alan Fu
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -8508268287523566997L;

    /**
     * 类型
     */
    private Type type;
    /**
     * 错误码
     */
    private int code = 200;
    /**
     * 消息
     */
    private List<String> messages;

    /**
     * 数据
     */
    private T data;

    /**
     * 构造函数
     */
    public Result() {
//		messages = new LinkedList<String>();
    }

    /**
     * 构造函数
     *
     * @param data
     */
    public Result(T data) {
        this.data = data;
    }

    /**
     * 构造函数
     *
     * @param type
     * @param message
     */
    public Result(Type type, String message) {
        this.type = type;

        addMessage(message);
    }

    /**
     * 构造函数
     *
     * @param type
     * @param code
     * @param message
     */
    public Result(Type type, int code, String message) {
        this.type = type;
        this.code = code;
        addMessage(message);
    }

    /**
     * 构造函数
     *
     * @param type
     * @param message
     * @param data
     */
    public Result(Type type, String message, T data) {
        this(type, message);
        this.data = data;
    }

    public static <T> Result success() {
        return new Result(Type.SUCCESS, null, null);
    }

    public static <T> Result success(T data) {
        return new Result(Type.SUCCESS, null, data);
    }

    public static <T> Result failure(String message) {
        return new Result(Type.FAILURE, 500, message);
    }

    /**
     * 添加消息
     *
     * @param message
     * @return
     */
    public Result<T> addMessage(String message) {
        if (StringUtils.hasText(message)) {
            messages = new LinkedList<String>();
            messages.add(message);
        }
        return this;
    }

    /**
     * 类型
     *
     * @author ultrafrog
     * @since 1.0
     */
    public enum Type {

        /**
         * 成功
         */
        SUCCESS,

        /**
         * 警告
         */
        WARNING,

        /**
         * 失败
         */
        FAILURE;
    }

}
