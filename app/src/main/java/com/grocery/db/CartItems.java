package com.grocery.db;

import android.widget.ImageView;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;

@Entity
public class CartItems {

    @PrimaryKey(autoGenerate = true)
    public int item_id;

    @ColumnInfo(name = "itemName")
    public String itemName;

    @ColumnInfo(name = "itemPrice")
    public String itemPrice;

    @ColumnInfo(name = "itemCategory")
    public String itemCategory;

    @ColumnInfo(name = "userId")
    public String userId;

    @ColumnInfo(name = "image_url")
    public String image_url;

//    @BindingAdapter("offlineImage")
//    public static void loadImage(ImageView view, String imageUrl) {
//        if (imageUrl != null) {
//            Glide.with(view.getContext())
//                    .load(imageUrl)
//                    .into(view);
//        } else {
//            Glide.with(view.getContext())
//                    .load(view.getResources().getDrawable(R.drawable.image_not_available))
//                    .into(view);
//        }
//    }

}
