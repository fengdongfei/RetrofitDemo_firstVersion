package com.feng.retrofit.api;

import java.io.Serializable;

/**
 * Created by shuaizhihao on 16/5/31.
 */
public class MResponse<T extends Serializable> implements MResult {

    private final static long serialVersionUID = 1L;

    public String message() {
        return message;
    }

    public int code() {
        return code;
    }

    public boolean verify() {
        return 0 == code();
    }

    public MResponse() {
        result = null;
        code = -1;
        message = "";
    }


    public int code;
    public String message;
    public T result;
}
