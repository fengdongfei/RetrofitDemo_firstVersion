package com.feng.retrofit;

import android.database.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.path;

public class MainActivity extends AppCompatActivity {
    private String path1, path2;
    private File file1, file2;
    private NewsService newsService;
    private TextView st;
    private View start_pics;
    MultipartBody.Part[] file_;
    boolean more=true;//是否是多图片上传

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path1 = "/storage/emulated/0/BGAPhotoPickerDownload/7ee30eacf00760ea0cbffccfa7f1b400.png";
        path2 = "/storage/emulated/0/BGAPhotoPickerDownload/c45c2dd688f90771680d323431e3452f.png";
        file1 = new File(path1);
        file2 = new File(path2);

        st =(TextView) findViewById(R.id.start);
        start_pics=findViewById(R.id.start_picss);

        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate(1,20);
            }
        });

        start_pics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatepic_more();
            }
        });
    }
    private void updatepic_more() {
//        1、创建RequestBody，其中`multipart/form-data`为编码类型
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
//        2、创建`MultipartBody.Part`，其中需要注意第一个参数`fileUpload`需要与服务器对应,也就是`键`
        if (more==false){
            MultipartBody.Part part = MultipartBody.Part.createFormData("fileUpload", file1.getName(), requestFile);
            API.Retrofit().updateImage(part).enqueue(new Callback<String>() {
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
        }else{
            file_ = new MultipartBody.Part[2];
            file_[1] = MultipartBody.Part.createFormData("file", file1.getName(), requestFile);
            file_[0] = MultipartBody.Part.createFormData("file", file2.getName(), requestFile);
            API.Retrofit().updateImage(file_).enqueue(new Callback<String>() {
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

    private void getDate(int page, int rows) {
        newsService = RetrofitFactory.getNewsService();
//        Observable<NewsEntity> observable = newsService.getNewsContent(page, rows);
        //这里的NewsEntity就是接口给你返回的Json解析实体
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
    }
}
