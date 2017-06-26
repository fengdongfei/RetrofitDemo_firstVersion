package com.feng.retrofit.api;

import java.io.Serializable;

/**
 * Created by wumingming on 01/06/2017.
 */

public interface MResult extends Serializable {
    public String message();

    public int code();

    public boolean verify();
}
