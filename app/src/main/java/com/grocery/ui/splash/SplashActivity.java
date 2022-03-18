package com.grocery.ui.splash;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.grocery.R;
import com.grocery.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler=new Handler();
        handler.postDelayed(() -> {
            Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        },3000);
    }
}
