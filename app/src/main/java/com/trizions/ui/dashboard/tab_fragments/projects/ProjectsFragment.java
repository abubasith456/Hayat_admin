package com.trizions.ui.dashboard.tab_fragments.projects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.trizions.BaseFragment;
import com.trizions.R;
import com.trizions.utils.PhotoPreViewActivity;
import butterknife.BindView;

public class ProjectsFragment extends BaseFragment {

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;
    OnProjectsListener mCallback;
    SharedPreferences pref;

    public ProjectsFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnProjectsListener) context;
        } catch (Exception exception){
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
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {

        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void openPhotoPreView(String photoPreviewUrl, Activity activity) {
        try {
            Intent objIntent = new Intent(activity, PhotoPreViewActivity.class);
            objIntent.putExtra("PhotoPreviewKey",photoPreviewUrl);
            activity.startActivity(objIntent);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void showProgress() {
        try {
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgress() {
        try {
            progressBar.setVisibility(View.GONE);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public interface OnProjectsListener {
        void onShowDetailsView(String message);
    }
}
