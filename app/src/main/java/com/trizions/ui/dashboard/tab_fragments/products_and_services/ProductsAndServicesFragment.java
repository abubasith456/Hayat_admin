package com.trizions.ui.dashboard.tab_fragments.products_and_services;

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
import com.trizions.ui.dashboard.tab_fragments.clients.ClientsFragment;
import com.trizions.utils.PhotoPreViewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAndServicesFragment extends BaseFragment {

    @BindView(R.id.recyclerViewClients)
    RecyclerView recyclerViewClients;

    @BindView(R.id.textViewNoResult)
    TextView textViewNoResult;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    OnProductsAndServicesListener mCallback;
    SharedPreferences pref;
    String searchText = "";
    ArrayList<ProductInfo> productsArray = new ArrayList<>();
    ProductsAdapter productsAdapter;

    class ProductInfo{
        String productName;
        String productDetails;
        ProductInfo(String productName, String productDetails){
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
        return inflater.inflate(R.layout.fragment_products_and_services, container, false);
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
            productsArray.clear();
            productsArray.add(new ProductInfo("Waste Management", "Waste management is the activities and actions required to manage waste from its inception to its disposal. This includes the collection, transport, treatment and disposal of waste, together with monitoring and regulation of the waste management process."));
            productsArray.add(new ProductInfo("Internet of Things", "The Internet of Things describes physical objects, that are embedded with sensors, processing ability, software, and other technologies, and that connect and exchange data with other devices and systems over the Internet or other communications networks."));
            productsArray.add(new ProductInfo("Machine Learning", "Machine learning is the study of computer algorithms that can improve automatically through experience and by the use of data. It is seen as a part of artificial intelligence."));
            productsAdapter = new ProductsAdapter(productsArray, getActivity());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewClients.setLayoutManager(mLayoutManager);
            recyclerViewClients.setAdapter(productsAdapter);
            textViewNoResult.setVisibility(View.GONE);
            productsAdapter.notifyDataSetChanged();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {
        private ArrayList<ProductInfo> productsArray;
        private Activity mActivity;

        public ProductsAdapter(ArrayList<ProductInfo> productsArray, Activity activity)
        {
            this.productsArray = productsArray;
            mActivity = activity;
        }

        public void setBookMarksList(ArrayList<ProductInfo> clientsArray){
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

        ProductsViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception exception){
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void bind(final ProductInfo response, final Activity activity) {
            try {
                if (response != null) {

                }
                textViewProjectName.setText(response.productName);
                textViewProjectDetails.setText(response.productDetails);
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

    public interface OnProductsAndServicesListener {
        void onShowDetailsView(String id);
    }
}
