package com.trizions.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.trizions.BaseActivity;
import com.trizions.R;
import com.trizions.dialog.CustomDialog;
import com.trizions.utils.Const;
import com.trizions.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.linearLayoutBack)
    LinearLayout linearLayoutBack;
    @BindView(R.id.editTextMobileNumber)
    EditText editTextMobileNumber;
    @BindView(R.id.mTextViewMobileNumberError)
    TextView mTextViewMobileNumberError;
    @BindView(R.id.layoutForgotPassword)
    LinearLayout layoutForgotPassword;
    @BindView(R.id.progressBar)
    FrameLayout progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        try{
            editTextMobileNumber.addTextChangedListener(new TextChange(editTextMobileNumber));
            editTextMobileNumber.setOnEditorActionListener(editorListener);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    private final TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_NEXT:
                    break;
                case EditorInfo.IME_ACTION_DONE:
                    validate(editTextMobileNumber.getText().toString().trim());
                    break;
            }
            return false;
        }
    };

    @OnClick(R.id.linearLayoutBack)
    void onBackClick(){
        try {
            Utils.hideSoftKeyboard(ForgotPasswordActivity.this);
            invalidateErrorMessages();
            finish();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutForgotPassword)
    void onForgotPasswordClick(){
        try {
            if (validate(editTextMobileNumber.getText().toString().trim())) {
                if(isNetworkConnectionAvailable()){

                }  else {
                    showCustomDialog("", getResources().getString(R.string.error_network), getResources().getString(R.string.ok), getResources().getString(R.string.confirm), null);
                }
            }
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    CustomDialog.OnDismissListener onDismissListener = () -> {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    };

    public boolean validate(String strEmail) {
        boolean valid = true;
        try {
            if (strEmail.isEmpty()) {
                editTextMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewMobileNumberError.setVisibility(View.VISIBLE);
                mTextViewMobileNumberError.setText(getResources().getString(R.string.error_mobile_number));
                valid = false;
            }
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    private void invalidateErrorMessages() {
        try {
            mTextViewMobileNumberError.setText("");
            mTextViewMobileNumberError.setVisibility(View.GONE);
            editTextMobileNumber.setText("");
            editTextMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    private class TextChange implements TextWatcher {
        View view;
        private TextChange(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                String s = charSequence.toString();
                if(view.getId() == R.id.editTextMobileNumber){
                    if(s.length() > 0) {
                        editTextMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        mTextViewMobileNumberError.setVisibility(View.GONE);
                    }
                }
            } catch (Exception exception){
                Log.e("Error ==> ", "" + exception);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

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
