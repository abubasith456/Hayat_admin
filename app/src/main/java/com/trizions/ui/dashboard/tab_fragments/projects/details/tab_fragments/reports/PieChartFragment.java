package com.trizions.ui.dashboard.tab_fragments.projects.details.tab_fragments.reports;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.trizions.BaseActivity;
import com.trizions.BaseFragment;
import com.trizions.R;

import java.util.ArrayList;

import butterknife.BindView;

public class PieChartFragment extends BaseFragment {

    @BindView(R.id.pieChart)
    PieChart pieChart;

    PieChartFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    protected View createView(final LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pie_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPieChartData();
    }

    private void loadPieChartData() {
        try {
            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(55, "Completed"));
            entries.add(new PieEntry(45, "Pending"));

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(ColorTemplate.rgb("#FFBB86FC"));
            colors.add(ColorTemplate.rgb("#FF6200EE"));

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setDrawValues(true);
            data.setValueFormatter(new PercentFormatter(pieChart));
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.BLACK);

            pieChart.setData(data);
            pieChart.invalidate();

            pieChart.animateY(2000, Easing.EaseInOutQuad);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setUsePercentValues(true);
            pieChart.setEntryLabelTextSize(10);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setCenterTextSize(10);
            pieChart.getDescription().setEnabled(false);

            Legend legend = pieChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setEnabled(true);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

}
