package com.trizions.ui.dashboard.tab_fragments.projects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.trizions.BaseActivity;
import com.trizions.BaseFragment;
import com.trizions.R;
import com.trizions.utils.PhotoPreViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProjectsTrackActivity extends BaseActivity {

    @BindView(R.id.appBarLayoutDetails)
    LinearLayout appBarLayoutDetails;
    @BindView(R.id.tabLayoutDetails)
    TabLayout tabLayoutDetails;
    @BindView(R.id.viewpagerDetails)
    ViewPager viewpagerDetails;
    @BindView(R.id.progress_barDetails)
    FrameLayout progressbarDetails;
    OnProjectsListener mCallback;
    SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_details);
        try {
            setupViewPager(viewpagerDetails);
            tabLayoutDetails.setupWithViewPager(viewpagerDetails);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ProjectsTrackActivity.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProjectsDetailsFragment(), "Current");
        //adapter.addFragment(new ProjectsDetailsFragment(), "Completed");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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
            progressbarDetails.setVisibility(View.VISIBLE);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgress() {
        try {
            progressbarDetails.setVisibility(View.GONE);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public interface OnProjectsListener {
        void onShowDetailsView(String message);
    }
}

