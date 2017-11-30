package com.zxl.river.chief.fragmetn;

import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.zxl.river.chief.R;
import com.zxl.river.chief.utils.MPChartHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 17-11-21.
 */

public class CountFragment extends BaseFragment {

    private LineChart lineChart;
    private List<String> xAxisValues;
    private List<String> titles;
    private List<List<Float>> yAxisValues;


    @Override
    public int getContentView() {
        return R.layout.count_fragment;
    }

    @Override
    public void initView(View contentView, Bundle savedInstanceState) {
        super.initView(contentView,savedInstanceState);
        lineChart = (LineChart)contentView.findViewById(R.id.lineChart);
    }

    @Override
    public void initData() {
        super.initData();
        xAxisValues = new ArrayList<>();
        List<Float> yAxisValues1 = new ArrayList<>();
        List<Float> yAxisValues2 = new ArrayList<>();
        List<Float> yAxisValues3 = new ArrayList<>();
        for(int i= 0; i < 7; ++i){
            xAxisValues.add(String.valueOf(i));
            yAxisValues1.add((float)(Math.random()*20+20));
            yAxisValues2.add((float)(Math.random()*20+40));
            yAxisValues3.add((float)(Math.random()*20+60));
        }

        yAxisValues = new ArrayList<>();
        yAxisValues.add(yAxisValues1);
        yAxisValues.add(yAxisValues2);
        yAxisValues.add(yAxisValues3);

        titles = new ArrayList<>();

        titles.add("折线1");
        titles.add("折线2");
        titles.add("折线3");

        MPChartHelper.setLinesChart(lineChart,xAxisValues,yAxisValues,titles,false,null);
    }
}
