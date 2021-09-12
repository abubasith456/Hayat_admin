package com.trizions.ui.dashboard.tab_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.trizions.BaseActivity;
import com.trizions.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageChatActivity extends BaseActivity {

    @BindView(R.id.recently_used_recycler_view)
    RecyclerView recyclerViewRecentlyUsed;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_chat);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.backButton)
    void backButtonClick(){
        try {
            finish();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void showProgress() {
        try {
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgress() {
        try {
            progressBar.setVisibility(View.GONE);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }
}
