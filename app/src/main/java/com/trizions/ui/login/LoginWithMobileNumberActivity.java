package com.trizions.ui.login;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.trizions.BaseActivity;
import com.trizions.R;
import com.trizions.utils.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginWithMobileNumberActivity extends BaseActivity {

    @BindView(R.id.editTextMobileNumber)
    EditText editTextMobileNumber;

    @BindView(R.id.mTextViewMobileNumberError)
    TextView mTextViewMobileNumberError;

    @BindView(R.id.layoutSend)
    LinearLayout layoutSend;

    @BindView(R.id.progressBar)
    FrameLayout progressBar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_mobile_number);
        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
    }

    @OnClick(R.id.layoutSend)
    void onLayoutVerifyClick() {
        try {
            if (validate(editTextMobileNumber.getText().toString().trim())) {
                Utils.hideSoftKeyboard(this);
                if (Utils.isNetworkConnectionAvailable(this)) {
                    showProgressBar();
                    String mobileNumber = "+" + "91" + " " + editTextMobileNumber.getText().toString();
                    mobileNumberAuth(mobileNumber);
                } else {
                    showCustomDialog("", getResources().getString(R.string.error_network), getResources().getString(R.string.ok), getResources().getString(R.string.confirm), null);
                }
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void mobileNumberAuth(String mobileNumber) {
        try {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(mobileNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(this)
                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(verificationId, forceResendingToken);
                                    hideProgressBar();
                                    Toast.makeText(getApplicationContext(), "OTP sent", Toast.LENGTH_SHORT).show();
                                    Intent verifyMobileNumber = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                                    verifyMobileNumber.putExtra("mobileNumber", mobileNumber);
                                    verifyMobileNumber.putExtra("verificationId", verificationId);
                                    startActivity(verifyMobileNumber);
                                }

                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    hideProgressBar();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    hideProgressBar();
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private boolean validate(String stringMobileNumber) {
        boolean valid = true;
        try {
            if (stringMobileNumber.isEmpty()) {
                editTextMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewMobileNumberError.setVisibility(View.VISIBLE);
                mTextViewMobileNumberError.setText(getResources().getString(R.string.error_mobile_number));
                valid = false;
            } else if (stringMobileNumber.length() < 10) {
                editTextMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewMobileNumberError.setVisibility(View.VISIBLE);
                mTextViewMobileNumberError.setText(getResources().getString(R.string.invalid_number));
                valid = false;
            } else {
                editTextMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewMobileNumberError.setVisibility(GONE);
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    public void showProgressBar() {
        try {
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgressBar() {
        try {
            progressBar.setVisibility(View.GONE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

}