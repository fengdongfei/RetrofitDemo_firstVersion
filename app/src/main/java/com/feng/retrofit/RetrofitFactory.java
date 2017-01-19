package com.feng.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 参考资料: http://square.github.io/retrofit/
 * GET,POST请求类
 * Created by Administrator on 2016/8/31.
 */
public class RetrofitFactory {
    private static NewsService newsService;
    //使用Retrofit请求数据
    public static synchronized NewsService getNewsService() {
        if (newsService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Appcinfig.BASE_PATH)
                    .addConverterFactory(GsonConverterFactory.create())//设置解析方式GOSN
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(new OkHttpClient())
                    .build();
            newsService = retrofit.create(NewsService.class);
        }
        return newsService;
    }
}
