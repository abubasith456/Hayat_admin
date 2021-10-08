package com.trizions.ui.dashboard.tab_fragments.projects.details.tab_fragments.boq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.trizions.BaseFragment;
import com.trizions.R;
import com.trizions.ui.dashboard.tab_fragments.projects.details.ProjectsTrackActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class BoqFragment extends BaseFragment {

    @BindView(R.id.linearLayoutTable)
    LinearLayout linearLayoutTable;
    @BindView(R.id.progress_bar)
    FrameLayout progressbar;
    @BindView(R.id.textViewMaterialSpecification)
    TextView textViewMaterialSpecification;

    public void BoqFragment() {
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
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View createView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_boq,container,false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        try{

        } catch(Exception exception){
            Log.e("Error ==>", "" + exception);
        }
    }

    @OnClick(R.id.textViewMaterialSpecification)
    void onMaterialSpecificationClick(){
        Intent intent = new Intent(getActivity(), MaterialSpecificationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void showProgress() {
        try {
            progressbar.setVisibility(View.VISIBLE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgress() {
        try {
            progressbar.setVisibility(View.GONE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }
}




