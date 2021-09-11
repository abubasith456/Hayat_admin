package com.trizions.ui.login;

import android.content.Intent;
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
import com.trizions.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.editTextMobileNumberInput)
    EditText editTextMobileNumberInput;
    @BindView(R.id.mTextViewErrorMobileNUmber)
    TextView mTextViewErrorMobileNUmber;
    @BindView(R.id.editTextPasswordInput)
    EditText editTextPasswordInput;
    @BindView(R.id.mTextViewErrorPassword)
    TextView mTextViewErrorPassword;
    @BindView(R.id.layoutSignIn)
    LinearLayout layoutSignIn;
    @BindView(R.id.layoutSignUp)
    LinearLayout layoutSignUp;
    @BindView(R.id.layoutForgotPassword)
    LinearLayout layoutForgotPassword;
    @BindView(R.id.progressBar)
    FrameLayout progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try{
            editTextMobileNumberInput.addTextChangedListener(new LoginActivity.TextChange(editTextMobileNumberInput));
            editTextPasswordInput.addTextChangedListener(new LoginActivity.TextChange(editTextPasswordInput));
            editTextMobileNumberInput.setOnEditorActionListener(editorListener);
            editTextPasswordInput.setOnEditorActionListener(editorListener);
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
                    validate(editTextMobileNumberInput.getText().toString().trim(), editTextPasswordInput.getText().toString());
                    break;
            }
            return false;
        }
    };

    @OnClick(R.id.layoutSignIn)
    void onSignInClick(){
        try {
            if (validate(editTextMobileNumberInput.getText().toString().trim(), editTextPasswordInput.getText().toString())) {
                Utils.hideSoftKeyboard(LoginActivity.this);
                if(Utils.isNetworkConnectionAvailable(this)){

                } else {
                    showCustomDialog("", getResources().getString(R.string.error_network), getResources().getString(R.string.ok), getResources().getString(R.string.confirm), null);
                }
            }
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutSignUp)
    void onSignUpClick(){
        try {
            Utils.hideSoftKeyboard(LoginActivity.this);
            invalidateErrorMessages();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutForgotPassword)
    void onForgotPasswordClick(){
        try {
            Utils.hideSoftKeyboard(LoginActivity.this);
            invalidateErrorMessages();
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public boolean validate(String strEmail, String password) {
        boolean valid = true;
        try {
            if (strEmail.isEmpty()) {
                editTextMobileNumberInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorMobileNUmber.setVisibility(View.VISIBLE);
                mTextViewErrorMobileNUmber.setText(getResources().getString(R.string.error_mobile_number));
                valid = false;
            }
            if (password.isEmpty()) {
                editTextPasswordInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorPassword.setVisibility(View.VISIBLE);
                mTextViewErrorPassword.setText(getResources().getString(R.string.error_password));
                valid = false;
            }
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    private void invalidateErrorMessages() {
        try {
            mTextViewErrorMobileNUmber.setText("");
            mTextViewErrorPassword.setText("");
            mTextViewErrorMobileNUmber.setVisibility(View.GONE);
            mTextViewErrorPassword.setVisibility(View.GONE);
            editTextMobileNumberInput.setText("");
            editTextPasswordInput.setText("");
            editTextMobileNumberInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextPasswordInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
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
                if(view.getId() == R.id.editTextMobileNumberInput){
                    if(s.length() > 0) {
                        editTextMobileNumberInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        mTextViewErrorMobileNUmber.setVisibility(View.GONE);
                    }
                } else if (view.getId() == R.id.editTextPasswordInput){
                    if(s.length() > 0) {
                        editTextPasswordInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        mTextViewErrorPassword.setVisibility(View.GONE);
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

