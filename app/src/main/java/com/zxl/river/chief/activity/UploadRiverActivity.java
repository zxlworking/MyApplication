package com.zxl.river.chief.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zxl.river.chief.common.BaseViewHolder;
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


    private SimpleDateFormat mRiverTimeFormat = new SimpleDateFormat("MM月dd日 HH:mm");

    private RecyclerView mUploadRiverRecyclerView;
    private UploadEventAdapter mUploadEventAdapter;

    private List<String> mRiverPicturePaths = new ArrayList<>();

    private LinearLayout mAddPictureTipsLl;
    private TextView mAddPictureFromAlbumTv;
    private TextView mAddPictureFromCameraTv;
    private TextView mCancelAddPictureTv;

    private String mCurrentPhotoPath = "";

    private int mPictureWidth = 0;
    private int mPictureHeight = 0;

    private long mRiverStartTime = 0;
    private long mRiverEndTime = 0;

    private boolean isDeletePictureMode = false;

    @Override
    public int getContentView() {
        return R.layout.upload_river_activity;
    }

    @Override
    public void initView() {
        super.initView();
        mAddPictureTipsLl = (LinearLayout) findViewById(R.id.add_picture_tips_ll);
        mAddPictureFromAlbumTv = (TextView) findViewById(R.id.add_picture_from_album_tv);
        mAddPictureFromCameraTv = (TextView) findViewById(R.id.add_picture_from_camera_tv);
        mCancelAddPictureTv = (TextView) findViewById(R.id.cancel_add_picture_tv);

        mUploadRiverRecyclerView = (RecyclerView) findViewById(R.id.upload_river_recycler_view);
        mUploadRiverRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mUploadEventAdapter = new UploadEventAdapter();
        mUploadRiverRecyclerView.setAdapter(mUploadEventAdapter);

        setRightImgVisibility(View.GONE);
        setTitle("提交巡河信息");

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.title_bar_left_img:
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

        mAddPictureFromAlbumTv.setOnClickListener(mOnClickListener);
        mAddPictureFromCameraTv.setOnClickListener(mOnClickListener);
        mCancelAddPictureTv.setOnClickListener(mOnClickListener);

    }

    @Override
    public void initData() {
        super.initData();
        Bundle mBundle = getIntent().getExtras();
        if(mBundle != null){
            mRiverStartTime = mBundle.getLong(EXTRA_RIVER_START_TIME);
            mRiverEndTime = mBundle.getLong(EXTRA_RIVER_END_TIME);
        }

        mPictureWidth = (CommonUtils.getScreenWidth(mContext) - 8 * 16) / 4;
        mPictureHeight = mPictureWidth;

        mUploadEventAdapter.notifyDataSetChanged();
    }


//    class RiverPictureAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return mRiverPicturePaths.size()+1;
//        }
//
//        @Override
//        public String getItem(int position) {
//            return mRiverPicturePaths.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            if(null == convertView){
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.upload_river_picture_item_view,null);
//            }
//
//            SimpleDraweeView mSimpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.upload_river_picture_item_img);
//
//            if(position == getCount() - 1){
//                mSimpleDraweeView.setImageResource(R.drawable.ic_add_picture);
//                mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mAddPictureTipsLl.setVisibility(View.VISIBLE);
//                    }
//                });
//            }else{
//                //Uri.parse("file://" + MyBimp.tempSelectBitmap.get(position).path))
//
//                /*
//                mSimpleDraweeView.setDownsampleEnabled(true);
//                Uri mUri = Uri.parse("file://" + mEventPicturePaths.get(position));
//                mSimpleDraweeView.setImageURI(mUri);
//                mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mEventPicturePaths.remove(position);
//                        notifyDataSetChanged();
//                    }
//                });
//                */
//
//
//                int mWidth = mSimpleDraweeView.getWidth();
//                int mHeight = mSimpleDraweeView.getHeight();
//                if(mWidth > 0 && mHeight > 0){
//                    Uri mUri = Uri.parse("file://" + mRiverPicturePaths.get(position));
//                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(mUri)
//                            .setResizeOptions(new ResizeOptions(mWidth,mHeight))
//                            .build();
//
//                    DraweeController controller = Fresco.newDraweeControllerBuilder()
//                            .setImageRequest(request)
//                            .setOldController(mSimpleDraweeView.getController())
//                            .setControllerListener(new BaseControllerListener<ImageInfo>())
//                            .build();
//                    mSimpleDraweeView.setController(controller);
//                }
//
//            }
//
//            return convertView;
//        }
//    }

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
                    mUploadEventAdapter.notifyDataSetChanged();
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
                            mUploadEventAdapter.notifyDataSetChanged();
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

    class UploadEventAdapter extends RecyclerView.Adapter<BaseViewHolder>{
        private static final int HEAD_ITEM_TYPE = 0;
        private static final int PICTURE_ITEM_TYPE = 1;
        private static final int BOTTOM_ITEM_TYPE = 2;


        private static final int HEAD_COUNT = 1;
        private static final int BOTTOM_COUNT = 1;


        @Override
        public int getItemViewType(int position) {
            int mPictureItemCount = (mRiverPicturePaths.size() + 1) / 4 + ((mRiverPicturePaths.size() + 1) % 4 != 0 ? 1 : 0);
            if(position < HEAD_COUNT){
                return HEAD_ITEM_TYPE;
            }else if(position < HEAD_COUNT + mPictureItemCount){
                return PICTURE_ITEM_TYPE;
            }else if(position < HEAD_COUNT + mPictureItemCount + BOTTOM_COUNT){
                return BOTTOM_ITEM_TYPE;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            int mPictureItemCount = (mRiverPicturePaths.size() + 1) / 4 + ((mRiverPicturePaths.size() + 1) % 4 != 0 ? 1 : 0);
            int mCount = HEAD_COUNT +
                    mPictureItemCount +
                    BOTTOM_COUNT;
            return mCount;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case HEAD_ITEM_TYPE:
                    View mHeadItemView = LayoutInflater.from(mContext).inflate(R.layout.upload_river_head_view,parent,false);
                    return new HeadItemViewHolder(mHeadItemView);
                case PICTURE_ITEM_TYPE:
                    View mPictureItemView = LayoutInflater.from(mContext).inflate(R.layout.upload_river_picture_item_view,parent,false);
                    return new PictureItemViewHolder(mPictureItemView);
                case BOTTOM_ITEM_TYPE:
                    View mBottomItemView = LayoutInflater.from(mContext).inflate(R.layout.upload_river_bottom_view,parent,false);
                    return new BottomItemViewHolder(mBottomItemView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            if(holder instanceof HeadItemViewHolder){

                TextView mRiverStartTimeTv = (TextView) holder.findViewById(R.id.river_start_time_tv);
                TextView mRiverEndTimeTv = (TextView) holder.findViewById(R.id.river_end_time_tv);

                mRiverStartTimeTv.setText(mRiverTimeFormat.format(mRiverStartTime > 0 ? new Date(mRiverStartTime) : new Date()));
                mRiverEndTimeTv.setText(mRiverTimeFormat.format(mRiverEndTime > 0 ? new Date(mRiverEndTime) : new Date()));
            }else if(holder instanceof PictureItemViewHolder){
                final int mPictureIndex = position - HEAD_COUNT;
                LinearLayout mUploadRiverPictureItemTopLl = (LinearLayout) holder.findViewById(R.id.upload_river_picture_item_top_ll);
                LinearLayout mUploadRiverPictureItemLl1 = (LinearLayout) holder.findViewById(R.id.upload_river_picture_item_ll_1);
                LinearLayout mUploadRiverPictureItemLl2 = (LinearLayout) holder.findViewById(R.id.upload_river_picture_item_ll_2);
                LinearLayout mUploadRiverPictureItemLl3 = (LinearLayout) holder.findViewById(R.id.upload_river_picture_item_ll_3);
                LinearLayout mUploadRiverPictureItemLl4 = (LinearLayout) holder.findViewById(R.id.upload_river_picture_item_ll_4);
                SimpleDraweeView mUploadRiverPictureItemImg1 = (SimpleDraweeView) holder.findViewById(R.id.upload_river_picture_item_img_1);
                SimpleDraweeView mUploadRiverPictureItemImg2 = (SimpleDraweeView) holder.findViewById(R.id.upload_river_picture_item_img_2);
                SimpleDraweeView mUploadRiverPictureItemImg3 = (SimpleDraweeView) holder.findViewById(R.id.upload_river_picture_item_img_3);
                SimpleDraweeView mUploadRiverPictureItemImg4 = (SimpleDraweeView) holder.findViewById(R.id.upload_river_picture_item_img_4);
                ImageView mUploadRiverPictureItemDeleteImg1 = (ImageView) holder.findViewById(R.id.upload_river_picture_item_delete_img_1);
                ImageView mUploadRiverPictureItemDeleteImg2 = (ImageView) holder.findViewById(R.id.upload_river_picture_item_delete_img_2);
                ImageView mUploadRiverPictureItemDeleteImg3 = (ImageView) holder.findViewById(R.id.upload_river_picture_item_delete_img_3);
                ImageView mUploadRiverPictureItemDeleteImg4 = (ImageView) holder.findViewById(R.id.upload_river_picture_item_delete_img_4);

//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mPictureWidth,mPictureHeight);
//                mUploadRiverPictureItemImg1.setLayoutParams(lp);
//                mUploadRiverPictureItemImg2.setLayoutParams(lp);
//                mUploadRiverPictureItemImg3.setLayoutParams(lp);
//                mUploadRiverPictureItemImg4.setLayoutParams(lp);

                if(mPictureIndex < 1){
                    mUploadRiverPictureItemTopLl.setVisibility(View.VISIBLE);
                }else{
                    mUploadRiverPictureItemTopLl.setVisibility(View.GONE);
                }


                doForPictureItemView(mPictureIndex * 4,mUploadRiverPictureItemLl1,mUploadRiverPictureItemImg1,mUploadRiverPictureItemDeleteImg1);
                doForPictureItemView(mPictureIndex * 4 + 1,mUploadRiverPictureItemLl2,mUploadRiverPictureItemImg2,mUploadRiverPictureItemDeleteImg2);
                doForPictureItemView(mPictureIndex * 4 + 2,mUploadRiverPictureItemLl3,mUploadRiverPictureItemImg3,mUploadRiverPictureItemDeleteImg3);
                doForPictureItemView(mPictureIndex * 4 + 3,mUploadRiverPictureItemLl4,mUploadRiverPictureItemImg4,mUploadRiverPictureItemDeleteImg4);

            }else if(holder instanceof BottomItemViewHolder){

            }
        }

        private void doForPictureItemView(final int pictureItemIndex, View pictureItemLl, SimpleDraweeView pictureItemImg,ImageView deleteImg){
            DebugUtils.d(TAG,"doForPictureItemView::pictureItemIndex = " + pictureItemIndex);
            DebugUtils.d(TAG,"doForPictureItemView::mRiverPicturePaths.size() = " + mRiverPicturePaths.size());

            if(pictureItemIndex < mRiverPicturePaths.size()){
                pictureItemLl.setVisibility(View.VISIBLE);

                setSimpleDraweeViewLocalPath(pictureItemImg,mRiverPicturePaths.get(pictureItemIndex));

                if(isDeletePictureMode){
                    deleteImg.setVisibility(View.VISIBLE);
                }else{
                    deleteImg.setVisibility(View.GONE);
                }

            }else if(pictureItemIndex == mRiverPicturePaths.size()){
                pictureItemLl.setVisibility(View.VISIBLE);

                setSimpleDraweeViewRes(pictureItemImg,R.mipmap.ic_add_picture);

                deleteImg.setVisibility(View.GONE);

            }else{
                pictureItemLl.setVisibility(View.INVISIBLE);
            }

            pictureItemLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DebugUtils.d(TAG,"doForPictureItemView::onClick::pictureItemIndex = " + pictureItemIndex);
                    DebugUtils.d(TAG,"doForPictureItemView::onClick::mRiverPicturePaths.size() = " + mRiverPicturePaths.size());
                    if(pictureItemIndex == mRiverPicturePaths.size()){
                        mAddPictureTipsLl.setVisibility(View.VISIBLE);
                    }else{
                        if(isDeletePictureMode){
                            mRiverPicturePaths.remove(pictureItemIndex);
                            if(mRiverPicturePaths.size() == 0){
                                isDeletePictureMode = false;
                            }
                            notifyDataSetChanged();
                        }
                    }
                }
            });

            pictureItemLl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(pictureItemIndex < mRiverPicturePaths.size()){
                        if(isDeletePictureMode){
                            isDeletePictureMode = false;
                        }else{
                            isDeletePictureMode = true;
                        }
                        notifyDataSetChanged();
                    }
                    return true;
                }
            });
        }

        private void setSimpleDraweeViewLocalPath(SimpleDraweeView simpleDraweeViewRes,String path){
            int mWidth = mPictureWidth;
            int mHeight = mPictureHeight;
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

        private void setSimpleDraweeViewRes(SimpleDraweeView simpleDraweeViewRes,int resId){
            int mWidth = mPictureWidth;
            int mHeight = mPictureHeight;
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

    class HeadItemViewHolder extends BaseViewHolder{

        public HeadItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    class PictureItemViewHolder extends BaseViewHolder {

        public PictureItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    class BottomItemViewHolder extends BaseViewHolder{

        public BottomItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
