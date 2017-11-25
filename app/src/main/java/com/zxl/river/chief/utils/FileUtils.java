package com.zxl.river.chief.utils;

import com.zxl.river.chief.common.Constants;

import java.io.File;

/**
 * Created by mac on 17-11-25.
 */

public class FileUtils {
    public static void init(){
        File mAppDir = new File(Constants.APP_DIR);
        if(!mAppDir.exists()){
            mAppDir.mkdirs();
        }
        File mAppPhotoDir = new File(Constants.APP_PHOTO_DIR);
        if(!mAppPhotoDir.exists()){
            mAppPhotoDir.mkdirs();
        }
    }
}
