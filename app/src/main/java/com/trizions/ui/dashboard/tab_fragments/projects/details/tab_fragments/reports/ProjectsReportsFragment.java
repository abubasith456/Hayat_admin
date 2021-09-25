package com.trizions.ui.dashboard.tab_fragments.projects.details.tab_fragments.reports;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.trizions.BaseFragment;
import com.trizions.R;

import butterknife.BindView;
import butterknife.OnClick;

public class ProjectsReportsFragment extends BaseFragment {

    @BindView(R.id.buttonBarChart)
    Button buttonBarChart;
    @BindView(R.id.buttonPieChart)
    Button buttonPieChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected View createView(final LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects_reports, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.buttonBarChart)
    public void loadBarChartFragment() {
        try {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, new BarChartFragment());
            fragmentTransaction.commit();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.buttonPieChart)
    public void loadPieChartFragment() {
        try {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, new PieChartFragment());
            fragmentTransaction.commit();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }
}


