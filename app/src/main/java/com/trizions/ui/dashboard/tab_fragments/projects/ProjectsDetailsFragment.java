package com.trizions.ui.dashboard.tab_fragments.projects;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trizions.BaseFragment;
import com.trizions.R;

import butterknife.BindView;

public class ProjectsDetailsFragment extends BaseFragment {

    @BindView(R.id.textViewProjectStatus)
    TextView textViewProjectStatus;
    @BindView(R.id.textViewProjectDetails)
    TextView textViewProjectDetails;
    @BindView(R.id.textViewProjectAddress)
    TextView textViewProjectAddress;
    @BindView(R.id.textViewDescriptionTitle)
    TextView textViewDescriptionTitle;
    @BindView(R.id.textViewProjectDescription)
    TextView textViewProjectDescription;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    public void ProjectDetailsFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View createView(final LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void showProgress() {
        try {
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgress() {
        try {
            progressBar.setVisibility(View.GONE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }
}
