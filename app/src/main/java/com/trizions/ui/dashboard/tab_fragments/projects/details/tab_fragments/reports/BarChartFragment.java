package com.trizions.ui.dashboard.tab_fragments.projects.details.tab_fragments.reports;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.trizions.BaseFragment;
import com.trizions.R;

import java.util.ArrayList;

import butterknife.BindView;

public class BarChartFragment extends BaseFragment {

    @BindView(R.id.barChart)
    BarChart barChart;

    BarChartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected View createView(final LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bar_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            BarDataSet barDataSet1 = new BarDataSet(barDataSet1(), "Trizion");
            barDataSet1.setColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));

            BarData data = new BarData(barDataSet1);
            barChart.setData(data);

            barDataSet1.setValueTextSize(15f);

            DayAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);
            String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
            xAxis.setCenterAxisLabels(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(10f);
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);

            barChart.setScaleEnabled(false);
            barChart.setDragEnabled(true);
            barChart.setVisibleXRangeMaximum(3);
            barChart.getAxisLeft().setDrawGridLines(false);
            barChart.getXAxis().setDrawGridLines(false);
            barChart.getAxisRight().setDrawGridLines(false);
            barChart.getDescription().setEnabled(false);
            barChart.getAxisRight().setEnabled(false);

            float barSpace =0.40f;
            float groupSpace =0.90f;
            data.setBarWidth(0.40f);

            barChart.getXAxis().setAxisMinimum(0);
            barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * 7);
            barChart.getAxisLeft().setAxisMinimum(0);
            barChart.getXAxis().setGranularity(1);
            barChart.groupBars(0, groupSpace, barSpace);
            barChart.animateY(1000);

            Legend legend = barChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);

            MyMarkerView myMarkerView = new MyMarkerView(this, xAxisFormatter);
            myMarkerView.setChartView(barChart);
            barChart.setMarker(myMarkerView);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private ArrayList<BarEntry> barDataSet1() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 2));
        barEntries.add(new BarEntry(2, 16));
        barEntries.add(new BarEntry(3, 15));
        barEntries.add(new BarEntry(4, 23));
        barEntries.add(new BarEntry(5, 21));
        barEntries.add(new BarEntry(6, 18));
        barEntries.add(new BarEntry(7, 5));
        return barEntries;
    }
}
