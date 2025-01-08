package com.kxj.artadmin.result;

import com.kxj.artadmin.enume.ResultCodeEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result<T> {
    // Getter 和 Setter 方法
    private boolean success;
    private String message;
    private T data;
    private Integer code;

    public Result(boolean success, String message, T data, Integer code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    // 无参构造函数
    public Result() {
    }

    // 静态方法用于创建成功的Result对象
    public static <T> Result<T> success(T data) {
        return new Result<>(true,
                ResultCodeEnum.SUCCESS.getMessage(),
                data,
                ResultCodeEnum.SUCCESS.getCode());
    }



    // 静态方法用于创建失败的Result对象
    public static <T> Result<T> failure(String message,Integer code) {
        return new Result<>(false, message, null,code);
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", code=" + code +
                '}';
    }
}
