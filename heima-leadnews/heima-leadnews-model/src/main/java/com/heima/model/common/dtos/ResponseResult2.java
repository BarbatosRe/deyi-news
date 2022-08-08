package com.heima.model.common.dtos;

import com.alibaba.fastjson.JSON;
import com.heima.model.common.enums.AppHttpCodeEnum;

import java.io.Serializable;

/**
 * 通用的结果返回类
 * @param <T>
 */
public class ResponseResult2<T> implements Serializable {


    private T data;

    public ResponseResult2(T data) {
        this.data = data;
    }

    public ResponseResult2() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
