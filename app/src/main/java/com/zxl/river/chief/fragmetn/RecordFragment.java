package com.zxl.river.chief.fragmetn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zxl.river.chief.R;
import com.zxl.river.chief.activity.CountActivity;
import com.zxl.river.chief.activity.NotificationDetailActivity;
import com.zxl.river.chief.activity.RecordDetailActivity;
import com.zxl.river.chief.activity.TrajectoryPlaybackActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 17-12-3.
 */

public class RecordFragment extends BaseFragment {

    private RecyclerView mRecordRecyclerView;
    private RecordAdapter mRecordAdapter;

    private List<String> mRecordDatas = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.record_fragment;
    }

    @Override
    public void initView(View contentView, Bundle savedInstanceState) {
        super.initView(contentView,savedInstanceState);
        mRecordRecyclerView = (RecyclerView) contentView.findViewById(R.id.record_recycler_view);

        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecordAdapter = new RecordAdapter();
        mRecordRecyclerView.setAdapter(mRecordAdapter);



        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){

                }
            }
        };

        setTitleBarVisibility(View.GONE);
    }

    @Override
    public void initData() {
        super.initData();
        for(int i = 0; i < 100; i++){
            mRecordDatas.add("");
        }
        mRecordAdapter.notifyDataSetChanged();
    }

    class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder>{

        @Override
        public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mContentView = LayoutInflater.from(mContext).inflate(R.layout.record_item_view,parent,false);
            RecordViewHolder mRecordViewHolder = new RecordViewHolder(mContentView);
            return mRecordViewHolder;
        }

        @Override
        public void onBindViewHolder(RecordViewHolder holder, int position) {
            SimpleDraweeView mRecordItemImg = (SimpleDraweeView) holder.findViewById(R.id.record_item_img);
            mRecordItemImg.setImageURI("http://img2.imgtn.bdimg.com/it/u=3746075707,1914896074&fm=214&gp=0.jpg");

            holder.getContentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mUploadEventIntent = new Intent(mContext,RecordDetailActivity.class);
                    mUploadEventIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mUploadEventIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRecordDatas.size();
        }
    }

    class RecordViewHolder extends RecyclerView.ViewHolder{

        private View mContentView;

        public RecordViewHolder(View itemView) {
            super(itemView);
            mContentView = itemView;
        }

        public View getContentView(){
            return mContentView;
        }

        public View findViewById(int id){
            return mContentView.findViewById(id);
        }
    }
}
