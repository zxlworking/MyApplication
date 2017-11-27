package com.dsv.appstore.http;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import com.dsv.appstore.bean.InstalledAppBean;
import com.dsv.appstore.common.DESUtil;
import com.dsv.appstore.common.DebugUtils;
import com.dsv.appstore.http.data.App;
import com.dsv.appstore.http.data.AppListRequestBean;
import com.dsv.appstore.http.data.DownLoadAppInfoRequestBean;
import com.dsv.appstore.http.data.FtpInfoResultBean;
import com.dsv.appstore.http.data.HttpResultBean;
import com.dsv.appstore.http.data.MPageObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.dsv.appstore.http.HttpApiService.HTTP_RESPONSE_FAILE;
import static com.dsv.appstore.http.HttpApiService.HTTP_RESPONSE_OK;


public class HttpUtil {
    private static final String TAG = "HttpUtil";
//    private static Retrofit mRetrofit = null;
//    private static HttpApiService mHttpApiService = null;
//
//    static {
//        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .build();
//        Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//        mRetrofit = new Retrofit.Builder()
//                .client(mOkHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(mGson))
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl(Constants.BASE_URL)
//                .build();
//        mHttpApiService = mRetrofit.create(HttpApiService.class);
//    }
//
//    public static Subscription checkInstalledApp(String applist, String cipher, Subscriber<ResponseBody> subscriber){
//        Observable<ResponseBody> mObservable = mHttpApiService.checkInstalledApp(
//                applist,
//                cipher);
//        return mObservable.
//                subscribeOn(Schedulers.newThread()).
//                observeOn(AndroidSchedulers.mainThread()).
//                subscribe(subscriber);
//    }
//
//    public static Subscription getAppList(AppListRequestBean appListRequestBean, Subscriber<HttpResultBean<MPageObject<App>>> subscriber) {
//        Observable<HttpResultBean<MPageObject<App>>> mObservable = mHttpApiService.getAppList(
//                appListRequestBean.getType(),
//                appListRequestBean.getPage(),
//                appListRequestBean.getPtcode(),
//                appListRequestBean.getCipher());
//        return mObservable.
//                subscribeOn(Schedulers.newThread()).
//                observeOn(AndroidSchedulers.mainThread()).
//                subscribe(subscriber);
//    }
//
//    public static Subscription getDownLoadAppInfo(int appVersionID, String cipher, Subscriber<HttpResultBean<FtpInfoResultBean>> subscriber){
//        Observable<HttpResultBean<FtpInfoResultBean>> mObservable = mHttpApiService.getDownLoadAppInfo(appVersionID,cipher);
//        return mObservable.
//                subscribeOn(Schedulers.newThread()).
//                observeOn(AndroidSchedulers.mainThread()).
//                subscribe(subscriber);
//    }

    /**
     * 请求应用列表
     */
    public static void getAppList(AppListRequestBean appListRequestBean, final Handler callBack){
        RequestParams params = new RequestParams();
        params.put("type", appListRequestBean.getType());
        params.put("page", appListRequestBean.getPage());
        params.put("ptcode", appListRequestBean.getPtcode());
        params.put("cipher", appListRequestBean.getCipher());
        HttpApiService.get(HttpApiService.getAppListUrl(), params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                        callBack.sendEmptyMessage(HTTP_RESPONSE_FAILE);
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        String target = new String(arg2);
                        DebugUtils.d(TAG,"getAppList::onSuccess::target = " + target);


                        Type mAppType = new ParameterizedTypeImpl(App.class, null);
                        Type mMPageObjectType = new ParameterizedTypeImpl(MPageObject.class, new Type[]{mAppType});
                        Type mHttpResultBeanType = new ParameterizedTypeImpl(HttpResultBean.class, new Type[]{mMPageObjectType});

                        Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        HttpResultBean<MPageObject<App>> mHttpResultBean = mGson.fromJson(target,mHttpResultBeanType);

                        if (mHttpResultBean.getResponsedata().getStatus().equals("0000")) {
                            Message msg = new Message();
                            msg.what = HTTP_RESPONSE_OK;
                            msg.obj = mHttpResultBean;
                            callBack.sendMessage(msg);
                        }else{
                            Message msg = new Message();
                            msg.what = HTTP_RESPONSE_FAILE;
                            msg.obj = mHttpResultBean.getResponsedata().getStatus();
                            callBack.sendMessage(msg);
                        }
                    }
                });
    }

    /**
     * 请求app下载信息
     */
    public static void getDownLoadAppInfo(DownLoadAppInfoRequestBean downLoadAppInfoRequestBean, final Handler callBack){
        RequestParams params = new RequestParams();
        params.put("id", downLoadAppInfoRequestBean.getAppVersionID());
        params.put("cipher", downLoadAppInfoRequestBean.getCipher());
        HttpApiService.get(HttpApiService.getDownLoadAppInfoUrl(), params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                        callBack.sendEmptyMessage(HTTP_RESPONSE_FAILE);
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        String target = new String(arg2);
                        DebugUtils.d(TAG,"getDownLoadAppInfo::onSuccess::target = " + target);


                        Type mFtpInfoResultBeanType = new ParameterizedTypeImpl(FtpInfoResultBean.class, null);
                        Type mHttpResultBeanType = new ParameterizedTypeImpl(HttpResultBean.class, new Type[]{mFtpInfoResultBeanType});

                        Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        HttpResultBean<FtpInfoResultBean> mHttpResultBean = mGson.fromJson(target,mHttpResultBeanType);

                        if (mHttpResultBean.getResponsedata().getStatus().equals("0000")) {
                            Message msg = new Message();
                            msg.what = HTTP_RESPONSE_OK;
                            msg.obj = mHttpResultBean;
                            callBack.sendMessage(msg);
                        }else{
                            Message msg = new Message();
                            msg.what = HTTP_RESPONSE_FAILE;
                            msg.obj = mHttpResultBean.getResponsedata().getStatus();
                            callBack.sendMessage(msg);
                        }
                    }
                });
    }

    /**
     * 将本地安装的app与服务端比较
     */
    public static void checkInstalledAppsVersion(List<InstalledAppBean> installedAppBeens, final Handler callBack) {
        RequestParams params = new RequestParams();
        JSONArray applist = new JSONArray();
        for (InstalledAppBean app : installedAppBeens) {
            JSONObject appJson = new JSONObject();
            try {
                appJson.put("packagename", app.getPackageName());
                appJson.put("versioncode", app.getVersionCode());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            applist.put(appJson);
        }
        params.put("applist", applist);
        String cipher = DESUtil.httpEncryption();
        params.put("cipher", cipher);
        HttpApiService.get(HttpApiService.checkInstalledAppsVersionUrl(), params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        callBack.sendEmptyMessage(HTTP_RESPONSE_FAILE);
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        String target = new String(arg2);
                        DebugUtils.d(TAG,"checkInstalledAppsVersion::onSuccess::target = " + target);

                        Type mAppType = new ParameterizedTypeImpl(App.class, null);
                        //Type mMPageObjectType = new ParameterizedTypeImpl(MPageObject.class, new Type[]{mAppType});
                        Type mHttpResultBeanType = new ParameterizedTypeImpl(HttpResultBean.class, new Type[]{mAppType});

                        Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        HttpResultBean<App> mHttpResultBean = mGson.fromJson(target,mHttpResultBeanType);

                        if (mHttpResultBean.getResponsedata().getStatus().equals("0000")) {
                            Message msg = new Message();
                            msg.what = HTTP_RESPONSE_OK;
                            msg.obj = mHttpResultBean;
                            callBack.sendMessage(msg);
                        }else{
                            Message msg = new Message();
                            msg.what = HTTP_RESPONSE_FAILE;
                            msg.obj = mHttpResultBean.getResponsedata().getStatus();
                            callBack.sendMessage(msg);
                        }
                    }

                });
    }

    /**
     * 将本地安装的app与服务端比较
     */
    public static void checkInstalledAppsUpdate(List<InstalledAppBean> installedAppBeens, final Handler callBack) {
        RequestParams params = new RequestParams();
        JSONArray applist = new JSONArray();
        for (InstalledAppBean app : installedAppBeens) {
            JSONObject appJson = new JSONObject();
            try {
                appJson.put("packagename", app.getPackageName());
                appJson.put("versioncode", app.getVersionCode());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            applist.put(appJson);
        }
        params.put("applist", applist);
        String cipher = DESUtil.httpEncryption();
        params.put("cipher", cipher);
        HttpApiService.get(HttpApiService.checkInstalledAppsUpdateUrl(), params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        callBack.sendEmptyMessage(HTTP_RESPONSE_FAILE);
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        String target = new String(arg2);
                        DebugUtils.d(TAG,"checkInstalledAppsUpdate::onSuccess::target = " + target);

                        Type mAppType = new ParameterizedTypeImpl(App.class, null);
                        //Type mMPageObjectType = new ParameterizedTypeImpl(MPageObject.class, new Type[]{mAppType});
                        Type mHttpResultBeanType = new ParameterizedTypeImpl(HttpResultBean.class, new Type[]{mAppType});

                        Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        HttpResultBean<App> mHttpResultBean = mGson.fromJson(target,mHttpResultBeanType);

                        if (mHttpResultBean.getResponsedata().getStatus().equals("0000")) {
                            Message msg = new Message();
                            msg.what = HTTP_RESPONSE_OK;
                            msg.obj = mHttpResultBean;
                            callBack.sendMessage(msg);
                        }else{
                            Message msg = new Message();
                            msg.what = HTTP_RESPONSE_FAILE;
                            msg.obj = mHttpResultBean.getResponsedata().getStatus();
                            callBack.sendMessage(msg);
                        }
                    }

                });
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;

        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override public Type getOwnerType() {
            return null;
        }
    }

    /**
     * 检查当前网络是否可用
     * @param context
     * @return true网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null){
            return false;
        }

        // 获取NetworkInfo对象
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        if (networkInfo != null && networkInfo.length > 0) {
            for (int i = 0; i < networkInfo.length; i++) {
                // 判断当前网络状态是否为连接状态
                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }
}