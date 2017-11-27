package com.dsv.appstore.http.data;

/**
 * Created by uidq0955 on 2017/9/26.
 */

public class HttpResultBean<T> {
    private MResponseData<T> responsedata;

    public MResponseData<T> getResponsedata() {
        return responsedata;
    }

    public void setResponsedata(MResponseData<T> responsedata) {
        this.responsedata = responsedata;
    }

    @Override
    public String toString() {
        return "HttpResultBean{" +
                "responsedata=" + responsedata +
                '}';
    }
}
