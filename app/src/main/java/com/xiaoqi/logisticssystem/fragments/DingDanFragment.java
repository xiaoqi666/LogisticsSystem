package com.xiaoqi.logisticssystem.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ping.greendao.gen.CustomerDao;
import com.ping.greendao.gen.ExpressDao;
import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.Express;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DingDanFragment extends Fragment {
    @BindView(R.id.mPieChart)
    PieChart mPieChart;
    Unbinder unbinder;
    private static final String[] types = new String[]{"圆通", "申通", "韵达", "中通", "EMS", "天天", "汇通", "邮政"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_dingdan, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initChart();
    }

    private void initChart() {
        List<PieEntry> yValues = new ArrayList<>();

        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            List<Express> list = AppClient.getDaoSession().getExpressDao().queryBuilder().where(ExpressDao.Properties.Type.eq(type)).list();
            if (list.size() > 0) {
                yValues.add(new PieEntry(list.size(), type));
            }//1540167152406
        }

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setValueTextSize(20);
        dataSet.setDrawValues(true);
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat("###");
                return decimalFormat.format(value);
            }
        });


        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.DKGRAY);
        colors.add(Color.LTGRAY);
        colors.add(Color.WHITE);
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        mPieChart.setDrawHoleEnabled(false);
        Description description = new Description();
        description.setText("");
        mPieChart.setDescription(description);
        mPieChart.setData(pieData);
        mPieChart.invalidate();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
