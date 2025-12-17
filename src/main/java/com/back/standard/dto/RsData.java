package com.back.standard.dto;

public record RsData<T>(
        String resultCode,
        String msg,
        T data
) {
    public int getStatusCode() {
        return Integer.parseInt(resultCode.split("-")[0]);
    }

    public static <T> RsData<T> success(T data) {
        return new RsData<>("200-1", "성공", data);
    }

    public static <T> RsData<T> success(String msg, T data) {
        return new RsData<>("200-1", msg, data);
    }

    public static <T> RsData<T> success(String resultCode, String msg, T data) {
        return new RsData<>(resultCode, msg, data);
    }

    public static <T> RsData<T> fail(String resultCode, String msg) {
        return new RsData<>(resultCode, msg, null);
    }
}
