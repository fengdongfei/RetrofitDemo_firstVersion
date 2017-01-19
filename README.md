# RetrofitDemo_GET_POST_UPDATE
主要介绍Retrofit的get,post,图片的单多上传使用案例,以及相关几个常用的注解,简单易懂....

# 概述
 首先简单介绍下Retrofit这个框架，Retrofit是底层是基于Okhttp的也就是说用法和Okhttp很相似；Retrofit它是一个HTTP请求工具，和Google开发的Volley功能上非常相似，这里有Volley的源码解析，但是使用上很不相似。
 Retrofit使用起来更简单，Volley使用上更加原始而且符合使用者的直觉，其实我觉得如果对自己Volley封装一下也可以像Retrofit那样的简单的使用；

 ## 注意事項
    *1. 导入的retrofit2包的版本必须要一致，否则就会报错
        compile 'io.reactivex:rxjava:1.1.0'//Rx的包
        compile 'io.reactivex:rxandroid:1.1.0'//Rx的包
        compile 'com.squareup.retrofit2:retrofit:2.0.0-beta4'//Retrofit2所需要的包
        compile 'com.squareup.retrofit2:converter-gson:2.0.0-beta4'//ConverterFactory的Gson依赖包
        compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0-beta4'//CallAdapterFactory的Rx依赖包
        compile 'com.squareup.retrofit2:converter-scalars:2.0.0-beta4'//ConverterFactory的String依赖包
        compile 'com.google.code.gson:gson:2.6.2'//Gosn解析包
    *2. Retrofit提供的请求方式注解有@GET和@POST，参数注解有@PATH和@Field等，我们只介绍常用的;前两个顾名思义就是定义你的请求方式Get or Post
        *@PATH  指的是通过参数填充完整的路径
            @GET("{name}")
               Call<User> getUser(@Path("name") String name);
            这里的参数username会被填充至{name}中，形成完整的Url请求地址，{name}相当于一个占位符；
       * @Query  就是我们的请求的键值对的设置，我们构建Call对象的时候会传入此参数，
       * @FormUrlEncoded  在post中使用
            @POST("mobileLogin/submit.html")
                @FormUrlEncoded
                Call<String> getString(@Field("loginname") String loginname,
                                      @Field("nloginpwd") String nloginpwd);
       * @Field("loginname")就是键，后面的loginname就是具体的值了
       * @POST 使用post方式,指明url
       * @GET 使用get方式

#Retrofit使用步骤:
##get请求方式
以: http://www.tngou.net/api/lore/list?page=?&rows?
## 第一步
    compile 'com.squareup.retrofit:retrofit:2.0.0-beta2'
    compile 'com.squareup.retrofit:converter-gson:2.0.0-beta2'

## 第二步
    编写API服务代码
    public interface NewsService {
        @GET("lore/list?")
        Call<NewsEntity> getNewsContent(@Query("page") int page, @Query("rows") int rows);
    }

## 定义接收数据的NewsEntity类和RetrofitFactory网络访问类

## 获取网络数据
         newsService = RetrofitFactory.getNewsService();
         Call<NewsEntity> call = newsService.getNewsContent(page, rows);
         call.enqueue(new Callback<NewsEntity>() {
             @Override
             public void onResponse(Call<NewsEntity> call, Response<NewsEntity> response) {
             //  请求数据成功
                 Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                 st.setText(response.code()+" "+response.message()+" "+response.body().getTotal()+"  "+response.body().getTngou().get(0).getDescription());
             }

             @Override
             public void onFailure(Call<NewsEntity> call, Throwable t) {
                 // 请求数据失败
                 Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
             }
         });

## post请求方式
    通过@POST指明url，添加@FormUrlEncoded，然后通过@Field添加参数即可
    public interface NewsService {
        @POST("login/app/user")
         @FormUrlEncoded
        Call<NewsEntity> getUser(@FieldMap Map<String, String> hashMap );
    }

## 單張圖片上傳
    @Multipart
    @POST("addPaster.html")
    Call<String> updateImage(@Part MultipartBody.Part  file);

## 多張圖片上傳
    //多张图片上传
     @Multipart
     @POST("addPaster.html")
     Call<String> updateImage(@Part MultipartBody.Part[]  file);

## 圖文上傳
    //图文上传
     @Multipart
     @POST("android/paster/addPaster.html")
     Call<DefaultResult> updateImage(@Part MultipartBody.Part[] parts,@QueryMap Map<String, String> maps);

参考资料:
    http://blog.csdn.net/liuhongwei123888/article/details/50375283
    http://www.cnblogs.com/angeldevil/p/3757335.html
    http://square.github.io/retrofit/
    http://www.jianshu.com/p/eb5d03085926
    https://daidingkang.cc/2016/06/17/Retrofit2-network-framework-parsing/
