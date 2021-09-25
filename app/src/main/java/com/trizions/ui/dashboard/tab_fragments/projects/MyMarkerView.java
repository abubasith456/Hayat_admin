package com.trizions.ui.dashboard.tab_fragments.projects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.trizions.R;

import java.text.DecimalFormat;

@SuppressLint("ViewConstructor")
class MyMarkerView extends MarkerView {


    private final TextView textViewContent;
    private final IAxisValueFormatter xAxisValueFormatter;

    private final DecimalFormat format;

    public MyMarkerView(BarChartFragment context, IAxisValueFormatter xAxisValueFormatter) {
        super(context.getContext(), R.layout.custom_marker_view);

        this.xAxisValueFormatter = xAxisValueFormatter;
        textViewContent = findViewById(R.id.textViewContent);
        format = new DecimalFormat("###.0");
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        textViewContent.setText(String.format("x: %s, y: %s", xAxisValueFormatter.getFormattedValue(e.getX(), null), format.format(e.getY())));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}