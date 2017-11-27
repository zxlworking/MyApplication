package com.dsv.appstore.http;

import com.dsv.appstore.common.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by uidq0955 on 2017/9/23.
 */

public class HttpApiService {

//    @GET(Constants.SERVER_TAG_RECOMMEND_RECOMMEND_UPDATE)
//    Observable<ResponseBody> checkInstalledApp(@Query("applist") String applist, @Query("cipher") String cipher);
//
//    @GET(Constants.SERVER_TAG_APP_LIST)
//    Observable<HttpResultBean<MPageObject<App>>> getAppList(@Query("type") int type, @Query("page") int page, @Query("ptcode") String ptcode, @Query("cipher") String cipher);
//
//    @GET(Constants.SERVER_TAG_DOWNLOAD_APP_INFO)
//    Observable<HttpResultBean<FtpInfoResultBean>> getDownLoadAppInfo(@Query("id") int appVersionID, @Query("cipher") String cipher);

    public final static int HTTP_RESPONSE_OK     = 0;
    public final static int HTTP_RESPONSE_FAILE  = -1;

    private HttpApiService(){

    }
    //实例话对象
    private static AsyncHttpClient client = new AsyncHttpClient();
    public static AsyncHttpClient getClient(){
        return client;
    }
    static{
        //设置链接超时，如果不设置，默认为10s
        client.setTimeout(15*1000);
    }

    /**
    * 用一个完整url获取一个string对象
    */
    public static void get(String urlString,AsyncHttpResponseHandler res){
        client.get(urlString, res);
    }

    /**
    * url里面带参数
    */
    public static void get(String urlString, RequestParams params, AsyncHttpResponseHandler res){
        client.get(urlString, params,res);
    }

    /**
     * 不带参数，获取json对象或者数组
     */
    public static void get(String urlString,JsonHttpResponseHandler res){
        client.get(urlString, res);
    }

    /**
     * 带参数，获取json对象或者数组
     */
    public static void get(String urlString,RequestParams params,JsonHttpResponseHandler res){
        client.get(urlString, params,res);
    }

    /**
     * 下载数据使用，会返回byte数据
     */
    public static void get(String uString, BinaryHttpResponseHandler bHandler){
        client.get(uString, bHandler);
    }

    /***
     *   最新上架
     */
    public static String getAppListUrl(){
        return Constants.BASE_URL + "/" + Constants.SERVER_TAG_APP_LIST;
    }
    /****
     * APP下载地址
     */
    public static String getDownLoadAppInfoUrl(){
        return Constants.BASE_URL + "/" + Constants.SERVER_TAG_DOWNLOAD_APP_INFO;
    }
    /****
     * 检查已安装的应用是否与服务端对应的服务地址
     */
    public static String checkInstalledAppsVersionUrl(){
        return Constants.BASE_URL + "/" + Constants.SERVER_TAG_CHECK_INSTALLED_APP_INFO;
    }
    /****
     * 检查已安装的应用是否有更新的服务地址
     */
    public static String checkInstalledAppsUpdateUrl(){
        return Constants.BASE_URL + "/" + Constants.SERVER_TAG_CHECK_INSTALLED_APP_UPDATE;
    }
}
