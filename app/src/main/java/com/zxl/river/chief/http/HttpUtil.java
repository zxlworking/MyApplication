package com.zxl.river.chief.http;


import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zxl.river.chief.common.Constants;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;


public class HttpUtil {
    private static final String TAG = "HttpUtil";
    private static Retrofit mRetrofit = null;
    private static HttpApiService mHttpApiService = null;

    static {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
        mHttpApiService = mRetrofit.create(HttpApiService.class);
    }

    public static Subscription test(String key1, String key2, final Handler observeOnHandler, Subscriber<ResponseBody> subscriber){
        Observable<ResponseBody> mObservable = mHttpApiService.test(
                key1,
                key2);
        return mObservable.
                subscribeOn(Schedulers.newThread()).
                //observeOn(AndroidSchedulers.mainThread()).
                observeOn(Schedulers.from(new Executor() {
                    @Override
                    public void execute(@NonNull Runnable command) {
                        observeOnHandler.post(command);
                    }
                })).
                subscribe(subscriber);
    }

    public static Subscription upload(File file, final Handler observeOnHandler, Subscriber<ResponseBody> subscriber){

        //创建RequwstBody对象
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        //创建nickNameBody对象
        RequestBody nickNameBody = RequestBody.create(MediaType.parse("text/plain"), "nickName");


        Observable<ResponseBody> mObservable = mHttpApiService.uploadFile(requestBody,nickNameBody);

        return mObservable.
                subscribeOn(Schedulers.newThread()).
                //observeOn(AndroidSchedulers.mainThread()).
                        observeOn(Schedulers.from(new Executor() {
                    @Override
                    public void execute(@NonNull Runnable command) {
                        observeOnHandler.post(command);
                    }
                })).
                        subscribe(subscriber);
    }

    public static Subscription uploadFiles(List<File> files, final Handler observeOnHandler, Subscriber<ResponseBody> subscriber){
        //组装partMap对象
        Map<String, RequestBody> partMap = new HashMap<>();
        for(File file : files ){
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            partMap.put("file\"; filename=\""+file.getName()+"\"", fileBody);
        }
        RequestBody nickNameBody = RequestBody.create(MediaType.parse("text/plain"), "nickName");
        partMap.put("nickName" , nickNameBody);

        Observable<ResponseBody> mObservable = mHttpApiService.uploadFiles(partMap);

        return mObservable.
                subscribeOn(Schedulers.newThread()).
                //observeOn(AndroidSchedulers.mainThread()).
                        observeOn(Schedulers.from(new Executor() {
                    @Override
                    public void execute(@NonNull Runnable command) {
                        observeOnHandler.post(command);
                    }
                })).
                        subscribe(subscriber);
    }

}