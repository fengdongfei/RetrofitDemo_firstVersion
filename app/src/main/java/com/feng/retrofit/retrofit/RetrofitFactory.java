package com.feng.retrofit.retrofit;

import android.util.Log;

import com.feng.retrofit.BuildConfig;
import com.feng.retrofit.api.host.HostAnoy;
import com.feng.retrofit.api.response.AuthStub;
import com.feng.retrofit.api.response.HttpLoggingInterceptor;
import com.socks.library.KLog;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitFactory {
    private static RetrofitFactory mInstance;

    public static RetrofitFactory getInstance(){
        if (mInstance==null){
            mInstance=new RetrofitFactory();
        }
        return mInstance;
    }

    public RetrofitFactory() {

    }

    /**
     * 利用注解配置host
     * @param mClass
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> mClass) {
        HostAnoy.NameSpace ns = mClass.getAnnotation(HostAnoy.NameSpace.class);
        Log.e("TAG---",ns.value());
        if (null == ns) try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ns.value())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp())
                .build();
        return retrofit.create(mClass);
    }

    /**
     * retrofit的header配置
     * @return
     */
    private OkHttpClient okHttp() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(Interceptor.Chain chain, String message) {
                KLog.json(getTag(chain.request().url()), message);
            }

            private String getTag(HttpUrl url) {
                if(url == null)
                    return "okhttp";
                return "okhttp-" + url.uri().getPath();
            }
        });
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.BASIC);
        okBuilder.addInterceptor(interceptor);
        /*
        cookieJar = new PersistentCookieJar(new SetCookieCache(),
              new SharedPrefsCookiePersistor(CFApplication.getApplication()));
        okBuilder.cookieJar(cookieJar);
        */

        okBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                final Request origRequest = chain.request();
                Request request = origRequest.newBuilder()
                        .header("Accept-Language", "en,zh-CN,zh")
                        .header("Accept-Charset", "utf-8")
                        .header("x-tzh-mobileheader", AuthStub.instance().toString())
                        .build();
                return chain.proceed(request);
            }
        });

        return okBuilder.build();
    }

}
