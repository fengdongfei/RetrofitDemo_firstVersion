package com.feng.retrofit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.feng.retrofit.api.response.AuthStub;
import com.feng.retrofit.api.response.MCallback;
import com.feng.retrofit.api.response.MResponse;
import com.feng.retrofit.api.service.NewsService;
import com.feng.retrofit.api.service.UpLoadService;
import com.feng.retrofit.model.AdSetModel;
import com.feng.retrofit.model.BModel;
import com.feng.retrofit.model.CHitNotifyModel;
import com.feng.retrofit.model.NewsModel;
import com.feng.retrofit.model.RCFileModel;
import com.feng.retrofit.model.UnFollowModel;
import com.feng.retrofit.model.UserModel;
import com.feng.retrofit.retrofit.RetrofitFactory;
import com.feng.retrofit.utils.MD5;
import com.feng.retrofit.utils.MFileUtils;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int TAKE_PICTURE = 101;
    @BindView(R.id.start)
    TextView start;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.addinto)
    TextView addinto;
    @BindView(R.id.start_picss)
    TextView startPicss;
    @BindView(R.id.delinto)
    TextView delinto;

    private File file1, file2;
    MultipartBody.Part[] file_;
    boolean more = true;//是否是多图片上传
    private File mTmpFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    /**
     * 模仿淘智慧登陆（POST）
     */
    private void Login() {
        HashMap map = new HashMap();
        map.put("mobilePhone", "18298192053");
        map.put("password", MD5.encrypt32("123456"));
        map.put("textPinNum", "");
        RetrofitFactory.getInstance()
                .create(NewsService.TzService.class)
                .login(map)
                .enqueue(new MCallback<MResponse<UserModel>>(this) {
                    @Override
                    protected void onSuccess(MResponse<UserModel> result) {
                        AuthStub.instance().token = result.result.token;
                        Toast.makeText(MainActivity.this, "success" + result.result.nickName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onFail(int errorCode, String errorInfo) {
                        Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 图片的上传--暂不可用
     */
    private void updatepicMore() {
//        1、创建RequestBody，其中`multipart/form-data`为编码类型
        RequestBody requestFile = RequestBody.create(MediaType.parse(""), file1);//multipart/form-data
//        2、创建`MultipartBody.Part`，其中需要注意第一个参数`fileUpload`需要与服务器对应,也就是`键`
        if (more == false) {
            MultipartBody.Part part = MultipartBody.Part.createFormData("uploadfile", file1.getName(), requestFile);//fileUpload
            RetrofitFactory.getInstance().create(UpLoadService.class)
                    .updateImage(part)
                    .enqueue(new Callback<String>() {
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
     *
     * @param page
     * @param rows
     */
    private void getDate(int page, int rows) {
        //这里的NewsEntity就是接口给你返回的Json解析实体
        RetrofitFactory.getInstance()
                .create(NewsService.class)
                .getNewsContent(page, rows)
                .enqueue(new MCallback<MResponse<NewsModel>>(this) {
                    @Override
                    protected void onSuccess(MResponse<NewsModel> result) {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                        start.setText(result.code() + " " + result.message() + " " + result.result + "  ");
                    }

                    @Override
                    protected void onFail(int errorCode, String errorInfo) {
                        Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
                    }

                });

    }

    /**
     * 模拟淘智慧banner接口（GET）
     */
    public void getads() {
        RetrofitFactory.getInstance()
                .create(NewsService.TzService.class)
                .fetchAds()
                .enqueue(new MCallback<MResponse<AdSetModel>>(this) {
                    @Override
                    protected void onSuccess(MResponse<AdSetModel> result) {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onFail(int errorCode, String errorInfo) {
                        Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {//拍照片
            if (mTmpFile != null) {
                file1 = new File(mTmpFile.getAbsolutePath());
                file2 = new File(mTmpFile.getAbsolutePath());
                RCFileModel.upload(file1, "img");
//                updatepicMore();
            }
        } else {
            if (mTmpFile != null && mTmpFile.exists()) {
                mTmpFile.delete();
            }
        }
    }

    /**
     * 进入相册拍照
     */
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

    @OnClick({R.id.start, R.id.login, R.id.addinto, R.id.start_picss,R.id.delinto})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                getDate(1, 20);
                getads();
                break;
            case R.id.login:
                Login();
                break;
            case R.id.addinto:
                Notify();
                break;
            case R.id.start_picss:
                takeCapture();
                break;
            case R.id.delinto:
                delInfo();
                break;
        }
    }

    /**
     * del
     */
    private void delInfo() {
        UnFollowModel model = new UnFollowModel();
        model.attentionSid = 59;
        RetrofitFactory.getInstance().create(NewsService.TzService.class)
                .unFollow(model)
                .enqueue(new MCallback<MResponse<BModel>>(this) {
                    @Override
                    protected void onSuccess(MResponse<BModel> result) {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onFail(int errorCode, String errorInfo) {
                        Toast.makeText(MainActivity.this, "onFail", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 淘智慧接口测试(PUT)
     */
    private void Notify() {
        CHitNotifyModel model = new CHitNotifyModel();
        model.contentSid = 500072;
        RetrofitFactory.getInstance().create(NewsService.TzService.class)
                .notifyHit(model)
                .enqueue(new MCallback<MResponse<CHitNotifyModel>>(MApplication.getContext()) {

                    @Override
                    protected void onSuccess(MResponse<CHitNotifyModel> result) {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onFail(int errorCode, String errorInfo) {
                        Toast.makeText(MainActivity.this, "onFail", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
