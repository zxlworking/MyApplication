package com.dsv.appstore.http.data;

/**
 * Created by uidq0955 on 2017/10/10.
 */

public class DownLoadAppInfoRequestBean extends HttpRequestBaseBean {
    private int appVersionID;

    public int getAppVersionID() {
        return appVersionID;
    }

    public void setAppVersionID(int appVersionID) {
        this.appVersionID = appVersionID;
    }
}
