package com.hayatAdmin.ui.dashboard.tab_fragments.items;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.hayatAdmin.BaseFragment;
import com.hayatAdmin.Filter.ItemFilter;
import com.grocery.R;
import com.hayatAdmin.firebase_model.Products;
import com.hayatAdmin.model.Category;
import com.hayatAdmin.utils.PhotoPreViewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemsFragment extends BaseFragment {

    @BindView(R.id.recyclerViewClients)
    RecyclerView recyclerViewClients;

    @BindView(R.id.textViewNoResult)
    TextView textViewNoResult;

    @BindView(R.id.imageViewFilter)
    ImageView imageViewFilter;

    @BindView(R.id.editTextSearchItem)
    EditText editTextSearchItem;

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

    public ItemsFragment() {

    }

    public ItemsFragment(String searchText) {
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
//            editTextSearchItem.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    try {
//                        productsAdapter.getFilter().filter(charSequence);
//                    } catch (Exception e) {
//                        Log.e("SearchText===>", e.getMessage());
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });
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

    @OnClick(R.id.imageViewFilter)
    void onFilterButtonClick() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Select Category")
                    .setItems(Category.selectCategory, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String selectedCategory = Category.selectCategory[which];
                            loadFilterItems(selectedCategory);
//                            textViewCategoryName.setText(selectedCategory);
//                        if (selectedCategory.equals("All")) {
//                            loadAllItems();
//                        } else {
//
//                        }
                        }
                    }).show();
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


    private void loadFilterItems(String selectedCategory) {
        try {
            productsArrayList = new ArrayList<>();
            firebaseFirestore.collection(selectedCategory).orderBy("itemName", Query.Direction.ASCENDING)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        productsArrayList.clear();
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Products products = new Products(documentSnapshot.getString("itemName"),
                                    documentSnapshot.getString("itemPrice"),
                                    documentSnapshot.getString("itemImage"),
                                    documentSnapshot.getString("itemId"),
                                    documentSnapshot.getString("itemCategory"),
                                    documentSnapshot.getString("userId"));
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

    public void setUpRecyclerView() {
        try {
            productsArrayList = new ArrayList<>();
            firebaseFirestore.collection("Biscuits").orderBy("itemName", Query.Direction.ASCENDING)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Products products = new Products(documentSnapshot.getString("itemName"),
                                    documentSnapshot.getString("itemPrice"),
                                    documentSnapshot.getString("itemImage"),
                                    documentSnapshot.getString("itemId"),
                                    documentSnapshot.getString("itemCategory"),
                                    documentSnapshot.getString("userId"));
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

    public class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> implements Filterable {
        public ArrayList<Products> productsArray, filterList;
        private Activity mActivity;
        public ItemFilter itemFilter;

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

        @Override
        public Filter getFilter() {

            if (itemFilter == null) {
                itemFilter = new ItemFilter(this, filterList);
            }
            return itemFilter;
        }
    }


    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        //
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

        @SuppressLint("SetTextI18n")
        public void bind(final Products response, final Activity activity) {
            try {
                if (response != null) {
                    String category = response.getProductCategory();
                    textViewProjectName.setText(response.getProductName());
                    if (category.equals("Biscuits")) {
                        textViewProjectDetails.setText("Per Pcs: " + response.getProductDetails() + "Rs");
                    } else {
                        textViewProjectDetails.setText("Per Kg: " + response.getProductDetails() + "Rs");
                    }
                    try {
                        String productImage = response.getProductImage();
                        Picasso.get().load(productImage).placeholder(R.drawable.logo).into(imageViewProductsImage);
                    } catch (Exception e) {
                        imageViewProductsImage.setImageResource(R.drawable.logo);
                    }
                    imageViewProductsImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openPhotoPreView(response.getProductImage(), getActivity());
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
            Button editButton = view.findViewById(R.id.buttonEdit);

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

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent editProductsIntent = new Intent(getActivity(), EditProductsAndServicesActivity.class);
                        editProductsIntent.putExtra("productId", response.getProductId());
                        editProductsIntent.putExtra("productName", response.getProductName());
                        editProductsIntent.putExtra("productDetails", response.getProductDetails());
                        editProductsIntent.putExtra("productImage", response.getProductImage());
                        editProductsIntent.putExtra("productCategory", response.getProductCategory());
                        startActivity(editProductsIntent);
                        bottomSheetDialog.dismiss();

                    } catch (Exception exception) {
                        Log.e("Error ==> ", "" + exception);
                    }
                }
            });

            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Delete")
                            .setCancelable(false)
                            .setMessage("Are you sure you want to delete" + response.getProductName() + "?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseFirestore.collection(response.getProductCategory()).document(response.getProductId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
