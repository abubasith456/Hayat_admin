package com.hayatAdmin.cart;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hayatAdmin.db.AppDatabase;
import com.hayatAdmin.db.CartItems;

public class Cart {

    private Context context;
    private String itemName;
    private String itemPrice;
    private double itemNos;
    private String userId;
    private String itemCategory;

    public void getCartItem(Context context, String itemName, String itemCategory, String itemPrice, double itemNos, String userId, String imageUrl) {
        this.context = context;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.userId = userId;
        this.itemNos = itemNos;
        this.itemCategory = itemCategory;

        try {
            AppDatabase db = AppDatabase.getDbInstance(context);
            CartItems cartItems = new CartItems();
            cartItems.item_id = 0;
            cartItems.itemName = itemName;
            cartItems.itemCategory = itemCategory;
            cartItems.itemPrice = itemPrice;
            cartItems.itemNos = itemNos;
            cartItems.userId = userId;
            cartItems.image_url = imageUrl;
            db.userDao().insertCart(cartItems);
            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("Db==> ", e.getMessage());
        }


    }


}
