package com.grocery.ui.login;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.grocery.BaseActivity;
import com.grocery.R;
import com.grocery.dialog.CustomDialog;
import com.grocery.model.login.forgot_password.ForgotPasswordRequest;
import com.grocery.model.login.forgot_password.ForgotPasswordResponse;
import com.grocery.rest_client.BCRequests;
import com.grocery.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.linearLayoutBack)
    LinearLayout linearLayoutBack;
    @BindView(R.id.editTextEmailAddress)
    EditText editTextEmailAddress;
    @BindView(R.id.mTextViewEmailAddressError)
    TextView mTextViewEmailAddressError;
    @BindView(R.id.layoutForgotPassword)
    LinearLayout layoutForgotPassword;
    @BindView(R.id.progressBar)
    FrameLayout progressBar;

    ForgotPasswordResponse forgotPasswordResponse;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        firebaseAuth = FirebaseAuth.getInstance();

        try {
            editTextEmailAddress.addTextChangedListener(new TextChange(editTextEmailAddress));
            editTextEmailAddress.setOnEditorActionListener(editorListener);
        } catch (Exception exception) {
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
                    validate(editTextEmailAddress.getText().toString().trim());
                    break;
            }
            return false;
        }
    };

    @OnClick(R.id.linearLayoutBack)
    void onBackClick() {
        try {
            Utils.hideSoftKeyboard(ForgotPasswordActivity.this);
            invalidateErrorMessages();
            finish();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutForgotPassword)
    void onForgotPasswordClick() {
        try {
            if (validate(editTextEmailAddress.getText().toString().trim())) {
                if (isNetworkConnectionAvailable()) {
                    firebaseForgotPassword();
                } else {
                    showCustomDialog("", getResources().getString(R.string.error_network), getResources().getString(R.string.ok), getResources().getString(R.string.confirm), null);
                }
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void firebaseForgotPassword() {
        try {
            showProgress();
            firebaseAuth.sendPasswordResetEmail(editTextEmailAddress.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                hideProgress();
                                showCustomDialog("", "Mail sent to your Email", getResources().getString(R.string.ok), getResources().getString(R.string.success), onDismissListener);
                            } else {
                                hideProgress();
                                showCustomDialog("", task.getException().getMessage(), getResources().getString(R.string.ok), getResources().getString(R.string.warning), null);
                            }
                        }
                    });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void forgotPassword() {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail(editTextEmailAddress.getText().toString());

        Call<ForgotPasswordResponse> forgotPasswordResponseCall = BCRequests.getInstance().getBCRestService().retrieve(forgotPasswordRequest);
        forgotPasswordResponseCall.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                forgotPasswordResponse = response.body();
                if (forgotPasswordResponse != null) {
                    String status = forgotPasswordResponse.getStatus();
                    String message = forgotPasswordResponse.getMessage();
                    if (status.equals("success")) {
                        showCustomDialog("", message, getResources().getString(R.string.ok), getResources().getString(R.string.success), onDismissListener);
                    } else {
                        showCustomDialog("", message, getResources().getString(R.string.ok), getResources().getString(R.string.warning), null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable throwable) {
                showCustomDialog("", throwable.getMessage(), getResources().getString(R.string.ok), getResources().getString(R.string.warning), null);
            }
        });
    }

    CustomDialog.OnDismissListener onDismissListener = () -> {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    };

    public boolean validate(String strEmail) {
        boolean valid = true;
        try {
            if (strEmail.isEmpty()) {
                editTextEmailAddress.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewEmailAddressError.setVisibility(View.VISIBLE);
                mTextViewEmailAddressError.setText(getResources().getString(R.string.error_mobile_number));
                valid = false;
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    private void invalidateErrorMessages() {
        try {
            mTextViewEmailAddressError.setText("");
            mTextViewEmailAddressError.setVisibility(View.GONE);
            editTextEmailAddress.setText("");
            editTextEmailAddress.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
        } catch (Exception exception) {
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
                if (view.getId() == R.id.editTextEmailAddress) {
                    if (s.length() > 0) {
                        editTextEmailAddress.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        mTextViewEmailAddressError.setVisibility(View.GONE);
                    }
                }
            } catch (Exception exception) {
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
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgress() {
        try {
            progressBar.setVisibility(View.GONE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }
}
