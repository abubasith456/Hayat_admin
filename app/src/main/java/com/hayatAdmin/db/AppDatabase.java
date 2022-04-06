package com.hayatAdmin.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {CartItems.class,OrderedItems.class}, version  = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CartDao userDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {

        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "CART_ITEMS")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }
}
