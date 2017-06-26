package com.feng.retrofit.api.service;

import com.feng.retrofit.api.host.HostAnoy;
import com.feng.retrofit.api.host.HostApi;
import com.feng.retrofit.api.response.MResponse;
import com.feng.retrofit.model.RCFileModel;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

@HostAnoy.NameSpace(HostApi.TZH_HOST)
public interface UpLoadService {

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

        @POST("upload/{at}")
        Call<MResponse<RCFileModel>> upload(@Path("at") String loc, @Body MultipartBody body);
    }