package com.trizions.ui.dashboard.tab_fragments.products_and_services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.trizions.BaseFragment;
import com.trizions.R;
import com.trizions.firebase_model.Products;
import com.trizions.utils.PhotoPreViewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductsAndServicesFragment extends BaseFragment {

    @BindView(R.id.recyclerViewClients)
    RecyclerView recyclerViewClients;

    @BindView(R.id.textViewNoResult)
    TextView textViewNoResult;

    @BindView(R.id.floatingButtonAddProducts)
    FloatingActionButton floatingButtonAddProducts;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    OnProductsAndServicesListener mCallback;
    SharedPreferences pref;
    String searchText = "";
    ProductsAdapter productsAdapter;
    ArrayList<Products> productsArrayList;
    FirebaseFirestore firebaseFirestore;

    class ProductInfo {
        String productName;
        String productDetails;

        ProductInfo(String productName, String productDetails) {
            this.productName = productName;
            this.productDetails = productDetails;
        }
    }

    public ProductsAndServicesFragment() {

    }

    public ProductsAndServicesFragment(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnProductsAndServicesListener) context;
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
        return inflater.inflate(R.layout.fragment_products_and_services, container, false);
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

    @OnClick(R.id.floatingButtonAddProducts)
    void onFloatingButtonAddProductsClick() {
        try {
            Intent addProductsIntent = new Intent(getContext(), AddProductsAndServicesActivity.class);
            startActivity(addProductsIntent);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void setUpRecyclerView() {
        try {
            productsArrayList = new ArrayList<>();
//            productsAdapter = new ProductsAdapter(productsArrayList, getActivity());
            firebaseFirestore.collection("Products").orderBy("productName", Query.Direction.ASCENDING)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Products products = new Products(documentSnapshot.getString("productName"),
                                    documentSnapshot.getString("productDetails"),
                                    documentSnapshot.getString("productImage"),
                                    documentSnapshot.getString("productId"));
                            productsArrayList.add(products);
                        }
                        productsAdapter = new ProductsAdapter(productsArrayList, getActivity());
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerViewClients.setLayoutManager(mLayoutManager);
                        recyclerViewClients.setAdapter(productsAdapter);
                        textViewNoResult.setVisibility(View.GONE);
                        productsAdapter.notifyDataSetChanged();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }


    public class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {
        private ArrayList<Products> productsArray;
        private Activity mActivity;

        public ProductsAdapter(ArrayList<Products> productsArray, Activity activity) {
            this.productsArray = productsArray;
            mActivity = activity;
        }

        public void setBookMarksList(ArrayList<Products> clientsArray) {
            this.productsArray = clientsArray;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_view_product_and_services, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
            holder.bind(productsArray.get(position), mActivity);

        }

        @Override
        public int getItemCount() {
            return productsArray.size();
        }
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewProjectName)
        TextView textViewProjectName;
        @BindView(R.id.textViewProjectDetails)
        TextView textViewProjectDetails;
        @BindView(R.id.imageViewProductsImage)
        ImageView imageViewProductsImage;
        @BindView(R.id.linearLayoutProductsList)
        LinearLayout linearLayoutProductsList;

        ProductsViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void bind(final Products response, final Activity activity) {
            try {
                if (response != null) {
                    textViewProjectName.setText(response.getProductName());
                    textViewProjectDetails.setText(response.getProductDetails());
                    try {
                        String productImage = response.getProductImage();
                        Picasso.get().load(productImage).placeholder(R.drawable.logo).into(imageViewProductsImage);
                    } catch (Exception e) {
                        imageViewProductsImage.setImageResource(R.drawable.logo);
                    }
                    imageViewProductsImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openPhotoPreView(response.getProductImage(),getActivity());
                        }
                    });
                    linearLayoutProductsList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            detailsBottomScreen(response);
                        }
                    });
                }
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }
    }

    public void detailsBottomScreen(Products response) {
        try {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            View view = LayoutInflater.from(getContext()).inflate(R.layout.details_bottom_screen_products, null);
            bottomSheetDialog.setContentView(view);
            ImageView imageBack = view.findViewById(R.id.imageBack);
            ImageView imageEdit = view.findViewById(R.id.imageEdit);
            ImageView imageViewProductImage = view.findViewById(R.id.imageViewProductImage);
            TextView textViewProductName = view.findViewById(R.id.textViewProductName);
            TextView textViewIProductDetails = view.findViewById(R.id.textViewIProductDetails);
            TextView textViewDelete = view.findViewById(R.id.textViewDelete);

            textViewProductName.setText(response.getProductName());
            textViewIProductDetails.setText(response.getProductDetails());
            try {
                String productImage = response.getProductImage();
                Picasso.get().load(productImage).placeholder(R.drawable.logo).into(imageViewProductImage);
            } catch (Exception e) {
                imageViewProductImage.setImageResource(R.drawable.logo);
            }
            bottomSheetDialog.show();

            imageBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                }
            });

            imageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editProductsIntent = new Intent(getActivity(), EditProductsAndServicesActivity.class);
                    editProductsIntent.putExtra("productId", response.getProductId());
                    editProductsIntent.putExtra("productName", response.getProductName());
                    editProductsIntent.putExtra("productDetails", response.getProductDetails());
                    editProductsIntent.putExtra("productImage", response.getProductImage());
                    startActivity(editProductsIntent);
                    bottomSheetDialog.dismiss();
                }
            });
            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Delete")
                            .setCancelable(false)
                            .setMessage("Are you sure you want to delete" + response.getProductName() + "?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseFirestore.collection("Products").document(response.getProductId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(@NonNull Void unused) {
                                            bottomSheetDialog.dismiss();
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

    void deleteFirebaseStorageImage(Products response) {
        try {
            FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
            StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(response.getProductImage());
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

    private void openPhotoPreView(String photoPreviewUrl, Activity activity) {
        try {
            Intent objIntent = new Intent(activity, PhotoPreViewActivity.class);
            objIntent.putExtra("PhotoPreviewKey", photoPreviewUrl);
            activity.startActivity(objIntent);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
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

    public interface OnProductsAndServicesListener {
        void onShowDetailsView(String id);
    }
}
