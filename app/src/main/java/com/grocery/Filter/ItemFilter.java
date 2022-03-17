package com.grocery.Filter;

import android.widget.Filter;

import com.grocery.firebase_model.Products;
import com.grocery.ui.dashboard.tab_fragments.items.ItemsFragment;

import java.util.ArrayList;

public class ItemFilter extends Filter {

    private ItemsFragment.ProductsAdapter itemAdapter;
    private ArrayList<Products> filterList;

    public ItemFilter(ItemsFragment.ProductsAdapter itemAdapter, ArrayList<Products> filterList) {
        this.itemAdapter = itemAdapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        //validate data from search query
        if (constraint != null && constraint.length() > 0) {
            //search field not empty,search perform show some
            //change case to uppercase, for sensitive
            constraint = constraint.toString().toUpperCase();
            //store our filter list
            ArrayList<Products> adminItems = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++) {
                //check
                if (filterList.get(i).getProductName().toUpperCase().contains(constraint) ||
                        filterList.get(i).getProductName().toUpperCase().contains(constraint)) {
                    //add filter data list
                    adminItems.add(filterList.get(i));
                }
            }
            filterResults.count = adminItems.size();
            filterResults.values = adminItems;
        } else {
            //search field empty,not search show orginal values
            filterResults.count = filterList.size();
            filterResults.values = filterList;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        itemAdapter.productsArray = (ArrayList<Products>) results.values;
        itemAdapter.notifyDataSetChanged();

    }
}
