package com.grocery.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.BaseActivity;
import com.grocery.R;

import butterknife.BindView;
import butterknife.OnClick;

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.search_recycler)
    RecyclerView recyclerViewSearch;

    @BindView(R.id.empty_text)
    TextView emptyText;

    @BindView(R.id.progress_bar)
    FrameLayout progressBar;

    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

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
