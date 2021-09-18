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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trizions.BaseFragment;
import com.trizions.R;
import com.trizions.ui.dashboard.tab_fragments.products_and_services.ProductsAndServicesFragment;
import com.trizions.utils.PhotoPreViewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentProjectsFragment extends BaseFragment {

    @BindView(R.id.recyclerViewProjects)
    RecyclerView recyclerViewProjects;

    @BindView(R.id.textViewNoResult)
    TextView textViewNoResult;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    ProjectsAdapter projectsAdapter;

    ProductsAndServicesFragment.OnProductsAndServicesListener mCallback;
    SharedPreferences pref;
    String searchText = "";

    ArrayList<String> projectsArray = new ArrayList<>();


    public CurrentProjectsFragment() {

    }

    public CurrentProjectsFragment(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (ProductsAndServicesFragment.OnProductsAndServicesListener) context;
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
        return inflater.inflate(R.layout.fragment_current_projects, container, false);
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
        try {
            setUpRecyclerView();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void setUpRecyclerView() {
        try {
            for(int i = 1; i <= 10; i++)
                projectsArray.add("Project " + 1);
            projectsAdapter = new ProjectsAdapter(projectsArray, getActivity());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewProjects.setLayoutManager(mLayoutManager);
            recyclerViewProjects.setAdapter(projectsAdapter);
            textViewNoResult.setVisibility(View.GONE);
            projectsAdapter.notifyDataSetChanged();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsViewHolder> {
        private ArrayList<String> bookMarksList;
        private Activity mActivity;

        public ProjectsAdapter(ArrayList<String> bookMarksList, Activity activity)
        {
            this.bookMarksList = bookMarksList;
            mActivity = activity;
        }

        public void setBookMarksList(ArrayList<String> bookMarksList){
            this.bookMarksList = bookMarksList;
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProjectsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_view_projects, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProjectsViewHolder holder, int position) {
            holder.bind(bookMarksList.get(position), mActivity);
        }

        @Override
        public int getItemCount() {
            return bookMarksList.size();
        }
    }

    public class ProjectsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewProjectName)
        TextView textViewProjectName;
        @BindView(R.id.textViewProjectStatus)
        TextView textViewProjectStatus;
        @BindView(R.id.textViewProjectDetails)
        TextView textViewProjectDetails;
        @BindView(R.id.textViewProjectAddress)
        TextView textViewProjectAddress;

        ProjectsViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception exception){
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void bind(final String response, final Activity activity) {
            try {
                if (response != null) {

                }

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