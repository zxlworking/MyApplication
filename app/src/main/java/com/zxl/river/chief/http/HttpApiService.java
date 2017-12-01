package com.zxl.river.chief.http;

import com.zxl.river.chief.common.Constants;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by uidq0955 on 2017/9/23.
 */

public interface HttpApiService {

    @GET("echo")
    Observable<ResponseBody> test(@Query("key1") String key1, @Query("key2") String key2);

    @POST("/file")
    @Multipart
    Observable<ResponseBody> uploadFile(@Part("file\"; filename=\"avatar.png\"") RequestBody file, @Part("nickName") RequestBody nickName);

    @Multipart
    @POST("Test")
    Observable<ResponseBody> uploadFiles(@PartMap Map<String, RequestBody> params);

}
