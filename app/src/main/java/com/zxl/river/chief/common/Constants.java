package com.zxl.river.chief.common;

import android.os.Environment;

/**
 * Created by mac on 17-11-24.
 */

public class Constants {

    public static final String PREFERENCE_NAME = "river_chief";

    public static final String APP_DIR = Environment.getExternalStorageDirectory().getPath();
    public static final String APP_PHOTO_DIR = APP_DIR + "/photo/";

    public static final String FILEPROVIDER_AUTHORITIES = "com.zxl.river.chief";

    public static final String BASE_URL = "http://10.218.7.30:8080//test_web/";
    //public static final String BASE_URL = "http://122.152.195.124:8080/appstore/sys/";

    public static final String[] EVENT_TYPE_NAME = new String[]{"地质灾害","违规排污","河道垃圾"};
    public static final String[] EVENT_STATE_NAME = new String[]{"未处理","处理中","已处理"};

    public static final int LOCATION_STATE = 1;

    public static final String ACTION_GET_LOCATION_PERMISSION = "ACTION_GET_LOCATION_PERMISSION";
}
