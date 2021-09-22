package com.trizions.ui.dashboard.tab_fragments.projects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.trizions.BaseFragment;
import com.trizions.R;
import com.trizions.utils.PhotoPreViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProjectsFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

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
        setRetainInstance(true);
    }

    @Override
    protected View createView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new CurrentProjectsFragment(), "Current");
        adapter.addFragment(new CompletedProjectsFragment(), "Completed");
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
