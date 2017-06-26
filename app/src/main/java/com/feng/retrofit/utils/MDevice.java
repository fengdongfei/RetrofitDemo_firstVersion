package com.feng.retrofit.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;

/**
 * Created by wumingming on 15/05/2017.
 */

public class MDevice {

    public static String touchId(){
        return "+"+ SystemClock.elapsedRealtime();
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        String tmDevice;
        if(null != imei && !"".equals(imei)) {
            tmDevice = "^[0]+$";
            if(!imei.matches(tmDevice)) {
                return imei;
            }
        }

        tmDevice = "" + tm.getDeviceId();
        String tmSerial = "" + tm.getSimSerialNumber();
        String androidId = "" + Settings.Secure.getString(context.getContentResolver(), "android_id");
        return (new UUID((long)androidId.hashCode(), (long)tmDevice.hashCode() << 32 | (long)tmSerial.hashCode())).toString();
    }

    public static String getDevicePlatform() {
        return "Android";
    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";

        try {
            PackageManager e = context.getPackageManager();
            PackageInfo pi = e.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if(versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception var4) {
            Log.e("VersionInfo", "Exception", var4);
        }

        return versionName;
    }

    public static int getAppVersionCode(Context context) {
        int versionCode = 0;

        try {
            PackageManager e = context.getPackageManager();
            PackageInfo pi = e.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception var4) {
            Log.e("VersionInfo", "Exception", var4);
        }

        return versionCode;
    }
}
