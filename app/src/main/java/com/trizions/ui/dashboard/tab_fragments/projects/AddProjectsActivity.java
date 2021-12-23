package com.trizions.ui.dashboard.tab_fragments.projects;

import static android.view.View.GONE;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trizions.BaseActivity;
import com.trizions.R;

import butterknife.BindView;

public class AddProjectsActivity extends BaseActivity {

    @BindView(R.id.imageViewUploadProjectImage)
    ImageView imageViewUploadProjectImage;
    @BindView(R.id.textViewSelectImage)
    TextView textViewSelectImage;
    @BindView(R.id.editTextAddProjectName)
    EditText editTextAddProjectName;
    @BindView(R.id.editTextAddProjectStatus)
    EditText editTextAddProjectStatus;
    @BindView(R.id.editTextAddProjectDetails)
    EditText editTextAddProjectDetails;
    @BindView(R.id.editTextAddProjectAddress)
    EditText editTextAddProjectAddress;
    @BindView(R.id.mTextViewErrorProjectName)
    TextView mTextViewErrorProjectName;
    @BindView(R.id.mTextViewErrorProjectStatus)
    TextView mTextViewErrorProjectStatus;
    @BindView(R.id.mTextViewErrorProjectDetails)
    TextView mTextViewErrorProjectDetails;
    @BindView(R.id.mTextViewErrorProjectAddress)
    TextView mTextViewErrorProjectAddress;
    @BindView(R.id.layoutNext)
    LinearLayout layoutNext;
    @BindView(R.id.progressBar)
    FrameLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_projects);
    }

    private boolean validate(String name,String status,String details,String address){
        boolean valid = true;
        try {
            if (name.isEmpty()) {
                editTextAddProjectName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorProjectName.setVisibility(View.VISIBLE);
                mTextViewErrorProjectName.setText(getResources().getString(R.string.error_client_name));
                valid = false;
            } else {
                editTextAddProjectName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorProjectName.setVisibility(GONE);
            }
            if (status.isEmpty()) {
                editTextAddProjectStatus.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorProjectStatus.setVisibility(View.VISIBLE);
                mTextViewErrorProjectStatus.setText(getResources().getString(R.string.error_client_role));
                valid = false;
            } else {
                editTextAddProjectStatus.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorProjectStatus.setVisibility(GONE);
            }
            if (details.isEmpty()) {
                editTextAddProjectDetails.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorProjectDetails.setVisibility(View.VISIBLE);
                mTextViewErrorProjectDetails.setText(getResources().getString(R.string.error_client_bussiness));
                valid = false;
            } else {
                editTextAddProjectDetails.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorProjectDetails.setVisibility(GONE);
            }
            if (address.isEmpty()) {
                editTextAddProjectAddress.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorProjectAddress.setVisibility(View.VISIBLE);
                mTextViewErrorProjectAddress.setText(getResources().getString(R.string.error_client_mobile));
                valid = false;
            } else {
                editTextAddProjectAddress.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorProjectAddress.setVisibility(GONE);
            }

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

}