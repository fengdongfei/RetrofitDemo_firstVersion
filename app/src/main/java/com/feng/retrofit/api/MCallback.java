package com.feng.retrofit.api;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shuaizhihao on 16/6/1.
 */
public abstract class MCallback<T extends MResult> implements Callback<T> {

    public static final int R_OK = 200;
    public static final int R_Network_Error = 500;
    public static final int R_Unexpected_Error = 501;
    public static final int R_Unauthenticated_Error = 502;
    public static final int R_Client_Error = 503;
    public static final int R_Server_Error = 504;

    private Context mContext;
    private boolean mErrIgnore = false;

    public MCallback(Context context) {
        this.mContext = context;
    }

    public MCallback(Context context, boolean errIgnore) {
        this.mErrIgnore = errIgnore;
        this.mContext = context;
    }

    protected abstract void onSuccess(T result);

    protected abstract void onFail(int errorCode, String errorInfo);

    private boolean isAlive() {
        if (mContext == null)
            return false;

        if (mContext instanceof Activity) {
            if (((Activity) mContext).isFinishing())
                return false;
        }
        return true;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        if (!isAlive()) return;

        String errorInfo = "错误信息";
        //String errorInfo = response.message();
        int errorCode = response.code();
        if (errorCode == 200) {
            errorCode = R_OK;
        } else if (errorCode == 401) {
            errorCode = R_Unauthenticated_Error;
        } else if (errorCode >= 400 && errorCode < 500) {
            errorCode = R_Client_Error;
        } else if (errorCode >= 500 && errorCode < 600) {
            errorCode = R_Server_Error;
        } else {
            errorCode = R_Unexpected_Error;
        }

        if (R_OK != errorCode) {
            if (!mErrIgnore) onFail(errorCode, errorInfo);
            return;
        }

        T body = response.body();
        if (null == body) {
            if (!mErrIgnore) onFail(errorCode, errorInfo);
            return;
        }

        if (body.verify()) {
            onSuccess(body);
            return;
        }
        if (!mErrIgnore) onFail(body.code(), body.message());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

        if (!isAlive()) return;

        if (!mErrIgnore)
            onFail((t instanceof IOException) ? R_Network_Error : R_Unexpected_Error, t.getMessage());
    }

}
