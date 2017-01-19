package com.feng.retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * 文件上传类
 * Created by admin on 2017/1/19.
 */

public class API {
    private static RetrofitAPI retrofitAPI;

    public static RetrofitAPI Retrofit() {
        if (retrofitAPI == null) {

            retrofitAPI = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.223:8080/pulamsi/android/paster/")
//                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
                    .create(RetrofitAPI.class);
        }
        return retrofitAPI;
    }

    public interface RetrofitAPI {

        //单张图片上传,传递文件必须加上@Multipart
        @Multipart
        @POST("addPaster.html")
        Call<String> updateImage(@Part MultipartBody.Part file);

        //多张图片上传
        @Multipart
        @POST("addPaster.html")
        Call<String> updateImage(@Part MultipartBody.Part[] file);

        //图文上传
        @Multipart
        @POST("android/paster/addPaster.html")
        Call<String> updateImage(@Part MultipartBody.Part[] parts,@QueryMap Map<String, String> maps);
    }
}
