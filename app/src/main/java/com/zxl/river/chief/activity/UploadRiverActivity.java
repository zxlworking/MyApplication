package com.zxl.river.chief.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zxl.river.chief.common.Constants;
import com.zxl.river.chief.utils.CommonUtils;
import com.zxl.river.chief.utils.DebugUtils;
import com.zxl.river.chief.utils.PhotoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zxl.river.chief.utils.PhotoUtils.CAMERA_PERMISSIONS_REQUEST_CODE;
import static com.zxl.river.chief.utils.PhotoUtils.CODE_CAMERA_REQUEST;
import static com.zxl.river.chief.utils.PhotoUtils.CODE_GALLERY_REQUEST;
import static com.zxl.river.chief.utils.PhotoUtils.CODE_RESULT_REQUEST;
import static com.zxl.river.chief.utils.PhotoUtils.STORAGE_PERMISSIONS_REQUEST_CODE;

/**
 * Created by mac on 17-11-26.
 */

public class UploadRiverActivity extends BaseActivity {
    private static final String TAG = "UploadRiverActivity";

    public static final String EXTRA_RIVER_START_TIME = "EXTRA_RIVER_START_TIME";
    public static final String EXTRA_RIVER_END_TIME = "EXTRA_RIVER_END_TIME";

    private ImageView mBackImg;
    private ImageView mSettingsImg;

    private TextView mTitleTv;

    private TextView mRiverStartTimeTv;
    private TextView mRiverEndTimeTv;

    private SimpleDateFormat mRiverTimeFormat = new SimpleDateFormat("MM月dd日 HH:mm");

    private GridView mRiverPictureGridView;
    private RiverPictureAdapter mRiverPictureAdapter;
    private List<String> mRiverPicturePaths = new ArrayList<>();

    private LinearLayout mAddPictureTipsLl;
    private TextView mAddPictureFromAlbumTv;
    private TextView mAddPictureFromCameraTv;
    private TextView mCancelAddPictureTv;

    private String mCurrentPhotoPath = "";

    @Override
    public int getContentView() {
        return R.layout.upload_river_activity;
    }

    @Override
    public void initView() {
        mBackImg = (ImageView) findViewById(R.id.back_img);
        mSettingsImg = (ImageView) findViewById(R.id.settings_img);

        mTitleTv = (TextView) findViewById(R.id.title_tv);

        mAddPictureTipsLl = (LinearLayout) findViewById(R.id.add_picture_tips_ll);
        mAddPictureFromAlbumTv = (TextView) findViewById(R.id.add_picture_from_album_tv);
        mAddPictureFromCameraTv = (TextView) findViewById(R.id.add_picture_from_camera_tv);
        mCancelAddPictureTv = (TextView) findViewById(R.id.cancel_add_picture_tv);

        mRiverPictureGridView = (GridView) findViewById(R.id.river_picture_grid_view);

        mSettingsImg.setVisibility(View.GONE);
        mTitleTv.setText("提交巡河信息");

        mRiverStartTimeTv = (TextView) findViewById(R.id.river_start_time_tv);
        mRiverEndTimeTv = (TextView) findViewById(R.id.river_end_time_tv);

        mBackImg.setOnClickListener(mOnClickListener);
        mAddPictureFromAlbumTv.setOnClickListener(mOnClickListener);
        mAddPictureFromCameraTv.setOnClickListener(mOnClickListener);
        mCancelAddPictureTv.setOnClickListener(mOnClickListener);

    }

    @Override
    public void initData() {
        Bundle mBundle = getIntent().getExtras();
        long mRiverStartTime = 0;
        long mRiverEndTime = 0;
        if(mBundle != null){
            mRiverStartTime = mBundle.getLong(EXTRA_RIVER_START_TIME);
            mRiverEndTime = mBundle.getLong(EXTRA_RIVER_END_TIME);
        }
        mRiverStartTimeTv.setText(mRiverTimeFormat.format(mRiverStartTime > 0 ? new Date(mRiverStartTime) : new Date()));
        mRiverEndTimeTv.setText(mRiverTimeFormat.format(mRiverEndTime > 0 ? new Date(mRiverEndTime) : new Date()));

        mRiverPictureAdapter = new RiverPictureAdapter();
        mRiverPictureGridView.setAdapter(mRiverPictureAdapter);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back_img:
                    finish();
                    break;
                case R.id.add_picture_from_album_tv:
                    if(CommonUtils.checkStoragePermission(mActivity)){
                        PhotoUtils.openPic(mActivity,CODE_GALLERY_REQUEST);
                    }
                    mAddPictureTipsLl.setVisibility(View.GONE);
                    break;
                case R.id.add_picture_from_camera_tv:
                    if(PhotoUtils.checkCameraPermission(mActivity)){
                        generatePhotoPath();
                        PhotoUtils.takePicture(mActivity,mCurrentPhotoPath);
                    }
                    mAddPictureTipsLl.setVisibility(View.GONE);
                    break;
                case R.id.cancel_add_picture_tv:
                    mAddPictureTipsLl.setVisibility(View.GONE);
                    break;
            }
        }
    };

    class RiverPictureAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mRiverPicturePaths.size()+1;
        }

        @Override
        public String getItem(int position) {
            return mRiverPicturePaths.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(null == convertView){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.upload_river_picture_item_view,null);
            }

            SimpleDraweeView mSimpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.upload_river_picture_item_img);

            if(position == getCount() - 1){
                mSimpleDraweeView.setImageResource(R.drawable.ic_add_picture);
                mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAddPictureTipsLl.setVisibility(View.VISIBLE);
                    }
                });
            }else{
                //Uri.parse("file://" + MyBimp.tempSelectBitmap.get(position).path))

                /*
                mSimpleDraweeView.setDownsampleEnabled(true);
                Uri mUri = Uri.parse("file://" + mEventPicturePaths.get(position));
                mSimpleDraweeView.setImageURI(mUri);
                mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEventPicturePaths.remove(position);
                        notifyDataSetChanged();
                    }
                });
                */


                int mWidth = mSimpleDraweeView.getWidth();
                int mHeight = mSimpleDraweeView.getHeight();
                if(mWidth > 0 && mHeight > 0){
                    Uri mUri = Uri.parse("file://" + mRiverPicturePaths.get(position));
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(mUri)
                            .setResizeOptions(new ResizeOptions(mWidth,mHeight))
                            .build();

                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(mSimpleDraweeView.getController())
                            .setControllerListener(new BaseControllerListener<ImageInfo>())
                            .build();
                    mSimpleDraweeView.setController(controller);
                }

            }

            return convertView;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (CommonUtils.hasSdcard()) {
                        generatePhotoPath();
                        DebugUtils.d(TAG,"onRequestPermissionsResult::CAMERA_PERMISSIONS_REQUEST_CODE::mCurrentPhotoPath = " + mCurrentPhotoPath);
                        PhotoUtils.takePicture(mActivity,mCurrentPhotoPath);
                    }else {
                        CommonUtils.showMessage(mContext, "设备没有SD卡！");
                    }
                }else {
                    CommonUtils.showMessage(mContext, "请允许打开相机!");
                }
                break;
            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(mActivity, CODE_GALLERY_REQUEST);
                } else {
                    CommonUtils.showMessage(mContext, "请允许打操作SDCard!");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    DebugUtils.d(TAG,"onActivityResult::CODE_CAMERA_REQUEST::mCurrentPhotoPath = " + mCurrentPhotoPath);
                    if(data != null){
                        DebugUtils.d(TAG,"onActivityResult::CODE_CAMERA_REQUEST::data = " + data.getData());
                    }
                    mRiverPicturePaths.add(0,mCurrentPhotoPath);
                    mRiverPictureAdapter.notifyDataSetChanged();
                    /*
                    generateCropPhotoPath();
                    PhotoUtils.cropImageUri(this, mCurrentPhotoPath, mCurrentCropPhotoPath, 1, 1, mOutPutX, mOutPutY, CODE_RESULT_REQUEST);
                    */
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    DebugUtils.d(TAG,"onActivityResult::CODE_GALLERY_REQUEST::mCurrentPhotoPath = " + mCurrentPhotoPath);
                    if(data != null){
                        DebugUtils.d(TAG,"onActivityResult::CODE_GALLERY_REQUEST::data = " + data.getData());
                        if (CommonUtils.hasSdcard()) {
                            Uri newUri = Uri.parse(PhotoUtils.getPath(mActivity, data.getData()));
                            String mNewPath = newUri.getPath();
                            DebugUtils.d(TAG,"onActivityResult::CODE_GALLERY_REQUEST::path = " + newUri.getPath());

                            mRiverPicturePaths.add(0,mNewPath);
                            mRiverPictureAdapter.notifyDataSetChanged();
                            /*
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                newUri = FileProvider.getUriForFile(this, Constants.FILEPROVIDER_AUTHORITIES, new File(newUri.getPath()));
                            }
                            PhotoUtils.cropImageUri(this, newUri, Uri.fromFile(new File(mCurrentCropPhotoPath)), 1, 1, mOutPutX, mOutPutY, CODE_RESULT_REQUEST);
                            */
                        }else {
                            CommonUtils.showMessage(mContext, "设备没有SD卡！");
                        }
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    DebugUtils.d(TAG,"onActivityResult::CODE_RESULT_REQUEST::data = " + data.getData());
                    break;
            }
        }
    }

    private void generatePhotoPath(){
        mCurrentPhotoPath = Constants.APP_PHOTO_DIR + System.currentTimeMillis() + ".jpg";
    }
}
