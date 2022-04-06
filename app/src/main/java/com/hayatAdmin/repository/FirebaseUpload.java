package com.hayatAdmin.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hayatAdmin.db.CartItems;

import java.util.HashMap;
import java.util.List;

public class FirebaseUpload {

    public void uploadOrders(Context context, List<CartItems> cartItems) {

        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            String timeStamp = "" + System.currentTimeMillis();

            for (int i=0;i<cartItems.size();i++){

            }

            HashMap<String, Object> addFieldInfo = new HashMap<>();
            addFieldInfo.put("orderId", "" + timeStamp);
//            addFieldInfo.put("productImage", "");//No image
//            addFieldInfo.put("productName", "" +);
//            addFieldInfo.put("productDetails", "" +);
            addFieldInfo.put("userId", "" + firebaseAuth.getCurrentUser());
            DocumentReference databaseReference = firebaseFirestore.collection("Orders").document(timeStamp);
            databaseReference.set(addFieldInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(@NonNull Void unused) {
                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception exception) {
            Log.e("Error Add item==> ", "" + exception);
        }


    }


}
