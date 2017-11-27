package com.dsv.appstore.http.data;

/**
 * Created by uidq0955 on 2017/9/26.
 */

public class AppListRequestBean extends HttpRequestBaseBean {
    private int type;
    private int page;
    private String ptcode;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getPtcode() {
        return ptcode;
    }

    public void setPtcode(String ptcode) {
        this.ptcode = ptcode;
    }


    @Override
    public String toString() {
        return "AppListRequestBean{" +
                "type=" + type +
                ", page=" + page +
                ", ptcode='" + ptcode + '\'' +
                '}';
    }
}
