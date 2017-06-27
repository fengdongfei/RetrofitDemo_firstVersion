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
/**
 * 参数注解有:
 * @PATH  指的是通过参数填充完整的路径
 @GET("{name}")
 Call<User> getUser(@Path("name") String name);
 这里的参数username会被填充至{name}中，形成完整的Url请求地址，{name}相当于一个占位符；

 @Query  就是我们的请求的键值对的设置，我们构建Call对象的时候会传入此参数，

 @FormUrlEncoded  在post中使用
 @POST("mobileLogin/submit.html")
 @FormUrlEncoded
 Call<String> getString(@Field("loginname") String loginname,@Field("nloginpwd") String nloginpwd);

  * @Field("loginname")就是键，后面的loginname就是具体的值了
 */

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