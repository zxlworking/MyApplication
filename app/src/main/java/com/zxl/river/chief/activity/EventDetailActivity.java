package com.zxl.river.chief.activity;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
 * Created by mac on 17-12-2.
 */

public class EventDetailActivity extends BaseActivity {

    private static final String TAG = "EventDetailActivity";

    private RecyclerView mEventDetailRecyclerView;
    private EventDetailAdapter mEventDetailAdapter;

    private List<String> mEventPicturePaths = new ArrayList<>();
    private List<String> mEventPersonPaths = new ArrayList<>();

    private int mPictureWidth = 0;
    private int mPictureHeight = 0;

    @Override
    public int getContentView() {
        return R.layout.event_detail_activity;
    }

    @Override
    public void initView() {
        super.initView();

        DebugUtils.d(TAG,"zxl--->initView");

        setTitle("事件详情");
        setRightImgVisibility(View.GONE);

        mEventDetailRecyclerView = (RecyclerView) findViewById(R.id.event_detail_recycler_view);
        mEventDetailRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mEventDetailAdapter = new EventDetailAdapter();
        mEventDetailRecyclerView.setAdapter(mEventDetailAdapter);

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

        DebugUtils.d(TAG,"zxl--->initData");

        mPictureWidth = (CommonUtils.getScreenWidth(mContext) - 8 * 16) / 4;
        mPictureHeight = mPictureWidth;

        for(int i = 0; i < 6; i++){
            mEventPicturePaths.add("--->"+i);
        }
        mEventPersonPaths.add("http://img3.duitang.com/uploads/item/201511/13/20151113110644_PcSFj.thumb.224_0.jpeg");
        mEventDetailAdapter.notifyDataSetChanged();
    }


    class EventDetailAdapter extends RecyclerView.Adapter<BaseViewHolder>{
        private static final int HEAD_ITEM_TYPE = 0;
        private static final int PICTURE_ITEM_TYPE = 1;
        private static final int PERSON_ITEM_TYPE = 2;
        private static final int BOTTOM_ITEM_TYPE = 3;


        private static final int HEAD_COUNT = 1;
        private static final int BOTTOM_COUNT = 1;


        @Override
        public int getItemViewType(int position) {
            int mPictureItemCount = mEventPicturePaths.size() / 4 + (mEventPicturePaths.size() % 4 != 0 ? 1 : 0);
            if(position < HEAD_COUNT){
                return HEAD_ITEM_TYPE;
            }else if(position < HEAD_COUNT + mPictureItemCount){
                return PICTURE_ITEM_TYPE;
            }else if(position < HEAD_COUNT + mPictureItemCount + mEventPersonPaths.size()){
                return PERSON_ITEM_TYPE;
            }else if(position < HEAD_COUNT + mPictureItemCount + mEventPersonPaths.size() + BOTTOM_COUNT){
                return BOTTOM_ITEM_TYPE;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            int mPictureItemCount = mEventPicturePaths.size() / 4 + (mEventPicturePaths.size() % 4 != 0 ? 1 : 0);
            int mCount = HEAD_COUNT +
                    mPictureItemCount +
                    mEventPersonPaths.size() +
                    BOTTOM_COUNT;
            return mCount;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case HEAD_ITEM_TYPE:
                    View mHeadItemView = LayoutInflater.from(mContext).inflate(R.layout.event_detail_head_view,parent,false);
                    return new HeadItemViewHolder(mHeadItemView);
                case PICTURE_ITEM_TYPE:
                    View mPictureItemView = LayoutInflater.from(mContext).inflate(R.layout.event_detail_picture_item_view,parent,false);
                    return new PictureItemViewHolder(mPictureItemView);
                case PERSON_ITEM_TYPE:
                    View mPersonItemView = LayoutInflater.from(mContext).inflate(R.layout.event_detail_person_item_view,parent,false);
                    return new PersonItemViewHolder(mPersonItemView);
                case BOTTOM_ITEM_TYPE:
                    View mBottomItemView = LayoutInflater.from(mContext).inflate(R.layout.event_detail_bottom_view,parent,false);
                    return new BottomItemViewHolder(mBottomItemView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            if(holder instanceof HeadItemViewHolder){

            }else if(holder instanceof PictureItemViewHolder){
                int mPictureIndex = position - HEAD_COUNT;
                LinearLayout mEventDetailPictureItemTopLl = (LinearLayout) holder.findViewById(R.id.event_detail_picture_item_top_ll);
                LinearLayout mEventDetailPictureItemLl1 = (LinearLayout) holder.findViewById(R.id.event_detail_picture_item_ll_1);
                LinearLayout mEventDetailPictureItemLl2 = (LinearLayout) holder.findViewById(R.id.event_detail_picture_item_ll_2);
                LinearLayout mEventDetailPictureItemLl3 = (LinearLayout) holder.findViewById(R.id.event_detail_picture_item_ll_3);
                LinearLayout mEventDetailPictureItemLl4 = (LinearLayout) holder.findViewById(R.id.event_detail_picture_item_ll_4);
                SimpleDraweeView mEventDetailPictureItemImg1 = (SimpleDraweeView) holder.findViewById(R.id.event_detail_picture_item_img_1);
                SimpleDraweeView mEventDetailPictureItemImg2 = (SimpleDraweeView) holder.findViewById(R.id.event_detail_picture_item_img_2);
                SimpleDraweeView mEventDetailPictureItemImg3 = (SimpleDraweeView) holder.findViewById(R.id.event_detail_picture_item_img_3);
                SimpleDraweeView mEventDetailPictureItemImg4 = (SimpleDraweeView) holder.findViewById(R.id.event_detail_picture_item_img_4);

//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mPictureWidth,mPictureHeight);
//                mEventDetailPictureItemImg1.setLayoutParams(lp);
//                mEventDetailPictureItemImg2.setLayoutParams(lp);
//                mEventDetailPictureItemImg3.setLayoutParams(lp);
//                mEventDetailPictureItemImg4.setLayoutParams(lp);


                if(mPictureIndex < 1){
                    mEventDetailPictureItemTopLl.setVisibility(View.VISIBLE);
                }else{
                    mEventDetailPictureItemTopLl.setVisibility(View.GONE);
                }

                if(mPictureIndex * 4 < mEventPicturePaths.size()){
                    mEventDetailPictureItemLl1.setVisibility(View.VISIBLE);
                    //mEventDetailPictureItemImg1.setImageResource(R.drawable.t1);

                    setSimpleDraweeViewRes(mEventDetailPictureItemImg1,R.drawable.t1);
                }else{
                    mEventDetailPictureItemLl1.setVisibility(View.INVISIBLE);
                }
                if(mPictureIndex * 4 + 1 < mEventPicturePaths.size()){
                    mEventDetailPictureItemLl2.setVisibility(View.VISIBLE);
                    //mEventDetailPictureItemImg2.setImageResource(R.drawable.t2);

                    setSimpleDraweeViewRes(mEventDetailPictureItemImg2,R.drawable.t2);
                }else{
                    mEventDetailPictureItemLl2.setVisibility(View.INVISIBLE);
                }
                if(mPictureIndex * 4 + 2 < mEventPicturePaths.size()){
                    mEventDetailPictureItemLl3.setVisibility(View.VISIBLE);
                    //mEventDetailPictureItemImg3.setImageResource(R.drawable.t3);

                    setSimpleDraweeViewRes(mEventDetailPictureItemImg3,R.drawable.t3);
                }else{
                    mEventDetailPictureItemLl3.setVisibility(View.INVISIBLE);
                }
                if(mPictureIndex * 4 + 3 < mEventPicturePaths.size()){
                    mEventDetailPictureItemLl4.setVisibility(View.VISIBLE);
                    //mEventDetailPictureItemImg4.setImageResource(R.drawable.t4);

                    setSimpleDraweeViewRes(mEventDetailPictureItemImg4,R.drawable.t4);
                }else{
                    mEventDetailPictureItemLl4.setVisibility(View.INVISIBLE);
                }
            }else if(holder instanceof PersonItemViewHolder){
                SimpleDraweeView mEventDetailPersonItemImg = (SimpleDraweeView) holder.findViewById(R.id.event_detail_person_item_img);
                int mPictureItemCount = mEventPicturePaths.size() / 4 + (mEventPicturePaths.size() % 4 != 0 ? 1 : 0);
                int mPersonIndex = position - HEAD_COUNT - mPictureItemCount;
                mEventDetailPersonItemImg.setImageURI(mEventPersonPaths.get(mPersonIndex));
            }else if(holder instanceof BottomItemViewHolder){
                holder.getContentView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mUploadEventIntent = new Intent(mContext,DealEventActivity.class);
                        mUploadEventIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mUploadEventIntent);
                    }
                });
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
