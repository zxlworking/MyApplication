package com.zxl.river.chief.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mac on 17-12-2.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder{

    private View mContentView;

    public BaseViewHolder(View itemView) {
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