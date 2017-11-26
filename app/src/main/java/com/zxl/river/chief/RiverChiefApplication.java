package com.zxl.river.chief;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.zxl.river.chief.utils.FileUtils;

/**
 * Created by mac on 17-11-25.
 */

public class RiverChiefApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtils.init();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();

        Fresco.initialize(this, config);

    }
}
