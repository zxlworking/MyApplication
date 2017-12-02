package com.zxl.river.chief.fragmetn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxl.river.chief.R;
import com.zxl.river.chief.activity.NotificationDetailActivity;
import com.zxl.river.chief.activity.UploadEventActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 17-12-2.
 */

public class NotificationFragment extends BaseFragment {

    private RecyclerView mEventRecyclerView;
    private NotificationAdapter mNotificationAdapter;

    private List<String> mNotificationDatas = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.event_fragment;
    }

    @Override
    public void initView(View contentView, Bundle savedInstanceState) {
        super.initView(contentView,savedInstanceState);
        mEventRecyclerView = (RecyclerView) contentView.findViewById(R.id.event_recycler_view);

        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mNotificationAdapter = new NotificationAdapter();
        mEventRecyclerView.setAdapter(mNotificationAdapter);

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
            mNotificationDatas.add("");
        }
        mNotificationAdapter.notifyDataSetChanged();
    }

    class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder>{

        @Override
        public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mContentView = LayoutInflater.from(mContext).inflate(R.layout.notification_item_view,parent,false);
            NotificationViewHolder mEventViewHolder = new NotificationViewHolder(mContentView);
            return mEventViewHolder;
        }

        @Override
        public void onBindViewHolder(NotificationViewHolder holder, int position) {
            holder.getContentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mUploadEventIntent = new Intent(mContext,NotificationDetailActivity.class);
                    mUploadEventIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mUploadEventIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mNotificationDatas.size();
        }
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{

        private View mContentView;

        public NotificationViewHolder(View itemView) {
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
