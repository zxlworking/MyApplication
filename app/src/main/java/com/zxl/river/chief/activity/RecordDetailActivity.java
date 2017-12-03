package com.zxl.river.chief.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.zxl.river.chief.utils.CommonUtils;
import com.zxl.river.chief.utils.DebugUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 17-12-3.
 */

public class RecordDetailActivity extends BaseActivity {
    private static final String TAG = "RecordDetailActivity";

    private RecyclerView mRecordDetailRecyclerView;
    private RecordDetailAdapter mRecordDetailAdapter;

    private List<Integer> mRecordDetailPicturePaths = new ArrayList<>();

    private int mPictureWidth = 0;
    private int mPictureHeight = 0;

    @Override
    public int getContentView() {
        return R.layout.record_detail_activity;
    }

    @Override
    public void initView() {
        super.initView();
        setRightImgVisibility(View.GONE);
        setTitle("日志详情");

        mRecordDetailRecyclerView = (RecyclerView) findViewById(R.id.record_detail_recycler_view);
        mRecordDetailRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecordDetailAdapter = new RecordDetailAdapter();
        mRecordDetailRecyclerView.setAdapter(mRecordDetailAdapter);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.title_bar_left_img:
                        finish();
                        break;
                }
            }
        };
    }

    @Override
    public void initData() {
        super.initData();

        mPictureWidth = (CommonUtils.getScreenWidth(mContext) - 8 * 16) / 4;
        mPictureHeight = mPictureWidth;

        mRecordDetailPicturePaths.add(R.drawable.t1);
        mRecordDetailPicturePaths.add(R.drawable.t2);
        mRecordDetailPicturePaths.add(R.drawable.t3);
        mRecordDetailPicturePaths.add(R.drawable.t4);
//        mRecordDetailPicturePaths.add("/storage/emulated/0/1.png");
//        mRecordDetailPicturePaths.add("/storage/emulated/0/1.png");
//        mRecordDetailPicturePaths.add("/storage/emulated/0/1.png");
//        mRecordDetailPicturePaths.add("/storage/emulated/0/1.png");
//        mRecordDetailPicturePaths.add("/storage/emulated/0/1.png");
//        mRecordDetailPicturePaths.add("/storage/emulated/0/1.png");

        mRecordDetailAdapter.notifyDataSetChanged();
    }


    class RecordDetailAdapter extends RecyclerView.Adapter<BaseViewHolder>{
        private static final int HEAD_ITEM_TYPE = 0;
        private static final int PICTURE_ITEM_TYPE = 1;
        private static final int BOTTOM_ITEM_TYPE = 2;


        private static final int HEAD_COUNT = 1;
        private static final int BOTTOM_COUNT = 1;


        @Override
        public int getItemViewType(int position) {
            int mPictureItemCount = mRecordDetailPicturePaths.size() / 4 + (mRecordDetailPicturePaths.size() % 4 != 0 ? 1 : 0);
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
            int mPictureItemCount = mRecordDetailPicturePaths.size() / 4 + (mRecordDetailPicturePaths.size() % 4 != 0 ? 1 : 0);
            int mCount = HEAD_COUNT + mPictureItemCount + BOTTOM_COUNT;
            return mCount;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case HEAD_ITEM_TYPE:
                    View mHeadItemView = LayoutInflater.from(mContext).inflate(R.layout.record_detail_head_view,parent,false);
                    return new HeadItemViewHolder(mHeadItemView);
                case PICTURE_ITEM_TYPE:
                    View mPictureItemView = LayoutInflater.from(mContext).inflate(R.layout.record_detail_picture_item_view,parent,false);
                    return new PictureItemViewHolder(mPictureItemView);
                case BOTTOM_ITEM_TYPE:
                    View mBottomItemView = LayoutInflater.from(mContext).inflate(R.layout.record_detail_bottom_view,parent,false);
                    return new BottomItemViewHolder(mBottomItemView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            if(holder instanceof HeadItemViewHolder){

            }else if(holder instanceof PictureItemViewHolder){
                final int mPictureIndex = position - HEAD_COUNT;
                LinearLayout mRecordDetailPictureItemTopLl = (LinearLayout) holder.findViewById(R.id.record_detail_picture_item_top_ll);
                LinearLayout mRecordDetailPictureItemLl1 = (LinearLayout) holder.findViewById(R.id.record_detail_picture_item_ll_1);
                LinearLayout mRecordDetailPictureItemLl2 = (LinearLayout) holder.findViewById(R.id.record_detail_picture_item_ll_2);
                LinearLayout mRecordDetailPictureItemLl3 = (LinearLayout) holder.findViewById(R.id.record_detail_picture_item_ll_3);
                LinearLayout mRecordDetailPictureItemLl4 = (LinearLayout) holder.findViewById(R.id.record_detail_picture_item_ll_4);
                SimpleDraweeView mRecordDetailPictureItemImg1 = (SimpleDraweeView) holder.findViewById(R.id.record_detail_picture_item_img_1);
                SimpleDraweeView mRecordDetailPictureItemImg2 = (SimpleDraweeView) holder.findViewById(R.id.record_detail_picture_item_img_2);
                SimpleDraweeView mRecordDetailPictureItemImg3 = (SimpleDraweeView) holder.findViewById(R.id.record_detail_picture_item_img_3);
                SimpleDraweeView mRecordDetailPictureItemImg4 = (SimpleDraweeView) holder.findViewById(R.id.record_detail_picture_item_img_4);

//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mPictureWidth,mPictureHeight);
//                mRecordDetailPictureItemImg1.setLayoutParams(lp);
//                mRecordDetailPictureItemImg2.setLayoutParams(lp);
//                mRecordDetailPictureItemImg3.setLayoutParams(lp);
//                mRecordDetailPictureItemImg4.setLayoutParams(lp);

                if(mPictureIndex < 1){
                    mRecordDetailPictureItemTopLl.setVisibility(View.VISIBLE);
                }else{
                    mRecordDetailPictureItemTopLl.setVisibility(View.GONE);
                }


                doForPictureItemView(mPictureIndex * 4,mRecordDetailPictureItemLl1,mRecordDetailPictureItemImg1);
                doForPictureItemView(mPictureIndex * 4 + 1,mRecordDetailPictureItemLl2,mRecordDetailPictureItemImg2);
                doForPictureItemView(mPictureIndex * 4 + 2,mRecordDetailPictureItemLl3,mRecordDetailPictureItemImg3);
                doForPictureItemView(mPictureIndex * 4 + 3,mRecordDetailPictureItemLl4,mRecordDetailPictureItemImg4);

            }else if(holder instanceof BottomItemViewHolder){
                holder.getContentView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mUploadEventIntent = new Intent(mContext,TrajectoryPlaybackActivity.class);
                        mUploadEventIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mUploadEventIntent);
                    }
                });
            }
        }

        private void doForPictureItemView(final int pictureItemIndex, View pictureItemLl, SimpleDraweeView pictureItemImg){
            DebugUtils.d(TAG,"doForPictureItemView::pictureItemIndex = " + pictureItemIndex);
            DebugUtils.d(TAG,"doForPictureItemView::mRecordDetailPicturePaths.size() = " + mRecordDetailPicturePaths.size());

            if(pictureItemIndex < mRecordDetailPicturePaths.size()){
                pictureItemLl.setVisibility(View.VISIBLE);

                setSimpleDraweeViewRes(pictureItemImg,mRecordDetailPicturePaths.get(pictureItemIndex));

            }else{
                pictureItemLl.setVisibility(View.INVISIBLE);
            }
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
