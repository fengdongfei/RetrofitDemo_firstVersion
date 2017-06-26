package com.feng.retrofit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.feng.retrofit.api.service.NewsService;
import com.feng.retrofit.api.service.UpLoadService;
import com.feng.retrofit.model.AdSetModel;
import com.feng.retrofit.model.NewsModel;
import com.feng.retrofit.model.UserModel;
import com.feng.retrofit.retrofit.RetrofitFactory;
import com.feng.retrofit.utils.MD5;
import com.feng.retrofit.utils.MFileUtils;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int TAKE_PICTURE = 101;
    private File file1, file2;
    private TextView st;
    private View start_pics;
    MultipartBody.Part[] file_;
    boolean more = true;//是否是多图片上传
    private File mTmpFile;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        st = (TextView) findViewById(R.id.start);
        login=(TextView)findViewById(R.id.login);
        start_pics = findViewById(R.id.start_picss);

        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate(1, 20);
//                getads();
            }
        });

        start_pics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeCapture();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login() {
        HashMap map=new HashMap();
        map.put("mobilePhone","13122277856");
        map.put("password", MD5.encrypt32("123456"));
        map.put("textPinNum","");
        Call<UserModel> call = RetrofitFactory.getInstance().create(NewsService.TzService.class).login(map);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                //  请求数据成功
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                // 请求数据失败
                Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatepic_more() {
//        1、创建RequestBody，其中`multipart/form-data`为编码类型
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
//        2、创建`MultipartBody.Part`，其中需要注意第一个参数`fileUpload`需要与服务器对应,也就是`键`
        if (more == false) {
            MultipartBody.Part part = MultipartBody.Part.createFormData("fileUpload", file1.getName(), requestFile);
            RetrofitFactory.getInstance().create(UpLoadService.class).updateImage(part).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, response.code() + "", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            file_ = new MultipartBody.Part[2];
            file_[1] = MultipartBody.Part.createFormData("file", file1.getName(), requestFile);
            file_[0] = MultipartBody.Part.createFormData("file", file2.getName(), requestFile);
            RetrofitFactory.getInstance().create(UpLoadService.class).updateImage(file_).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, response.code() + "", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * GET请求
     * @param page
     * @param rows
     */
    private void getDate(int page, int rows) {
        //这里的NewsEntity就是接口给你返回的Json解析实体
        Call<NewsModel> call = RetrofitFactory.getInstance().create(NewsService.class).getNewsContent(page, rows);
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                //  请求数据成功
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                st.setText(response.code() + " " + response.message() + " " + response.body().getTotal() + "  " + response.body().getTngou().get(0).getDescription());
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                // 请求数据失败
                Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 模拟淘智慧banner接口（GET）
     */
    public void getads(){
        Call<AdSetModel> call = RetrofitFactory.getInstance().create(NewsService.TzService.class).fetchAds();
        call.enqueue(new Callback<AdSetModel>() {
            @Override
            public void onResponse(Call<AdSetModel> call, Response<AdSetModel> response) {
                //  请求数据成功
                Toast.makeText(MainActivity.this, "success"+response.body().A.get(0).title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AdSetModel> call, Throwable t) {
                // 请求数据失败
                Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void takeCapture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            mTmpFile = null;
            mTmpFile = MFileUtils.createTmpFile(MainActivity.this);
            if (mTmpFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                startActivityForResult(takePictureIntent, TAKE_PICTURE);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {//拍照片
            if (mTmpFile != null) {
                file1 = new File(mTmpFile.getAbsolutePath());
                file2 = new File(mTmpFile.getAbsolutePath());
                updatepic_more();
            }
        } else {
            if (mTmpFile != null && mTmpFile.exists()) {
                mTmpFile.delete();
            }
        }
    }
}
