package com.liqun.power.util;

public class Result<T> {

    public int code;

    public T data;

    public String message;

    private Result(int _code, T _data, String _message) {
        this.code = _code;
        this.data = _data;
        this.message = _message;
    }

    public static <T> Result<T> success(T _data) {
        return new Result<T>(200, _data, "success");
    }

    public static <T> Result<T> error(int _code, String _message) {
        return new Result<T>(_code, null, _message);
    }
}
