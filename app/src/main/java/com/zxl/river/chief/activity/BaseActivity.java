package com.zxl.river.chief.activity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zxl.river.chief.R;
import com.zxl.river.chief.utils.ActivityManager;
import com.zxl.river.chief.utils.CommonUtils;

/**
 * Created by mac on 17-11-24.
 */

public abstract class BaseActivity extends Activity {

    public Context mContext;

    public Activity mActivity;

    private TextView mTitleTv;

    private ImageView mTitleBarLeftImg;
    private ImageView mTitleBarRightImg;

    private SimpleDraweeView mShowBigImg;

    public View.OnClickListener mOnClickListener = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        ActivityManager.getInstance().addActivity(this);

        mContext = this;
        mActivity = this;

        initView(savedInstanceState);
        initData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }

    public abstract int getContentView();

    public void initView(Bundle savedInstanceState){
        initView();

    }

    public void initView(){
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mTitleBarLeftImg = (ImageView) findViewById(R.id.title_bar_left_img);
        mTitleBarRightImg = (ImageView) findViewById(R.id.title_bar_right_img);

        mShowBigImg = (SimpleDraweeView) findViewById(R.id.show_big_img);

        if(mShowBigImg != null){
            mShowBigImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideBigImg();
                }
            });
        }
    }

    public void initData(){
        if(mTitleBarLeftImg != null){
            mTitleBarLeftImg.setOnClickListener(mOnClickListener);
        }
        if(mTitleBarRightImg != null){
            mTitleBarRightImg.setOnClickListener(mOnClickListener);
        }
    }

    public void setTitle(String title){
        if(mTitleTv != null){
            mTitleTv.setText(title);
        }
    }

    public void setLeftImgVisibility(int v){
        if(mTitleBarLeftImg != null){
            mTitleBarLeftImg.setVisibility(v);
        }
    }

    public void setRightImgVisibility(int v){
        if(mTitleBarRightImg != null){
            mTitleBarRightImg.setVisibility(v);
        }
    }

    public void setRightImgRes(int resId){
        if(mTitleBarRightImg != null){
            mTitleBarRightImg.setVisibility(View.VISIBLE);
            mTitleBarRightImg.setImageResource(resId);
        }
    }

    public void showBigImgByLocalFile(String path){
        if(mShowBigImg != null){
            mShowBigImg.setVisibility(View.VISIBLE);
            int mWidth = CommonUtils.getScreenWidth(mContext);
            int mHeight = CommonUtils.getScreenHeight(mContext);
            if(mWidth > 0 && mHeight > 0){
                Uri mUri = Uri.parse("file://" + path);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(mUri)
                        .setResizeOptions(new ResizeOptions(mWidth,mHeight))
                        .build();

                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(mShowBigImg.getController())
                        .setControllerListener(new BaseControllerListener<ImageInfo>())
                        .build();
                mShowBigImg.setController(controller);
            }
        }
    }

    public void showBigImgByRes(int resId){
        if(mShowBigImg != null) {
            mShowBigImg.setVisibility(View.VISIBLE);
            int mWidth = CommonUtils.getScreenWidth(mContext);
            int mHeight = CommonUtils.getScreenHeight(mContext);
            if(mWidth > 0 && mHeight > 0){
                Uri mUri = Uri.parse("res://" + mContext.getPackageName() + "/" + resId);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(mUri)
                        .setResizeOptions(new ResizeOptions(mWidth,mHeight))
                        .build();

                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(mShowBigImg.getController())
                        .setControllerListener(new BaseControllerListener<ImageInfo>())
                        .build();
                mShowBigImg.setController(controller);
            }
        }
    }

    public void showBigImgByUrl(String url){
        if(mShowBigImg != null){
            mShowBigImg.setVisibility(View.VISIBLE);
            mShowBigImg.setImageURI(url);
        }
    }

    public void hideBigImg(){
        if(mShowBigImg != null){
            mShowBigImg.setVisibility(View.GONE);
        }
    }

    private void setSimpleDraweeViewLocalPath(int mWidth,int mHeight, SimpleDraweeView simpleDraweeViewRes,String path){
        if(mWidth > 0 && mHeight > 0){
            Uri mUri = Uri.parse("file://" + path);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(mUri)
                    .setResizeOptions(new ResizeOptions(mWidth,mHeight))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(simpleDraweeViewRes.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            simpleDraweeViewRes.setController(controller);
        }
    }

    private void setSimpleDraweeViewRes(int mWidth,int mHeight, SimpleDraweeView simpleDraweeViewRes,int resId){
        if(mWidth > 0 && mHeight > 0){
            Uri mUri = Uri.parse("res://" + mContext.getPackageName() + "/" + resId);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(mUri)
                    .setResizeOptions(new ResizeOptions(mWidth,mHeight))
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(simpleDraweeViewRes.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>())
                    .build();
            simpleDraweeViewRes.setController(controller);
        }
    }
}
