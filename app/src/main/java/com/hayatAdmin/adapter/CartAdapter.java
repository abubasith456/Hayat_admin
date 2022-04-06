package com.hayatAdmin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.R;
import com.hayatAdmin.db.AppDatabase;
import com.hayatAdmin.db.CartItems;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItems> cartModelList;
    private Context context;
    private LayoutInflater layoutInflater;

    public CartAdapter(List<CartItems> cartModelList, Context context) {
        this.cartModelList = cartModelList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_list_cart_items, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        CartItems cartItems = cartModelList.get(position);
        holder.bind(cartItems, position);
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemLgPcs;
        ImageButton imageButton;
        Button buttonPlaceOrder;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textViewName);
            itemPrice = itemView.findViewById(R.id.textViewPrice);
            itemLgPcs = itemView.findViewById(R.id.textViewKgPcs);
            imageButton = itemView.findViewById(R.id.imageButtonDelete);

        }

        @SuppressLint("SetTextI18n")
        public void bind(CartItems cartItems, int position) {

            itemName.setText(cartItems.itemName);
            itemPrice.setText("Rs " + cartItems.itemPrice);
            itemLgPcs.setText(String.valueOf(cartItems.itemNos));

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppDatabase appDatabase = AppDatabase.getDbInstance(context);
                    CartItems cartItem = new CartItems();
                    cartItem.item_id = cartModelList.get(position).item_id;
                    appDatabase.userDao().deleteCart(cartItem);
                    cartModelList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();

                }
            });


        }
    }
}
