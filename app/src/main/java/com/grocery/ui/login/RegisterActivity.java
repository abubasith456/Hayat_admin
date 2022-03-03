package com.grocery.ui.login;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.grocery.BaseActivity;
import com.grocery.R;
import com.grocery.dialog.CustomDialog;
import com.grocery.model.login.register.RegisterRequest;
import com.grocery.model.login.register.RegisterResponse;
import com.grocery.rest_client.BCRequests;
import com.grocery.utils.EmailValidator;
import com.grocery.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends BaseActivity {
    @BindView(R.id.linearLayoutBack)
    LinearLayout linearLayoutBack;
    @BindView(R.id.editTextSignUpUserName)
    EditText editTextSignUpUserName;
    @BindView(R.id.editTextSignUpMobileNumber)
    EditText editTextSignUpMobileNumber;
    @BindView(R.id.editTextSignUpEmail)
    EditText editTextSignUpEmail;
    @BindView(R.id.editTextSignUpPassword)
    EditText editTextSignUpPassword;
    @BindView(R.id.mTextViewUserNameError)
    TextView mTextViewUserNameError;
    @BindView(R.id.mTextViewMobileNumberError)
    TextView mTextViewMobileNumberError;
    @BindView(R.id.mTextViewEmailError)
    TextView mTextViewEmailError;
    @BindView(R.id.mTextViewPasswordError)
    TextView mTextViewPasswordError;
    @BindView(R.id.layoutSignUp)
    LinearLayout layoutSignUp;
    @BindView(R.id.progressBar)
    FrameLayout progressBar;

    private EmailValidator emailValidator;
    RegisterResponse registerResponse;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    long timeStamp;

    boolean isUserNameAvail, isMobileNumberAvail, isEmailAvail, isPasswordAvail = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = getApplicationContext().getSharedPreferences("LoginStatus", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        try {
            Utils.hideSoftKeyboard(RegisterActivity.this);
            editTextSignUpUserName.addTextChangedListener(new TextChange(editTextSignUpUserName));
            editTextSignUpMobileNumber.addTextChangedListener(new TextChange(editTextSignUpMobileNumber));
            editTextSignUpEmail.addTextChangedListener(new TextChange(editTextSignUpEmail));
            editTextSignUpPassword.addTextChangedListener(new TextChange(editTextSignUpPassword));

            editTextSignUpUserName.setOnEditorActionListener(editorListener);
            editTextSignUpMobileNumber.setOnEditorActionListener(editorListener);
            editTextSignUpEmail.setOnEditorActionListener(editorListener);
            editTextSignUpPassword.setOnEditorActionListener(editorListener);
            emailValidator = new EmailValidator();
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
                    validate(editTextSignUpUserName.getText().toString(), editTextSignUpMobileNumber.getText().toString(), editTextSignUpEmail.getText().toString().trim(), editTextSignUpPassword.getText().toString());
                    break;
            }
            return false;
        }
    };

    @OnClick(R.id.linearLayoutBack)
    void onBackClick() {
        try {
            Utils.hideSoftKeyboard(RegisterActivity.this);
            invalidateErrorMessages();
            finish();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutSignUp)
    void onSignUpClick() {
        try {
            if (validate(editTextSignUpUserName.getText().toString(), editTextSignUpMobileNumber.getText().toString(), editTextSignUpEmail.getText().toString().trim(), editTextSignUpPassword.getText().toString())) {
                Utils.hideSoftKeyboard(RegisterActivity.this);
                if (Utils.isNetworkConnectionAvailable(this)) {
                    showProgress();
//                    registration();
                    firebaseRegister();
                } else {
                    showCustomDialog("", getResources().getString(R.string.error_network), getResources().getString(R.string.ok), getResources().getString(R.string.confirm), null);
                }
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void firebaseRegister() {

        firebaseAuth.createUserWithEmailAndPassword(editTextSignUpEmail.getText().toString(), editTextSignUpPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideProgress();
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            storeUserInfo(userId, editTextSignUpUserName.getText().toString(), editTextSignUpEmail.getText().toString(), editTextSignUpMobileNumber.getText().toString());
                            showCustomDialog("", "Register successfully", getResources().getString(R.string.ok), getResources().getString(R.string.success), onDismissListener);
                            editor.putString("userLogged?","LoggedIn");
                            editor.apply();
                        } else {

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgress();
                showCustomDialog("", e.getMessage(), getResources().getString(R.string.ok), getResources().getString(R.string.warning), null);
            }
        });

    }

    private void storeUserInfo(String userId, String userName, String email, String mobileNumber) {
        try {
            HashMap<String, Object> addFieldInfo = new HashMap<>();
            addFieldInfo.put("userId", "" + userId);
            addFieldInfo.put("userName", "" + userName);
            addFieldInfo.put("userEmailAddress", "" + email);
            addFieldInfo.put("userMobileNumber", "" + mobileNumber);
            DocumentReference databaseReference = firebaseFirestore.collection("Users").document(userId);
            databaseReference.set(addFieldInfo);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void registration() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName(editTextSignUpUserName.getText().toString());
        registerRequest.setMobileNumber(editTextSignUpMobileNumber.getText().toString());
        registerRequest.setEmail(editTextSignUpEmail.getText().toString());
        registerRequest.setPassword(editTextSignUpPassword.getText().toString());

        Call<RegisterResponse> registerResponseCall = BCRequests.getInstance().getBCRestService().register(registerRequest);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    registerResponse = response.body();
                    if (registerResponse != null) {
                        String status = registerResponse.getStatus();
                        String message = registerResponse.getMessage();
                        if (status.equals("success")) {
                            showCustomDialog("", message, getResources().getString(R.string.ok), getResources().getString(R.string.success), onDismissListener);
                        } else {
                            showCustomDialog("", message, getResources().getString(R.string.ok), getResources().getString(R.string.warning), null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable throwable) {
                showCustomDialog("", throwable.getMessage(), getResources().getString(R.string.ok), getResources().getString(R.string.success), null);
            }
        });
    }

    CustomDialog.OnDismissListener onDismissListener = () -> finish();

    private void invalidateErrorMessages() {
        try {
            mTextViewUserNameError.setText("");
            mTextViewMobileNumberError.setText("");
            mTextViewEmailError.setText("");
            mTextViewPasswordError.setText("");
            mTextViewUserNameError.setVisibility(View.GONE);
            mTextViewMobileNumberError.setVisibility(View.GONE);
            mTextViewEmailError.setVisibility(View.GONE);
            mTextViewPasswordError.setVisibility(View.GONE);
            editTextSignUpUserName.setText("");
            editTextSignUpMobileNumber.setText("");
            editTextSignUpEmail.setText("");
            editTextSignUpPassword.setText("");
            editTextSignUpUserName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextSignUpMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextSignUpPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public boolean validate(String strFirstName, String strLastName, String strEmail, String strPassword) {
        boolean valid = true;
        try {
            if (strFirstName.isEmpty()) {
                mTextViewUserNameError.setVisibility(View.VISIBLE);
                editTextSignUpUserName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewUserNameError.setText(getResources().getString(R.string.error_user_name));
                valid = false;
            }
            if (strLastName.isEmpty()) {
                mTextViewMobileNumberError.setVisibility(View.VISIBLE);
                editTextSignUpMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewMobileNumberError.setText(getResources().getString(R.string.error_mobile_number));
                valid = false;
            }
            if (strEmail.isEmpty()) {
                mTextViewEmailError.setVisibility(View.VISIBLE);
                editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewEmailError.setText(getResources().getString(R.string.error_email));
                valid = false;
            }
            if (!emailValidator.validate(strEmail)) {
                mTextViewEmailError.setVisibility(View.VISIBLE);
                editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewEmailError.setText(getResources().getString(R.string.invalid_email));
                valid = false;
            }
            if (strPassword.isEmpty()) {
                mTextViewPasswordError.setVisibility(View.VISIBLE);
                editTextSignUpPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewPasswordError.setText(getResources().getString(R.string.error_password));
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
                switch (view.getId()) {
                    case R.id.editTextSignUpEmail:
                        isEmailAvail = s.length() > 0;
                        if (isEmailAvail) {
                            mTextViewEmailError.setVisibility(View.GONE);
                            editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        }
                        break;
                    case R.id.editTextSignUpMobileNumber:
                        isMobileNumberAvail = s.length() > 0;
                        if (isMobileNumberAvail) {
                            mTextViewMobileNumberError.setVisibility(View.GONE);
                            editTextSignUpMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        }
                        break;
                    case R.id.editTextSignUpUserName:
                        isUserNameAvail = s.length() > 0;
                        if (isUserNameAvail) {
                            mTextViewUserNameError.setVisibility(View.GONE);
                            editTextSignUpUserName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        }
                        break;
                    case R.id.editTextSignUpPassword:
                        isPasswordAvail = s.length() > 0;
                        if (isPasswordAvail) {
                            mTextViewPasswordError.setVisibility(View.GONE);
                            editTextSignUpPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        }
                        break;
                }
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            try {
                String s = editable.toString();
                if (view.getId() == R.id.editTextSignUpEmail) {
                    if (!s.equals(s.toLowerCase())) {
                        s = s.toLowerCase();
                        editTextSignUpEmail.setText(s);
                        editTextSignUpEmail.setSelection(editTextSignUpEmail.getText().toString().length());
                    }
                }
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
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
