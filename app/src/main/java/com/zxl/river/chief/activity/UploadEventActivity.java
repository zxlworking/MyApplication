package com.zxl.river.chief.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.zxl.river.chief.utils.PhotoUtils.CAMERA_PERMISSIONS_REQUEST_CODE;
import static com.zxl.river.chief.utils.PhotoUtils.CODE_CAMERA_REQUEST;
import static com.zxl.river.chief.utils.PhotoUtils.CODE_GALLERY_REQUEST;
import static com.zxl.river.chief.utils.PhotoUtils.CODE_RESULT_REQUEST;
import static com.zxl.river.chief.utils.PhotoUtils.STORAGE_PERMISSIONS_REQUEST_CODE;


/**
 * Created by mac on 17-11-24.
 */

public class UploadEventActivity extends BaseActivity {
    private static final String TAG = "UploadEventActivity";

    private RecyclerView mUploadEventRecyclerView;
    private UploadEventAdapter mUploadEventAdapter;

    private List<String> mEventPicturePaths = new ArrayList<>();
    private List<String> mEventPersonPaths = new ArrayList<>();

    private LinearLayout mAddPictureTipsLl;
    private TextView mAddPictureFromAlbumTv;
    private TextView mAddPictureFromCameraTv;
    private TextView mCancelAddPictureTv;

    private String mCurrentPhotoPath = "";
    private String mCurrentCropPhotoPath = "";
    private int mOutPutX = 480;
    private int mOutPutY = 480;

    private int mPictureWidth = 0;
    private int mPictureHeight = 0;

    private boolean isDeletePictureMode = false;
    private boolean isDeletePersonMode = false;

    @Override
    public int getContentView() {
        return R.layout.upload_event_activity;
    }

    @Override
    public void initView() {
        super.initView();

        setRightImgVisibility(View.GONE);
        setTitle("上报事件");

        mAddPictureTipsLl = (LinearLayout) findViewById(R.id.add_picture_tips_ll);
        mAddPictureFromAlbumTv = (TextView) findViewById(R.id.add_picture_from_album_tv);
        mAddPictureFromCameraTv = (TextView) findViewById(R.id.add_picture_from_camera_tv);
        mCancelAddPictureTv = (TextView) findViewById(R.id.cancel_add_picture_tv);

        mUploadEventRecyclerView = (RecyclerView) findViewById(R.id.upload_event_recycler_view);
        mUploadEventRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mUploadEventAdapter = new UploadEventAdapter();
        mUploadEventRecyclerView.setAdapter(mUploadEventAdapter);


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
        mOutPutX = CommonUtils.getScreenWidth(mContext);
        mOutPutY = CommonUtils.getScreenHeight(mContext);

        mPictureWidth = (CommonUtils.getScreenWidth(mContext) - 8 * 16) / 4;
        mPictureHeight = mPictureWidth;


//        mEventPersonPaths.add("http://img3.duitang.com/uploads/item/201511/13/20151113110644_PcSFj.thumb.224_0.jpeg");
//        mEventPersonPaths.add("http://diy.qqjay.com/u2/2014/1130/6272576897a2e42385ddbcf41435d938.jpg");
//        mEventPersonPaths.add("http://www.feizl.com/upload2007/2014_01/140116182482507.jpg");
//        mEventPersonPaths.add("http://img2.imgtn.bdimg.com/it/u=3746075707,1914896074&fm=214&gp=0.jpg");
//        mEventPersonPaths.add("http://img3.imgtn.bdimg.com/it/u=2777008330,1289798081&fm=27&gp=0.jpg");
        mUploadEventAdapter.notifyDataSetChanged();

    }

//    class EventPictureAdapter extends BaseAdapter{
//
//        @Override
//        public int getCount() {
//            return mEventPicturePaths.size()+1;
//        }
//
//        @Override
//        public String getItem(int position) {
//            return mEventPicturePaths.get(position);
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
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.upload_event_picture_item_view,null);
//            }
//
//            SimpleDraweeView mSimpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.upload_event_picture_item_img);
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
//                    Uri mUri = Uri.parse("file://" + mEventPicturePaths.get(position));
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
//
//    class EventPersonAdapter extends BaseAdapter{
//
//        @Override
//        public int getCount() {
//            return mEventPersonPaths.size()+1;
//        }
//
//        @Override
//        public String getItem(int position) {
//            return mEventPersonPaths.get(position);
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
//                convertView = LayoutInflater.from(mContext).inflate(R.layout.upload_event_person_item_view,null);
//            }
//
//            SimpleDraweeView mSimpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.upload_event_person_item_img);
//
//            if(position == getCount() - 1){
//                mSimpleDraweeView.setImageResource(R.drawable.ic_add_picture);
//                /*
//                mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mAddPictureTipsLl.setVisibility(View.VISIBLE);
//                    }
//                });
//                */
//            }else{
//                mSimpleDraweeView.setImageURI(mEventPersonPaths.get(position));
//                /*
//                mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mEventPicturePaths.remove(position);
//                        notifyDataSetChanged();
//                    }
//                });
//                */
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

        DebugUtils.d(TAG,"onActivityResult::requestCode = " + requestCode);
        DebugUtils.d(TAG,"onActivityResult::resultCode = " + resultCode);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    DebugUtils.d(TAG,"onActivityResult::CODE_CAMERA_REQUEST::mCurrentPhotoPath = " + mCurrentPhotoPath);
                    if(data != null){
                        DebugUtils.d(TAG,"onActivityResult::CODE_CAMERA_REQUEST::data = " + data.getData());
                    }
                    mEventPicturePaths.add(0,mCurrentPhotoPath);
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

                            mEventPicturePaths.add(0,mNewPath);
                            mUploadEventAdapter.notifyDataSetChanged();
                            /*
                            generateCropPhotoPath();
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
                case PersonListActivity.CODE_PERSON_LIST_REQUEST:
                    String mPersonPath = data.getStringExtra(PersonListActivity.EXTRA_PERSON_LIST);
                    DebugUtils.d(TAG,"onActivityResult::CODE_PERSON_LIST_REQUEST::mPersonPath = " + mPersonPath);
                    if(!TextUtils.isEmpty(mPersonPath) && !mEventPersonPaths.contains(mPersonPath)){
                        mEventPersonPaths.add(mPersonPath);
                        mUploadEventAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    private void generatePhotoPath(){
        mCurrentPhotoPath = Constants.APP_PHOTO_DIR + System.currentTimeMillis() + ".jpg";
    }
    private void generateCropPhotoPath(){
        mCurrentCropPhotoPath = Constants.APP_PHOTO_DIR + System.currentTimeMillis() + ".jpg";
    }

    class UploadEventAdapter extends RecyclerView.Adapter<BaseViewHolder>{
        private static final int HEAD_ITEM_TYPE = 0;
        private static final int PICTURE_ITEM_TYPE = 1;
        private static final int PERSON_ITEM_TYPE = 2;
        private static final int BOTTOM_ITEM_TYPE = 3;


        private static final int HEAD_COUNT = 1;
        private static final int BOTTOM_COUNT = 1;


        @Override
        public int getItemViewType(int position) {
            int mPictureItemCount = (mEventPicturePaths.size() + 1) / 4 + ((mEventPicturePaths.size() + 1) % 4 != 0 ? 1 : 0);
            int mPersonItemCount = (mEventPersonPaths.size() + 1) / 4 + ((mEventPersonPaths.size() + 1) % 4 != 0 ? 1 : 0);
            if(position < HEAD_COUNT){
                return HEAD_ITEM_TYPE;
            }else if(position < HEAD_COUNT + mPictureItemCount){
                return PICTURE_ITEM_TYPE;
            }else if(position < HEAD_COUNT + mPictureItemCount + mPersonItemCount){
                return PERSON_ITEM_TYPE;
            }else if(position < HEAD_COUNT + mPictureItemCount + mPersonItemCount + BOTTOM_COUNT){
                return BOTTOM_ITEM_TYPE;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            int mPictureItemCount = (mEventPicturePaths.size() + 1) / 4 + ((mEventPicturePaths.size() + 1) % 4 != 0 ? 1 : 0);
            int mPersonItemCount = (mEventPersonPaths.size() + 1) / 4 + ((mEventPersonPaths.size() + 1) % 4 != 0 ? 1 : 0);
            int mCount = HEAD_COUNT +
                    mPictureItemCount +
                    mPersonItemCount +
                    BOTTOM_COUNT;
            return mCount;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case HEAD_ITEM_TYPE:
                    View mHeadItemView = LayoutInflater.from(mContext).inflate(R.layout.upload_event_head_view,parent,false);
                    return new HeadItemViewHolder(mHeadItemView);
                case PICTURE_ITEM_TYPE:
                    View mPictureItemView = LayoutInflater.from(mContext).inflate(R.layout.upload_event_picture_item_view,parent,false);
                    return new PictureItemViewHolder(mPictureItemView);
                case PERSON_ITEM_TYPE:
                    View mPersonItemView = LayoutInflater.from(mContext).inflate(R.layout.upload_event_person_item_view,parent,false);
                    return new PersonItemViewHolder(mPersonItemView);
                case BOTTOM_ITEM_TYPE:
                    View mBottomItemView = LayoutInflater.from(mContext).inflate(R.layout.upload_event_bottom_view,parent,false);
                    return new BottomItemViewHolder(mBottomItemView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            if(holder instanceof HeadItemViewHolder){

            }else if(holder instanceof PictureItemViewHolder){
                final int mPictureIndex = position - HEAD_COUNT;
                LinearLayout mUploadEventPictureItemTopLl = (LinearLayout) holder.findViewById(R.id.upload_event_picture_item_top_ll);
                LinearLayout mUploadEventPictureItemLl1 = (LinearLayout) holder.findViewById(R.id.upload_event_picture_item_ll_1);
                LinearLayout mUploadEventPictureItemLl2 = (LinearLayout) holder.findViewById(R.id.upload_event_picture_item_ll_2);
                LinearLayout mUploadEventPictureItemLl3 = (LinearLayout) holder.findViewById(R.id.upload_event_picture_item_ll_3);
                LinearLayout mUploadEventPictureItemLl4 = (LinearLayout) holder.findViewById(R.id.upload_event_picture_item_ll_4);
                SimpleDraweeView mUploadEventPictureItemImg1 = (SimpleDraweeView) holder.findViewById(R.id.upload_event_picture_item_img_1);
                SimpleDraweeView mUploadEventPictureItemImg2 = (SimpleDraweeView) holder.findViewById(R.id.upload_event_picture_item_img_2);
                SimpleDraweeView mUploadEventPictureItemImg3 = (SimpleDraweeView) holder.findViewById(R.id.upload_event_picture_item_img_3);
                SimpleDraweeView mUploadEventPictureItemImg4 = (SimpleDraweeView) holder.findViewById(R.id.upload_event_picture_item_img_4);
                ImageView mUploadEventPictureItemDeleteImg1 = (ImageView) holder.findViewById(R.id.upload_event_picture_item_delete_img_1);
                ImageView mUploadEventPictureItemDeleteImg2 = (ImageView) holder.findViewById(R.id.upload_event_picture_item_delete_img_2);
                ImageView mUploadEventPictureItemDeleteImg3 = (ImageView) holder.findViewById(R.id.upload_event_picture_item_delete_img_3);
                ImageView mUploadEventPictureItemDeleteImg4 = (ImageView) holder.findViewById(R.id.upload_event_picture_item_delete_img_4);

//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mPictureWidth,mPictureHeight);
//                mUploadEventPictureItemImg1.setLayoutParams(lp);
//                mUploadEventPictureItemImg2.setLayoutParams(lp);
//                mUploadEventPictureItemImg3.setLayoutParams(lp);
//                mUploadEventPictureItemImg4.setLayoutParams(lp);

                if(mPictureIndex < 1){
                    mUploadEventPictureItemTopLl.setVisibility(View.VISIBLE);
                }else{
                    mUploadEventPictureItemTopLl.setVisibility(View.GONE);
                }


                doForPictureItemView(mPictureIndex * 4,mUploadEventPictureItemLl1,mUploadEventPictureItemImg1,mUploadEventPictureItemDeleteImg1);
                doForPictureItemView(mPictureIndex * 4 + 1,mUploadEventPictureItemLl2,mUploadEventPictureItemImg2,mUploadEventPictureItemDeleteImg2);
                doForPictureItemView(mPictureIndex * 4 + 2,mUploadEventPictureItemLl3,mUploadEventPictureItemImg3,mUploadEventPictureItemDeleteImg3);
                doForPictureItemView(mPictureIndex * 4 + 3,mUploadEventPictureItemLl4,mUploadEventPictureItemImg4,mUploadEventPictureItemDeleteImg4);

            }else if(holder instanceof PersonItemViewHolder){
                int mPictureItemCount = (mEventPicturePaths.size() + 1) / 4 + ((mEventPicturePaths.size() + 1) % 4 != 0 ? 1 : 0);
                int mPersonIndex = position - HEAD_COUNT - mPictureItemCount;
                LinearLayout mUploadEventPersonItemTopLl = (LinearLayout) holder.findViewById(R.id.upload_event_person_item_top_ll);
                LinearLayout mUploadEventPersonItemLl1 = (LinearLayout) holder.findViewById(R.id.upload_event_person_item_ll_1);
                LinearLayout mUploadEventPersonItemLl2 = (LinearLayout) holder.findViewById(R.id.upload_event_person_item_ll_2);
                LinearLayout mUploadEventPersonItemLl3 = (LinearLayout) holder.findViewById(R.id.upload_event_person_item_ll_3);
                LinearLayout mUploadEventPersonItemLl4 = (LinearLayout) holder.findViewById(R.id.upload_event_person_item_ll_4);
                SimpleDraweeView mUploadEventPersonItemImg1 = (SimpleDraweeView) holder.findViewById(R.id.upload_event_person_item_img_1);
                SimpleDraweeView mUploadEventPersonItemImg2 = (SimpleDraweeView) holder.findViewById(R.id.upload_event_person_item_img_2);
                SimpleDraweeView mUploadEventPersonItemImg3 = (SimpleDraweeView) holder.findViewById(R.id.upload_event_person_item_img_3);
                SimpleDraweeView mUploadEventPersonItemImg4 = (SimpleDraweeView) holder.findViewById(R.id.upload_event_person_item_img_4);
                ImageView mUploadEventPersonItemDeleteImg1 = (ImageView) holder.findViewById(R.id.upload_event_person_item_delete_img_1);
                ImageView mUploadEventPersonItemDeleteImg2 = (ImageView) holder.findViewById(R.id.upload_event_person_item_delete_img_2);
                ImageView mUploadEventPersonItemDeleteImg3 = (ImageView) holder.findViewById(R.id.upload_event_person_item_delete_img_3);
                ImageView mUploadEventPersonItemDeleteImg4 = (ImageView) holder.findViewById(R.id.upload_event_person_item_delete_img_4);

                if(mPersonIndex < 1){
                    mUploadEventPersonItemTopLl.setVisibility(View.VISIBLE);
                }else{
                    mUploadEventPersonItemTopLl.setVisibility(View.GONE);
                }

                doForPersonItemView(mPersonIndex * 4,mUploadEventPersonItemLl1,mUploadEventPersonItemImg1,mUploadEventPersonItemDeleteImg1);
                doForPersonItemView(mPersonIndex * 4 + 1,mUploadEventPersonItemLl2,mUploadEventPersonItemImg2,mUploadEventPersonItemDeleteImg2);
                doForPersonItemView(mPersonIndex * 4 + 2,mUploadEventPersonItemLl3,mUploadEventPersonItemImg3,mUploadEventPersonItemDeleteImg3);
                doForPersonItemView(mPersonIndex * 4 + 3,mUploadEventPersonItemLl4,mUploadEventPersonItemImg4,mUploadEventPersonItemDeleteImg4);


            }else if(holder instanceof BottomItemViewHolder){

            }
        }

        private void doForPictureItemView(final int pictureItemIndex, View pictureItemLl, SimpleDraweeView pictureItemImg,ImageView deleteImg){
            DebugUtils.d(TAG,"doForPictureItemView::pictureItemIndex = " + pictureItemIndex);
            DebugUtils.d(TAG,"doForPictureItemView::mEventPicturePaths.size() = " + mEventPicturePaths.size());

            if(pictureItemIndex < mEventPicturePaths.size()){
                pictureItemLl.setVisibility(View.VISIBLE);

                setSimpleDraweeViewLocalPath(pictureItemImg,mEventPicturePaths.get(pictureItemIndex));

                if(isDeletePictureMode){
                    deleteImg.setVisibility(View.VISIBLE);
                }else{
                    deleteImg.setVisibility(View.GONE);
                }
            }else if(pictureItemIndex == mEventPicturePaths.size()){
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
                    DebugUtils.d(TAG,"doForPictureItemView::onClick::mEventPicturePaths.size() = " + mEventPicturePaths.size());
                    if(pictureItemIndex == mEventPicturePaths.size()){
                        mAddPictureTipsLl.setVisibility(View.VISIBLE);
                    }else{
                        if(isDeletePictureMode){
                            mEventPicturePaths.remove(pictureItemIndex);
                            if(mEventPicturePaths.size() == 0){
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
                    if(pictureItemIndex < mEventPicturePaths.size()){
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

        private void doForPersonItemView(final int mPersonItemIndex, View personItemLl, SimpleDraweeView personItemImg,ImageView deleteImg){
            DebugUtils.d(TAG,"doForPictureItemView::mPersonItemIndex = " + mPersonItemIndex);
            DebugUtils.d(TAG,"doForPictureItemView::mEventPersonPaths.size() = " + mEventPersonPaths.size());

            if(mPersonItemIndex < mEventPersonPaths.size()){
                personItemLl.setVisibility(View.VISIBLE);

                personItemImg.setImageURI(mEventPersonPaths.get(mPersonItemIndex));

                if(isDeletePersonMode){
                    deleteImg.setVisibility(View.VISIBLE);
                }else{
                    deleteImg.setVisibility(View.GONE);
                }
            }else if(mPersonItemIndex == mEventPersonPaths.size()){
                personItemLl.setVisibility(View.VISIBLE);

                setSimpleDraweeViewRes(personItemImg,R.mipmap.ic_add_person);

                deleteImg.setVisibility(View.GONE);

            }else{
                personItemLl.setVisibility(View.INVISIBLE);
            }

            personItemLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DebugUtils.d(TAG,"doForPictureItemView::onClick::mPersonItemIndex = " + mPersonItemIndex);
                    DebugUtils.d(TAG,"doForPictureItemView::onClick::mEventPersonPaths.size() = " + mEventPersonPaths.size());
                    if(mPersonItemIndex == mEventPersonPaths.size()){
                        Intent mPersonListIntent = new Intent(mContext,PersonListActivity.class);
                        startActivityForResult(mPersonListIntent,PersonListActivity.CODE_PERSON_LIST_REQUEST);
                    }else{
                        if(isDeletePersonMode){
                            mEventPersonPaths.remove(mPersonItemIndex);
                            if(mEventPersonPaths.size() == 0){
                                isDeletePersonMode = false;
                            }
                            notifyDataSetChanged();
                        }
                    }
                }
            });

            personItemLl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(mPersonItemIndex < mEventPersonPaths.size()){
                        if(isDeletePersonMode){
                            isDeletePersonMode = false;
                        }else{
                            isDeletePersonMode = true;
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

    class PictureItemViewHolder extends BaseViewHolder{

        public PictureItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    class PersonItemViewHolder extends BaseViewHolder{

        public PersonItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    class BottomItemViewHolder extends BaseViewHolder{

        public BottomItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
