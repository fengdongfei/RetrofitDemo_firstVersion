package com.feng.retrofit.api;


import com.feng.retrofit.MApplication;
import com.feng.retrofit.utils.MDevice;

import java.util.Date;

/**
 * Created by wumingming on 02/05/2017.
 */
public class AuthStub {
    private static AuthStub sInstance = null;

    public static AuthStub instance() {
        synchronized (AuthStub.class) {
            if (null == sInstance)
                sInstance = new AuthStub();
        }
        return sInstance;
    }

    private AuthStub() {
        deviceId = MDevice.getDeviceId(MApplication.getContext());
        deviceManufacturer = MDevice.getDeviceManufacturer();
        platform = MDevice.getDevicePlatform();
        version = MDevice.getAppVersionName(MApplication.getContext());
        sourceCode = "app";
        token = "login user token";
    }

    private long timeStamp;          //时间戳

    public String deviceId;          //设备编码
    public String deviceManufacturer;//设备制造商
    public String platform;         //平台
    public String version;           //应用版本号
    public String sourceCode;        //应用渠道号
    public String token;             //用户登陆成功后返回的token


    public String toString() {
        return "{" + "\"deviceId\":" + "\"" + deviceId + "\"" +
                ", \"deviceManufacturer\":" + "\"" + deviceManufacturer + "\"" +
                ", \"platform\":" + "\"" + platform + "\"" +
                ", \"version\":" + "\"" + version + "\"" +
                ", \"sourceCode\":" + "\"" + sourceCode + "\"" +
                ", \"token\":" + "\"" + token + "\"" +
                ", \"timestamp\":" + (timeStamp = new Date().getTime()) +
                ", \"signature\":" + "\"" + "MD5" + "\"" + "}";
    }
}
