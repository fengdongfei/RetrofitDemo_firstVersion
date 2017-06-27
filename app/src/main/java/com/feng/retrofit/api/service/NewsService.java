package com.feng.retrofit.api.service;

import com.feng.retrofit.api.host.HostAnoy;
import com.feng.retrofit.api.host.HostApi;
import com.feng.retrofit.api.response.MResponse;
import com.feng.retrofit.model.AdSetModel;
import com.feng.retrofit.model.BModel;
import com.feng.retrofit.model.CHitNotifyModel;
import com.feng.retrofit.model.NewsModel;
import com.feng.retrofit.model.UnFollowModel;
import com.feng.retrofit.model.UserModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

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

// 首先定义请求接口，即程序中都需要什么请求操作
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

        @PUT("content/hittedhistory")
        Call<MResponse<CHitNotifyModel>> notifyHit(@Body CHitNotifyModel model);

        @HTTP(method = "DELETE", path = "user/follow", hasBody = true)
        Call<MResponse<BModel>> unFollow(@Body UnFollowModel model);
    }
}
