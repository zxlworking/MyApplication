package com.zxl.river.chief.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 * Created by mac on 17-12-3.
 */

public class DealEventActivity extends BaseActivity {

    private static final String TAG = "DealEventActivity";

    private RecyclerView mDealEventRecyclerView;
    private DealEventAdapter mDealEventAdapter;

    private List<String> mDealEventPersonPaths = new ArrayList<>();

    private boolean isDeletePersonMode = false;

    private int mPictureWidth = 0;
    private int mPictureHeight = 0;

    @Override
    public int getContentView() {
        return R.layout.deal_event_activity;
    }

    @Override
    public void initView() {
        super.initView();

        setRightImgVisibility(View.GONE);
        setTitle("事件处理");

        mDealEventRecyclerView = (RecyclerView) findViewById(R.id.deal_event_recycler_view);
        mDealEventRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDealEventAdapter = new DealEventAdapter();
        mDealEventRecyclerView.setAdapter(mDealEventAdapter);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DebugUtils.d(TAG,"onActivityResult::requestCode = " + requestCode);
        DebugUtils.d(TAG,"onActivityResult::resultCode = " + resultCode);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PersonListActivity.CODE_PERSON_LIST_REQUEST:
                    String mPersonPath = data.getStringExtra(PersonListActivity.EXTRA_PERSON_LIST);
                    DebugUtils.d(TAG,"onActivityResult::CODE_PERSON_LIST_REQUEST::mPersonPath = " + mPersonPath);
                    if(!TextUtils.isEmpty(mPersonPath) && !mDealEventPersonPaths.contains(mPersonPath)){
                        mDealEventPersonPaths.add(mPersonPath);
                        mDealEventAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    class DealEventAdapter extends RecyclerView.Adapter<BaseViewHolder>{
        private static final int HEAD_ITEM_TYPE = 0;
        private static final int PERSON_ITEM_TYPE = 1;
        private static final int BOTTOM_ITEM_TYPE = 2;


        private static final int HEAD_COUNT = 1;
        private static final int BOTTOM_COUNT = 1;


        @Override
        public int getItemViewType(int position) {
            int mPersonItemCount = (mDealEventPersonPaths.size() + 1) / 4 + ((mDealEventPersonPaths.size() + 1) % 4 != 0 ? 1 : 0);
            if(position < HEAD_COUNT){
                return HEAD_ITEM_TYPE;
            }else if(position < HEAD_COUNT + mPersonItemCount){
                return PERSON_ITEM_TYPE;
            }else if(position < HEAD_COUNT + mPersonItemCount + BOTTOM_COUNT){
                return BOTTOM_ITEM_TYPE;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            int mPersonItemCount = (mDealEventPersonPaths.size() + 1) / 4 + ((mDealEventPersonPaths.size() + 1) % 4 != 0 ? 1 : 0);
            int mCount = HEAD_COUNT +
                    mPersonItemCount +
                    BOTTOM_COUNT;
            return mCount;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case HEAD_ITEM_TYPE:
                    View mHeadItemView = LayoutInflater.from(mContext).inflate(R.layout.deal_event_head_view,parent,false);
                    return new HeadItemViewHolder(mHeadItemView);
                case PERSON_ITEM_TYPE:
                    View mPersonItemView = LayoutInflater.from(mContext).inflate(R.layout.deal_event_person_item_view,parent,false);
                    return new PersonItemViewHolder(mPersonItemView);
                case BOTTOM_ITEM_TYPE:
                    View mBottomItemView = LayoutInflater.from(mContext).inflate(R.layout.deal_event_bottom_view,parent,false);
                    return new BottomItemViewHolder(mBottomItemView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            if(holder instanceof HeadItemViewHolder){

            }else if(holder instanceof PersonItemViewHolder){
                int mPersonIndex = position - HEAD_COUNT;
                LinearLayout mDealEventPersonItemTopLl = (LinearLayout) holder.findViewById(R.id.deal_event_person_item_top_ll);
                LinearLayout mDealEventPersonItemLl1 = (LinearLayout) holder.findViewById(R.id.deal_event_person_item_ll_1);
                LinearLayout mDealEventPersonItemLl2 = (LinearLayout) holder.findViewById(R.id.deal_event_person_item_ll_2);
                LinearLayout mDealEventPersonItemLl3 = (LinearLayout) holder.findViewById(R.id.deal_event_person_item_ll_3);
                LinearLayout mDealEventPersonItemLl4 = (LinearLayout) holder.findViewById(R.id.deal_event_person_item_ll_4);
                SimpleDraweeView mDealEventPersonItemImg1 = (SimpleDraweeView) holder.findViewById(R.id.deal_event_person_item_img_1);
                SimpleDraweeView mDealEventPersonItemImg2 = (SimpleDraweeView) holder.findViewById(R.id.deal_event_person_item_img_2);
                SimpleDraweeView mDealEventPersonItemImg3 = (SimpleDraweeView) holder.findViewById(R.id.deal_event_person_item_img_3);
                SimpleDraweeView mDealEventPersonItemImg4 = (SimpleDraweeView) holder.findViewById(R.id.deal_event_person_item_img_4);
                ImageView mDealEventPersonItemDeleteImg1 = (ImageView) holder.findViewById(R.id.deal_event_person_item_delete_img_1);
                ImageView mDealEventPersonItemDeleteImg2 = (ImageView) holder.findViewById(R.id.deal_event_person_item_delete_img_2);
                ImageView mDealEventPersonItemDeleteImg3 = (ImageView) holder.findViewById(R.id.deal_event_person_item_delete_img_3);
                ImageView mDealEventPersonItemDeleteImg4 = (ImageView) holder.findViewById(R.id.deal_event_person_item_delete_img_4);

                if(mPersonIndex < 1){
                    mDealEventPersonItemTopLl.setVisibility(View.VISIBLE);
                }else{
                    mDealEventPersonItemTopLl.setVisibility(View.GONE);
                }

                doForPersonItemView(mPersonIndex * 4,mDealEventPersonItemLl1,mDealEventPersonItemImg1,mDealEventPersonItemDeleteImg1);
                doForPersonItemView(mPersonIndex * 4 + 1,mDealEventPersonItemLl2,mDealEventPersonItemImg2,mDealEventPersonItemDeleteImg2);
                doForPersonItemView(mPersonIndex * 4 + 2,mDealEventPersonItemLl3,mDealEventPersonItemImg3,mDealEventPersonItemDeleteImg3);
                doForPersonItemView(mPersonIndex * 4 + 3,mDealEventPersonItemLl4,mDealEventPersonItemImg4,mDealEventPersonItemDeleteImg4);


            }else if(holder instanceof BottomItemViewHolder){

            }
        }


        private void doForPersonItemView(final int mPersonItemIndex, View personItemLl, SimpleDraweeView personItemImg,ImageView deleteImg){
            DebugUtils.d(TAG,"doForPictureItemView::mPersonItemIndex = " + mPersonItemIndex);
            DebugUtils.d(TAG,"doForPictureItemView::mDealEventPersonPaths.size() = " + mDealEventPersonPaths.size());

            if(mPersonItemIndex < mDealEventPersonPaths.size()){
                personItemLl.setVisibility(View.VISIBLE);

                personItemImg.setImageURI(mDealEventPersonPaths.get(mPersonItemIndex));

                if(isDeletePersonMode){
                    deleteImg.setVisibility(View.VISIBLE);
                }else{
                    deleteImg.setVisibility(View.GONE);
                }
            }else if(mPersonItemIndex == mDealEventPersonPaths.size()){
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
                    DebugUtils.d(TAG,"doForPictureItemView::onClick::mDealEventPersonPaths.size() = " + mDealEventPersonPaths.size());
                    if(mPersonItemIndex == mDealEventPersonPaths.size()){
                        Intent mPersonListIntent = new Intent(mContext,PersonListActivity.class);
                        startActivityForResult(mPersonListIntent,PersonListActivity.CODE_PERSON_LIST_REQUEST);
                    }else{
                        if(isDeletePersonMode){
                            mDealEventPersonPaths.remove(mPersonItemIndex);
                            if(mDealEventPersonPaths.size() == 0){
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
                    if(mPersonItemIndex < mDealEventPersonPaths.size()){
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

    class HeadItemViewHolder extends BaseViewHolder {

        public HeadItemViewHolder(View itemView) {
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
