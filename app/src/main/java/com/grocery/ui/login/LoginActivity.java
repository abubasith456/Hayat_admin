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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.grocery.BaseActivity;
import com.grocery.R;
import com.grocery.dialog.CustomDialog;
import com.grocery.model.login.LoginResponse;
import com.grocery.ui.dashboard.DashBoardActivity;
import com.grocery.utils.Utils;

import java.util.HashMap;

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
    @BindView(R.id.imageViewSignupGoogle)
    ImageView imageViewSignupGoogle;
    @BindView(R.id.imageViewSignupWithMobileNUmber)
    ImageView imageViewSignupWithMobileNUmber;
    //    @BindView(R.id.layoutScanCode)
//    LinearLayout layoutScanCode;
    @BindView(R.id.progressBar)
    FrameLayout progressBar;

    LoginResponse loginResponse;
    Integer userId;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private GoogleSignInClient googleSignInClient;
    static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        try {
            editTextMobileNumberInput.addTextChangedListener(new LoginActivity.TextChange(editTextMobileNumberInput));
            editTextPasswordInput.addTextChangedListener(new LoginActivity.TextChange(editTextPasswordInput));
            editTextMobileNumberInput.setOnEditorActionListener(editorListener);
            editTextPasswordInput.setOnEditorActionListener(editorListener);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            invalidateErrorMessages();
            Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("userId", String.valueOf(userId));
            startActivity(intent);
            finish();
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
    void onSignInClick() {
        try {
            if (validate(editTextMobileNumberInput.getText().toString().trim(), editTextPasswordInput.getText().toString())) {
                Utils.hideSoftKeyboard(LoginActivity.this);
                if (Utils.isNetworkConnectionAvailable(this)) {
                    showProgress();
                    firebaseLogin();

                } else {
                    showCustomDialog("", getResources().getString(R.string.error_network), getResources().getString(R.string.ok), getResources().getString(R.string.confirm), null);
                }
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void firebaseLogin() {
        try {
            firebaseAuth.signInWithEmailAndPassword(editTextMobileNumberInput.getText().toString(), editTextPasswordInput.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                hideProgress();
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                showCustomDialog("", "Login successfully", getResources().getString(R.string.ok), getResources().getString(R.string.success), onDismissListener);
                            } else {
                                hideProgress();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgress();
                    showCustomDialog("", e.getMessage(), getResources().getString(R.string.ok), getResources().getString(R.string.warning), null);

                }
            });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    CustomDialog.OnDismissListener onDismissListener = () -> {
        invalidateErrorMessages();
        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId", String.valueOf(userId));
        startActivity(intent);
        finish();
    };

    @OnClick(R.id.layoutSignUp)
    void onSignUpClick() {
        try {
            Utils.hideSoftKeyboard(LoginActivity.this);
            invalidateErrorMessages();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutForgotPassword)
    void onForgotPasswordClick() {
        try {
            Utils.hideSoftKeyboard(LoginActivity.this);
            invalidateErrorMessages();
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.imageViewSignupGoogle)
    void onSignupGoogleButtonClick() {
        try {
            Utils.hideSoftKeyboard(LoginActivity.this);
            invalidateErrorMessages();
            signupWithGoogle();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.imageViewSignupWithMobileNUmber)
    void onSignupWithMobileNumberClick(){
        try {
            Utils.hideSoftKeyboard(LoginActivity.this);
            invalidateErrorMessages();
            startActivity(new Intent(LoginActivity.this, LoginWithMobileNumberActivity.class));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void signupWithGoogle() {
        try {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void signupWithMobileNumber() {
        try {

        }catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                showProgress();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        try {
            showProgress();
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                hideProgress();
                                String userName = task.getResult().getUser().getDisplayName();
                                String userEmail = task.getResult().getUser().getEmail();
                                String userId = task.getResult().getUser().getUid();
                                storeUserInfo(userName, userEmail, userId);
                            } else {
                            }
                        }
                    });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }


    }

    private void storeUserInfo(String userName, String userEmail, String userId) {
        try {
            HashMap<String, Object> addFieldInfo = new HashMap<>();
            addFieldInfo.put("userId", "" + userId);
            addFieldInfo.put("userName", "" + userName);
            addFieldInfo.put("userEmailAddress", "" + userEmail);
            addFieldInfo.put("userMobileNumber", "" + "");
            DocumentReference databaseReference = firebaseFirestore.collection("Users").document(userId);
            databaseReference.set(addFieldInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(@NonNull Void unused) {
                    invalidateErrorMessages();
                    hideProgress();
                    Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userId", String.valueOf(userId));
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

//    @OnClick(R.id.layoutScanCode)
//    void onScanCodeClick() {
//        try {
//            Intent intent = new Intent(LoginActivity.this, ScanBarCodeActivity.class);
//            startActivityForResult(intent, 2);
//        } catch (Exception exception) {
//            Log.e("Error ==> ", "" + exception);
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 2) ;
//        {
//            String message = data.getStringExtra("BarCode Value");
//            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
//        }
//    }

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
        } catch (Exception exception) {
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
                if (view.getId() == R.id.editTextMobileNumberInput) {
                    if (s.length() > 0) {
                        editTextMobileNumberInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        mTextViewErrorMobileNUmber.setVisibility(View.GONE);
                    }
                } else if (view.getId() == R.id.editTextPasswordInput) {
                    if (s.length() > 0) {
                        editTextPasswordInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        mTextViewErrorPassword.setVisibility(View.GONE);
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

