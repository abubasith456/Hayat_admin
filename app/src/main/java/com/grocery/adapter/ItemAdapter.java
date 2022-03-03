package com.grocery.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<String> itemName;
    private List<Integer> itemImage;
    private Context context;
    private LayoutInflater layoutInflater;

    public ItemAdapter(Context context, List<String> itemName, List<Integer> itemImage) {
        this.context = context;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_view_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        holder.itemName.setText(itemName.get(position));
        holder.itemImage.setImageResource(itemImage.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemName.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        ImageView itemImage;
        LinearLayout linearLayout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName=itemView.findViewById(R.id.textViewItemName);
            itemImage=itemView.findViewById(R.id.imageViewItems);
            linearLayout=itemView.findViewById(R.id.linearLayoutItemClick);

        }
    }
}
