package com.trizions.ui.dashboard.tab_fragments.clients;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.trizions.BaseFragment;
import com.trizions.R;
import com.trizions.firebase_model.Clients;
import com.trizions.firebase_model.Products;
import com.trizions.model.clients.ClientsResponse;
import com.trizions.model.clients.ClientsRequest;
import com.trizions.rest_client.BCRequests;
import com.trizions.utils.PhotoPreViewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientsFragment extends BaseFragment {

    @BindView(R.id.recyclerViewClients)
    RecyclerView recyclerViewClients;

    @BindView(R.id.textViewNoResult)
    TextView textViewNoResult;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    @BindView(R.id.floatingButtonAddClients)
    FloatingActionButton floatingButtonAddClients;

    OnClientsListener mCallback;
    SharedPreferences pref;
    ClientsAdapter clientsAdapter;
    ArrayList<Clients> clientsArrayList;
    FirebaseFirestore firebaseFirestore;

    ClientsResponse clientsResponse;
    String clientsData;
    String userId;
    String page;
    String perPage;
    int pageNo = 1, limit = 1;


    public ClientsFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnClientsListener) context;
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();
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

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
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


    @OnClick(R.id.floatingButtonAddClients)
    void onFloatingButtonAddClientsClick() {
        try {
            Intent addClientsIntent = new Intent(getActivity(), AddClientsActivity.class);
            startActivity(addClientsIntent);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void setUpRecyclerView() {
        try {
            clientsArrayList = new ArrayList<>();
            firebaseFirestore.collection("Clients").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    Clients clients = new Clients(documentSnapshot.getString("clientId"),
                                            documentSnapshot.getString("clientImage"),
                                            documentSnapshot.getString("clientName"),
                                            documentSnapshot.getString("clientRole"),
                                            documentSnapshot.getString("clientBusinessName"),
                                            documentSnapshot.getString("clientMobileNumber"),
                                            documentSnapshot.getString("clientEmail"),
                                            documentSnapshot.getString("clientAddress"));
                                    clientsArrayList.add(clients);
                                }
                                clientsAdapter = new ClientsAdapter(clientsArrayList, getActivity());
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                recyclerViewClients.setLayoutManager(mLayoutManager);
                                recyclerViewClients.setAdapter(clientsAdapter);
                                textViewNoResult.setVisibility(View.GONE);
                                clientsAdapter.notifyDataSetChanged();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public class ClientsAdapter extends RecyclerView.Adapter<ClientsViewHolder> {
        private ArrayList<Clients> clientsArray;
        private Activity mActivity;

        public ClientsAdapter(ArrayList<Clients> clientsArray, Activity activity) {
            this.clientsArray = clientsArray;
            mActivity = activity;
        }

        public void setBookMarksList(ArrayList<Clients> clientsArray) {
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
        @BindView(R.id.textViewProjectClientRoll)
        TextView textViewProjectClientRoll;
        @BindView(R.id.textViewBusinessName)
        TextView textViewBusinessName;
        @BindView(R.id.textViewMobileNumber)
        TextView textViewMobileNumber;
        @BindView(R.id.textViewEmail)
        TextView textViewEmail;
        @BindView(R.id.textViewAddress)
        TextView textViewAddress;
        @BindView(R.id.imageViewAvatar)
        ImageView imageViewAvatar;
        @BindView(R.id.linearLayoutClients)
        LinearLayout linearLayoutClients;

        ClientsViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void bind(final Clients response, final Activity activity) {
            try {
                if (response != null) {
                    textViewClientName.setText(response.getClientName());
                    textViewProjectClientRoll.setText(response.getClientRole());
                    textViewBusinessName.setText(response.getClientBusiness());
                    textViewMobileNumber.setText(response.getClientMobileNumber());
                    textViewEmail.setText(response.getClientEmail());
                    textViewAddress.setText(response.getClientAddress());
                    try {
                        String productImage = response.getClientImage();
                        Picasso.get().load(productImage).placeholder(R.drawable.tab_icon_about_us).into(imageViewAvatar);
                    } catch (Exception e) {
                        imageViewAvatar.setImageResource(R.drawable.tab_icon_about_us);
                    }
                }
                textViewMobileNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCallButtonAction(response.getClientMobileNumber());
                    }
                });
                textViewEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onEmailButtonAction(response.getClientEmail());
                    }
                });
                imageViewAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPhotoPreView(response.getClientImage(), getActivity());
                    }
                });

                textViewAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onAddressButtonAction(response.getClientAddress());
                    }
                });

                linearLayoutClients.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detailsBottomScreen(response);
                    }
                });

            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        private void detailsBottomScreen(Clients response) {
            try {
                BottomSheetDialog clientBottomSheetDialog = new BottomSheetDialog(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.details_bottom_screen_clients, null);
                clientBottomSheetDialog.setContentView(view);
                ImageView imageBack = view.findViewById(R.id.imageBack);
                ImageView imageEdit = view.findViewById(R.id.imageEdit);
                ImageView imageViewBottomScreenAvatar = view.findViewById(R.id.imageViewBottomScreenAvatar);
                TextView textViewBottomScreenClientName = view.findViewById(R.id.textViewBottomScreenClientName);
                TextView textViewBottomScreenClientRoll = view.findViewById(R.id.textViewBottomScreenClientRoll);
                TextView textViewBottomScreenBusinessName = view.findViewById(R.id.textViewBottomScreenBusinessName);
                TextView textViewBottomScreenMobileNumber = view.findViewById(R.id.textViewBottomScreenMobileNumber);
                TextView textViewBottomScreenEmail = view.findViewById(R.id.textViewBottomScreenEmail);
                TextView textViewBottomScreenAddress = view.findViewById(R.id.textViewBottomScreenAddress);
                TextView textViewDeleteClient = view.findViewById(R.id.textViewDeleteClient);
                try {
                    String productImage = response.getClientImage();
                    Picasso.get().load(productImage).placeholder(R.drawable.tab_icon_about_us).into(imageViewBottomScreenAvatar);
                } catch (Exception e) {
                    imageViewBottomScreenAvatar.setImageResource(R.drawable.tab_icon_about_us);
                }
                textViewBottomScreenClientName.setText(response.getClientName());
                textViewBottomScreenClientRoll.setText(response.getClientRole());
                textViewBottomScreenBusinessName.setText(response.getClientBusiness());
                textViewBottomScreenMobileNumber.setText(response.getClientMobileNumber());
                textViewBottomScreenEmail.setText(response.getClientEmail());
                textViewBottomScreenAddress.setText(response.getClientAddress());
                clientBottomSheetDialog.show();
                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clientBottomSheetDialog.dismiss();
                    }
                });

                imageEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editClientIntent = new Intent(getActivity(), EditClientsActivity.class);
                        editClientIntent.putExtra("clientId",response.getClientsId());
                        editClientIntent.putExtra("clientImage",response.getClientImage());
                        editClientIntent.putExtra("clientName",response.getClientName());
                        editClientIntent.putExtra("clientRole",response.getClientRole());
                        editClientIntent.putExtra("clientBusiness",response.getClientBusiness());
                        editClientIntent.putExtra("clientMobileNumber",response.getClientMobileNumber());
                        editClientIntent.putExtra("clientEmail",response.getClientEmail());
                        editClientIntent.putExtra("clientAddress",response.getClientAddress());
                        startActivity(editClientIntent);
                        clientBottomSheetDialog.dismiss();
                    }
                });

                textViewDeleteClient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Delete")
                                .setCancelable(false)
                                .setMessage("Are you sure you want to delete" + response.getClientName() + "?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        firebaseFirestore.collection("Clients").document(response.getClientsId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(@NonNull Void unused) {
                                                clientBottomSheetDialog.dismiss();
                                                deleteFirebaseStorageImage(response);
                                                setUpRecyclerView();
                                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                });
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        void deleteFirebaseStorageImage(Clients response) {
            try {
                FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
                StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(response.getClientImage());
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Photo deleted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void onCallButtonAction(String mobileNumber) {
            try {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "You need to allow call permission to make call from this app", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNumber));
                startActivity(intent);
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void onEmailButtonAction(String email) {
            try {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + email));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void onAddressButtonAction(String address) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + address));
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

    public interface OnClientsListener {
        void onShowDetailsView(String message);
    }
}
