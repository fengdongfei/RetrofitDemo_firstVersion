package com.feng.retrofit.model;

import android.util.Log;

import com.feng.retrofit.MApplication;
import com.feng.retrofit.api.host.HostApi;
import com.feng.retrofit.api.response.MCallback;
import com.feng.retrofit.api.response.MResponse;
import com.feng.retrofit.api.service.UpLoadService;
import com.feng.retrofit.retrofit.RetrofitFactory;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.shaohui.advancedluban.Luban;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by wumingming on 15/05/2017.
 */

public class RCFileModel extends BModel {

    public static final String[] sImageType = new String[]{"jpeg", "jpg", "png"};

    public static final int MODE_S = -1;
    public static final int MODE_M = 0;
    public static final int MODE_L = 1;
    public static final int MODE_N = -2;

    public static String imageOf(String path, int mode) {
        StringBuilder urlBuilder = new StringBuilder("" + path);
        int sufPos = urlBuilder.lastIndexOf(".");
        sufPos = (sufPos == -1) ? urlBuilder.length() : sufPos;
        switch (mode) {
            case MODE_L:
                urlBuilder.insert(sufPos, "_L");
                break;
            case MODE_M:
                urlBuilder.insert(sufPos, "_M");
                break;
            case MODE_S:
                urlBuilder.insert(sufPos, "_S");
                break;
            default:
                break;
        }
        return HostApi.RCS_IMG + urlBuilder.toString();
    }

    public static Observable<File> getCompressFileTask(File file) {
        String fileName = file.getName().toLowerCase();
        for (String type : sImageType) {
            if (!fileName.endsWith(type))
                continue;

            return Luban.compress(MApplication.getContext(), file)
                    .setMaxSize(5000)               // limit the final image size（unit：Kb）, 最大 5000 K
                    .setMaxHeight(1920)             // limit image height
                    .setMaxWidth(1080)              // limit image width
                    .putGear(Luban.CUSTOM_GEAR)     // 自定义
                    .asObservable();
        }

        return null;
    }

    /**
     * 上传图片, 带压缩
     *
     * @param file
     * @param atLoc
     * @param
     */
    public static void upload(File file, final String atLoc) {

        Observable<File> compressFileTask = getCompressFileTask(file);
        if (compressFileTask == null) {
            Log.d("RCFileModel", "图片格式不支持");
            return;
        }

        compressFileTask.subscribe(new Observer<File>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(File compressedFile) {
                uploadRaw(compressedFile, atLoc);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("RCFileModel", e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /**
     * 上传原始图片
     *
     * @param file
     * @param atLoc
     */
    public static void uploadRaw(File file, final String atLoc) {
        Log.d("path", file.getAbsolutePath());

        RequestBody rBody = MultipartBody.create(MediaType.parse(""), file);
        rBody = new MultipartBody.Builder()
                .addFormDataPart("uploadfile", file.toString().toLowerCase(), rBody)
                .build();
        RetrofitFactory.getInstance().create(UpLoadService.class).upload(atLoc, (MultipartBody) rBody)
                .enqueue(new MCallback<MResponse<RCFileModel>>(MApplication.getContext()) {
                    @Override
                    protected void onSuccess(MResponse<RCFileModel> result) {
                        Log.d("RCFileModel", "uploadRaw success");
                    }

                    @Override
                    protected void onFail(int errorCode, String errorInfo) {
                        Log.d("RCFileModel", "uploadRaw fail" + errorCode + "  " + errorInfo);
                    }
                });
    }

    public String value;
}
