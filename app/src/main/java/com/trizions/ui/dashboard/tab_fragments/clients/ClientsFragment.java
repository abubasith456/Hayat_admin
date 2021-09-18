package com.trizions.ui.dashboard.tab_fragments.clients;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.trizions.BaseFragment;
import com.trizions.R;
import com.trizions.ui.dashboard.tab_fragments.projects.CurrentProjectsFragment;
import com.trizions.utils.PhotoPreViewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientsFragment extends BaseFragment {

    @BindView(R.id.recyclerViewClients)
    RecyclerView recyclerViewClients;

    @BindView(R.id.textViewNoResult)
    TextView textViewNoResult;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    OnClientsListener mCallback;
    SharedPreferences pref;
    ArrayList<String> clientsArray = new ArrayList<>();
    ClientsAdapter clientsAdapter;

    public ClientsFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnClientsListener) context;
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
        return inflater.inflate(R.layout.fragment_clients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
        } catch (Exception exception){
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
                clientsArray.add("Project " + 1);
            clientsAdapter = new ClientsAdapter(clientsArray, getActivity());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewClients.setLayoutManager(mLayoutManager);
            recyclerViewClients.setAdapter(clientsAdapter);
            textViewNoResult.setVisibility(View.GONE);
            clientsAdapter.notifyDataSetChanged();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public class ClientsAdapter extends RecyclerView.Adapter<ClientsViewHolder> {
        private ArrayList<String> clientsArray;
        private Activity mActivity;

        public ClientsAdapter(ArrayList<String> clientsArray, Activity activity)
        {
            this.clientsArray = clientsArray;
            mActivity = activity;
        }

        public void setBookMarksList(ArrayList<String> clientsArray){
            this.clientsArray = clientsArray;
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public ClientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ClientsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_view_clients, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ClientsViewHolder holder, int position) {
            holder.bind(clientsArray.get(position), mActivity);

        }

        @Override
        public int getItemCount() {
            return clientsArray.size();
        }
    }

    public class ClientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewClientName)
        TextView textViewClientName;
        @BindView(R.id.textViewBusinessName)
        TextView textViewBusinessName;
        @BindView(R.id.textViewMobileNumber)
        TextView textViewMobileNumber;
        @BindView(R.id.textViewEmail)
        TextView textViewEmail;
        @BindView(R.id.textViewAddress)
        TextView textViewAddress;

        ClientsViewHolder(View itemView) {
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
                textViewMobileNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCallButtonAction();
                    }
                });

                textViewEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onEmailButtonAction();
                    }
                });

                textViewAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAddressButtonAction();
                    }
                });

            } catch (Exception exception){
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void onCallButtonAction() {
            try {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "You need to allow call permission to make call from this app", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91 9980919307"));
                startActivity(intent);
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void onEmailButtonAction() {
            try {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:hr_admin_010@trizions.com"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
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

    public interface OnClientsListener {
        void onShowDetailsView(String message);
    }
}
