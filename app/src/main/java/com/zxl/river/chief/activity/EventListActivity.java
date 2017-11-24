package com.zxl.river.chief.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxl.river.chief.R;
import com.zxl.river.chief.common.Constants;
import com.zxl.river.chief.http.data.EventData;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mac on 17-11-24.
 */

public class EventListActivity extends BaseActivity {

    private ImageView mBackImg;
    private ImageView mSettingsImg;

    private TextView mTitleTv;

    private RecyclerView mEventRecyclerView;
    private EventAdapter mEventAdapter;

    private List<EventData> mEventDatas = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.event_list_activity;
    }

    @Override
    public void initView() {

        mBackImg = (ImageView) findViewById(R.id.back_img);
        mSettingsImg = (ImageView) findViewById(R.id.settings_img);

        mTitleTv = (TextView) findViewById(R.id.title_tv);

        mEventRecyclerView = (RecyclerView) findViewById(R.id.event_recycler_view);

        mSettingsImg.setVisibility(View.GONE);
        mTitleTv.setText("事件列表");

        mBackImg.setOnClickListener(mOnClickListener);

        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mEventAdapter = new EventAdapter();
        mEventRecyclerView.setAdapter(mEventAdapter);
    }

    @Override
    public void initData() {
        for(int i = 0; i < 100; i++){
            EventData mEventData = new EventData();
            mEventData.setTitle("城区河道综合治理工作情况的回报");
            mEventData.setUploadEventName("2017.11.20");
            mEventData.setEventType(i % Constants.EVENT_TYPE_NAME.length);
            mEventData.setUploadEventName("张延");
            mEventData.setUploadEventDepartment("六合区委");
            mEventData.setEventState(i % Constants.EVENT_STATE_NAME.length);
            mEventData.setDealEventTime("2017-11-21");
            mEventDatas.add(mEventData);
        }
        mEventAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back_img:
                    finish();
                    break;
            }
        }
    };

    class EventAdapter extends RecyclerView.Adapter<EventViewHolder>{

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View mContentView = LayoutInflater.from(mContext).inflate(R.layout.event_item_view,parent,false);
            EventViewHolder mEventViewHolder = new EventViewHolder(mContentView);
            return mEventViewHolder;
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mEventDatas.size();
        }
    }

    class EventViewHolder extends RecyclerView.ViewHolder{

        private View mContentView;

        public EventViewHolder(View itemView) {
            super(itemView);
            mContentView = itemView;
        }

        public View findViewById(int id){
            return mContentView.findViewById(id);
        }
    }
}
