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
import com.zxl.river.chief.utils.CommonUtils;
import com.zxl.river.chief.utils.PhotoUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zxl.river.chief.utils.PhotoUtils.CODE_GALLERY_REQUEST;


/**
 * Created by mac on 17-11-24.
 */

public class EventListActivity extends BaseActivity {

    private RecyclerView mEventRecyclerView;
    private EventAdapter mEventAdapter;

    private List<String> mEventDatas = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.event_list_activity;
    }

    @Override
    public void initView() {
        super.initView();
        mEventRecyclerView = (RecyclerView) findViewById(R.id.event_recycler_view);

        setSettingsImgVisibility(View.GONE);
        setTitle("事件列表");

        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mEventAdapter = new EventAdapter();
        mEventRecyclerView.setAdapter(mEventAdapter);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.back_img:
                        finish();
                        break;
                }
            }
        };
    }

    @Override
    public void initData() {
        super.initData();
        for(int i = 0; i < 100; i++){
            mEventDatas.add("");
        }
        mEventAdapter.notifyDataSetChanged();
    }

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
