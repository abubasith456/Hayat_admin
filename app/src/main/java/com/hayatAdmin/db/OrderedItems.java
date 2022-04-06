package com.hayatAdmin.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OrderedItems {

    @PrimaryKey(autoGenerate = true)
    public int item_id;

    @ColumnInfo(name = "itemName")
    public String itemName;

    @ColumnInfo(name = "itemPrice")
    public String itemPrice;

    @ColumnInfo(name = "itemCategory")
    public String itemCategory;

    @ColumnInfo(name = "timeStamp")
    public String timeStamp;

    @ColumnInfo(name = "image_url")
    public String image_url;
}
