package com.feng.retrofit.api.service;

import com.feng.retrofit.api.host.HostAnoy;
import com.feng.retrofit.api.host.HostApi;
import com.feng.retrofit.api.response.MResponse;
import com.feng.retrofit.model.AdSetModel;
import com.feng.retrofit.model.NewsModel;
import com.feng.retrofit.model.UserModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 首先定义请求接口，即程序中都需要什么请求操作
 */
@HostAnoy.NameSpace(HostApi.SVC_HOST)
public interface NewsService {
    @GET("lore/list?")
        //这里的NewsEntity就是接口给你返回的Json解析实体
    Call<MResponse<NewsModel>> getNewsContent(@Query("page") int page, @Query("rows") int rows);

    @POST("login/app/user")
    @FormUrlEncoded
    Call<UserModel> getUser(@FieldMap Map<String, String> hashMap, @Field("loginname") String loginname,
                            @Field("nloginpwd") String nloginpwd);

    @HostAnoy.NameSpace(HostApi.TZH_HOST)
    interface TzService{
        @GET("ad")
        Call<MResponse<AdSetModel>> fetchAds();

        @POST("user/login")
        Call<MResponse<UserModel>> login(@Body Map<String, Object> params);
    }
}
