package com.trizions.ui.dashboard.tab_fragments.projects;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trizions.BaseFragment;
import com.trizions.R;
import com.trizions.ui.dashboard.tab_fragments.products_and_services.ProductsAndServicesFragment;
import com.trizions.ui.dashboard.tab_fragments.projects.details.ProjectsTrackActivity;
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

    ArrayList<CurrentProjectsInfo> CurrentProjectsArray = new ArrayList<>();

    static class CurrentProjectsInfo {
        String ProjectName;
        String ProjectStatus;
        String ProjectWork;
        String ProjectAddress;

        CurrentProjectsInfo(String ProjectName, String ProjectStatus, String ProjectWork, String ProjectAddress) {
            this.ProjectName = ProjectName;
            this.ProjectStatus = ProjectStatus;
            this.ProjectWork = ProjectWork;
            this.ProjectAddress = ProjectAddress;
        }

    }

    public CurrentProjectsFragment() {

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


    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            setUpRecyclerView();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUpRecyclerView() {
        try {
            CurrentProjectsArray.clear();
            CurrentProjectsArray.add(new CurrentProjectsInfo("Trizion", "30% completed", "New lights installation and repair existing lights", "No. 3/65, 11th cross street, Mannaivakkam, Chennai"));
            CurrentProjectsArray.add(new CurrentProjectsInfo("Abu", "50% completed", "Android development", "No. 1/107 I, West street, Enangudi, Nagapattinum"));
            CurrentProjectsArray.add(new CurrentProjectsInfo("Arun", "50% completed", "Android development", "No. 1/97 A, North street, Kodavasal, Kumbagonam"));
            CurrentProjectsArray.add(new CurrentProjectsInfo("Basith", "80% completed", "Admin and Android development", "No. 2/45, Anna street, 11th cross street, Mannarkudi, Thiruvarur"));
            projectsAdapter = new ProjectsAdapter(CurrentProjectsArray, getActivity());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewProjects.setLayoutManager(mLayoutManager);
            recyclerViewProjects.setAdapter(projectsAdapter);
            textViewNoResult.setVisibility(View.GONE);
            projectsAdapter.notifyDataSetChanged();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsViewHolder> {
        private ArrayList<CurrentProjectsInfo> CurrentProjectArray;
        private Activity mActivity;

        public ProjectsAdapter(ArrayList<CurrentProjectsInfo> projectsArray, Activity activity) {
            this.CurrentProjectArray = projectsArray;
            mActivity = activity;
        }

        public void setBookMarksList(ArrayList<CurrentProjectsInfo> projectsArray) {
            this.CurrentProjectArray = projectsArray;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProjectsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_view_projects, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProjectsViewHolder holder, int position) {
            holder.bind(CurrentProjectArray.get(position), mActivity);
        }

        @Override
        public int getItemCount() {
            return CurrentProjectArray.size();
        }
    }

    public class ProjectsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detailsLinearLayout)
        LinearLayout detailsLinearLayout;
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
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void bind(final CurrentProjectsInfo response, final Activity activity) {
            try {
                if (response != null) {

                }
                textViewProjectName.setText(response.ProjectName);
                textViewProjectStatus.setText(response.ProjectStatus);
                textViewProjectDetails.setText(response.ProjectWork);
                textViewProjectAddress.setText(response.ProjectAddress);
                detailsLinearLayout = detailsLinearLayout.findViewById(R.id.detailsLinearLayout);
                detailsLinearLayout.setOnClickListener(view -> {
                    Intent intent = new Intent(getActivity(), ProjectsTrackActivity.class);
                    startActivity(intent);
                });

            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void onAddressButtonAction() {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=Sundar Nagar, Mannivakkam, Chennai, India - 600048"));
                startActivity(intent);
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        private void openPhotoPreView(String photoPreviewUrl, Activity activity) {
            try {
                Intent objIntent = new Intent(activity, PhotoPreViewActivity.class);
                objIntent.putExtra("PhotoPreviewKey", photoPreviewUrl);
                activity.startActivity(objIntent);
            } catch (Exception exception) {
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