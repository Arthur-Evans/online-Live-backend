package com.niit.onlivestream.common;


/**
 * 错误码

 */
public enum ErrorCode {

    SUCCESS(201, "ok", ""),
    TOKEN_OUTTIME(201,"Token已过期",""),
    PARAMS_ERROR(201, "请求参数错误", ""),
    NULL_ERROR(201, "请求数据为空", ""),
    NOT_LOGIN(201, "未登录", ""),
    NO_AUTH(201, "无权限", ""),
    SYSTEM_ERROR(202, "系统内部异常", "");

    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
