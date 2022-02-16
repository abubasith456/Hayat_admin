package com.grocery.ui.dashboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grocery.BaseActivity;
import com.grocery.R;
import com.grocery.dialog.CustomDialog;
import com.grocery.model.login.change_password.ChangePasswordRequest;
import com.grocery.model.login.change_password.ChangePasswordResponse;
import com.grocery.rest_client.BCRequests;
import com.grocery.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.linearLayoutBack)
    LinearLayout linearLayoutBack;
    @BindView(R.id.editTextMobileNumber)
    EditText editTextMobileNumber;
    @BindView(R.id.textViewMobileNumberError)
    TextView textViewMobileNumberError;
    @BindView(R.id.editTextOldPassword)
    EditText editTextOldPassword;
    @BindView(R.id.textViewOldPasswordError)
    TextView textViewOldPasswordError;
    @BindView(R.id.editTextNewPassword)
    EditText editTextNewPassword;
    @BindView(R.id.textViewNewPasswordError)
    TextView textViewNewPasswordError;
    @BindView(R.id.layoutChangePassword)
    LinearLayout layoutChangePassword;
    @BindView(R.id.progressBar)
    FrameLayout progressBar;

    ChangePasswordResponse changePasswordResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        try {
            editTextMobileNumber.addTextChangedListener(new ChangePasswordActivity.TextChange(editTextMobileNumber));
            editTextOldPassword.addTextChangedListener(new ChangePasswordActivity.TextChange(editTextOldPassword));
            editTextNewPassword.addTextChangedListener(new ChangePasswordActivity.TextChange(editTextNewPassword));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.linearLayoutBack)
    void onBackClick() {
        try {
            Utils.hideSoftKeyboard(ChangePasswordActivity.this);
            invalidateErrorMessages();
            finish();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutChangePassword)
    void onChangePasswordClick() {
        try {
            if (validate(editTextMobileNumber.getText().toString().trim(), editTextOldPassword.getText().toString(), editTextNewPassword.getText().toString())) {
                Utils.hideSoftKeyboard(ChangePasswordActivity.this);
                if (isNetworkConnectionAvailable()) {
                    showProgress();
                    changePassword();
                } else {
                    showCustomDialog("", getResources().getString(R.string.error_network), getResources().getString(R.string.ok), getResources().getString(R.string.confirm), null);
                }
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void changePassword() {
        try {
            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
            changePasswordRequest.setMobileNumber(editTextMobileNumber.getText().toString());
            changePasswordRequest.setOldPassword(editTextOldPassword.getText().toString());
            changePasswordRequest.setNewPassword(editTextNewPassword.getText().toString());
            Call<ChangePasswordResponse> changePasswordResponseCall = BCRequests.getInstance().getBCRestService().changePassword(changePasswordRequest);
            changePasswordResponseCall.enqueue(new Callback<ChangePasswordResponse>() {
                @Override
                public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            hideProgress();
                            changePasswordResponse = response.body();
                            if (response != null && changePasswordResponse != null) {
                                String status = changePasswordResponse.getStatus();
                                String message = changePasswordResponse.getMessage();
                                if (status.equals("success")) {
                                    showCustomDialog("", message, getResources().getString(R.string.ok), getResources().getString(R.string.success), onDismissListener);
                                } else {
                                    showCustomDialog("", message, getResources().getString(R.string.ok), getResources().getString(R.string.warning), null);
                                }
                            }
                        }
                    } catch (Exception exception) {
                        Log.e("Error ==> ", "" + exception);
                    }
                }

                @Override
                public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                    hideProgress();
                    showCustomDialog("", t.getMessage(), getResources().getString(R.string.ok), getResources().getString(R.string.warning), null);
                }
            });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    CustomDialog.OnDismissListener onDismissListener = () -> {
        invalidateErrorMessages();
        finish();
    };

    public boolean validate(String stringMobileNumber, String stringOldPassword, String stringNewPassword) {
        boolean valid = true;
        try {
            if (stringMobileNumber.isEmpty()) {
                editTextMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                textViewMobileNumberError.setVisibility(View.VISIBLE);
                textViewMobileNumberError.setText(getResources().getString(R.string.error_mobile_number));
                valid = false;
            } else if (stringMobileNumber.length() < 10) {
                editTextMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                textViewMobileNumberError.setVisibility(View.VISIBLE);
                textViewMobileNumberError.setText(getResources().getString(R.string.invalid_number));
                valid = false;
            }
            if (stringOldPassword.isEmpty()) {
                editTextOldPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                textViewOldPasswordError.setVisibility(View.VISIBLE);
                textViewOldPasswordError.setText(getResources().getString(R.string.error_old_password));
                valid = false;
            }
            if (stringOldPassword.isEmpty()) {
                editTextNewPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                textViewNewPasswordError.setVisibility(View.VISIBLE);
                textViewNewPasswordError.setText(getResources().getString(R.string.error_new_password));
                valid = false;
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
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
                if (view.getId() == R.id.editTextMobileNumber) {
                    if (s.length() > 0) {
                        editTextMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        textViewMobileNumberError.setVisibility(View.GONE);
                    }
                } else if (view.getId() == R.id.editTextOldPassword) {
                    if (s.length() > 0) {
                        editTextOldPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        textViewOldPasswordError.setVisibility(View.GONE);
                    }
                } else if (view.getId() == R.id.editTextNewPassword) {
                    if (s.length() > 0) {
                        editTextNewPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        textViewNewPasswordError.setVisibility(View.GONE);
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

    private void invalidateErrorMessages() {
        try {
            textViewMobileNumberError.setText("");
            textViewMobileNumberError.setVisibility(View.GONE);
            textViewOldPasswordError.setText("");
            textViewOldPasswordError.setVisibility(View.GONE);
            textViewNewPasswordError.setText("");
            textViewNewPasswordError.setVisibility(View.GONE);
            editTextMobileNumber.setText("");
            editTextOldPassword.setText("");
            editTextNewPassword.setText("");
            editTextMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextOldPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextNewPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
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