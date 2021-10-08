package com.trizions.ui.dashboard.tab_fragments.projects.details.tab_fragments.boq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.trizions.BaseActivity;
import com.trizions.R;
import com.trizions.ui.dashboard.tab_fragments.about_us.AboutUsFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MaterialSpecificationActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_specification);

    }

    @OnClick(R.id.buttonBackArrow)
    void backButtonClick(){
        try {
           finish();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }


}