package com.zxl.river.chief.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zxl.river.chief.R;
import com.zxl.river.chief.common.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 17-12-3.
 */

public class PersonListActivity extends BaseActivity {

    public static final int CODE_PERSON_LIST_REQUEST = 0xa3;

    public static final String EXTRA_PERSON_LIST = "EXTRA_PERSON_LIST";

    private RecyclerView mPersonListRecyclerView;
    private PersonListAdapter mPersonListAdapter;

    private List<String> mPersonPaths = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.person_list_activity;
    }

    @Override
    public void initView() {
        super.initView();

        setRightImgVisibility(View.GONE);
        setTitle("人员列表");

        mPersonListRecyclerView = (RecyclerView) findViewById(R.id.person_list_recycler_view);
        mPersonListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPersonListAdapter = new PersonListAdapter();
        mPersonListRecyclerView.setAdapter(mPersonListAdapter);

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

        mPersonPaths.add("http://img3.duitang.com/uploads/item/201511/13/20151113110644_PcSFj.thumb.224_0.jpeg");
        mPersonPaths.add("http://diy.qqjay.com/u2/2014/1130/6272576897a2e42385ddbcf41435d938.jpg");
        mPersonPaths.add("http://www.feizl.com/upload2007/2014_01/140116182482507.jpg");
        mPersonPaths.add("http://img2.imgtn.bdimg.com/it/u=3746075707,1914896074&fm=214&gp=0.jpg");
        mPersonPaths.add("http://img3.imgtn.bdimg.com/it/u=2777008330,1289798081&fm=27&gp=0.jpg");

        mPersonListAdapter.notifyDataSetChanged();
    }

    class PersonListAdapter extends RecyclerView.Adapter<BaseViewHolder>{

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.person_list_item_view,parent,false);
            return new BaseViewHolder(contentView);
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, final int position) {
            SimpleDraweeView mPersonListItemImg = (SimpleDraweeView) holder.findViewById(R.id.person_list_item_img);
            mPersonListItemImg.setImageURI(mPersonPaths.get(position));

            holder.getContentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent();
                    mIntent.putExtra(EXTRA_PERSON_LIST,mPersonPaths.get(position));
                    setResult(RESULT_OK,mIntent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPersonPaths.size();
        }
    }
}
