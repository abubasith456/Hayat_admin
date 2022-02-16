package com.grocery.utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.grocery.R;

/**
 * Created by Siva Bala.
 */
public class PhotoPreViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        initialize();
    }

    private void initialize() {
        try {
            Intent intent = getIntent();
            String imgUrl = intent.getStringExtra("PhotoPreviewKey");
            PhotoView mPhotoView = (PhotoView) findViewById(R.id.imageView);
            LinearLayout linearLayoutBack = (LinearLayout) findViewById(R.id.linear_layout_back);
            linearLayoutBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoPreViewActivity.this.finish();
                }
            });
            if(imgUrl != null && !imgUrl.equalsIgnoreCase(""))
                Glide.with(this).load(imgUrl).placeholder(R.drawable.logo).error(R.drawable.logo).into(mPhotoView);
            else
                Toast.makeText(this, "Image preview is not available", Toast.LENGTH_LONG).show();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

}





