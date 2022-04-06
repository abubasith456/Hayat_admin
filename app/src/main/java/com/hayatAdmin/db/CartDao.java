package com.hayatAdmin.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDao {

    @Query("SELECT * FROM CartItems")
    List<CartItems> getAllCart();

    @Query("SELECT * FROM OrderedItems")
    List<OrderedItems> getAllOrderedItems();

    @Insert
    void insertCart(CartItems... cartItems);

    @Insert
    void insertCart(OrderedItems... orderedItems);

    @Update
    void updateCart(CartItems cartItems);

    @Update
    void updateCart(OrderedItems... orderedItems);

    @Delete
    void deleteCart(CartItems cartItems);

    @Delete
    void deleteCart(OrderedItems orderedItems);
}
