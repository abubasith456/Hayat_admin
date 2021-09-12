package com.trizions.ui.dashboard.tab_fragments.products_and_services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.trizions.BaseFragment;
import com.trizions.R;

import butterknife.BindView;

public class ProductsAndServicesFragment extends BaseFragment {

    @BindView(R.id.search_recycler)
    RecyclerView recyclerViewSearch;

    @BindView(R.id.empty_text)
    TextView emptyText;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    OnProductsAndServicesListener mCallback;
    SharedPreferences pref;
    String searchText = "";

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
