package com.feng.retrofit;

import android.database.Observable;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 首先定义请求接口，即程序中都需要什么请求操作
 */

public interface NewsService {
    @GET("lore/list?")
//    Observable<NewsEntity> getNewsContent(@Query("page") int page, @Query("rows") int rows);
    //这里的NewsEntity就是接口给你返回的Json解析实体
    Call<NewsEntity> getNewsContent(@Query("page") int page, @Query("rows") int rows);

    @POST("login/app/user")
    @FormUrlEncoded
    Call<NewsEntity> getUser(@FieldMap Map<String, String> hashMap ,@Field("loginname") String loginname,
                             @Field("nloginpwd") String nloginpwd);

}
