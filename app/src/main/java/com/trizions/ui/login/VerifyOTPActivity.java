package com.trizions.ui.login;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trizions.BaseActivity;
import com.trizions.R;
import com.trizions.dialog.CustomDialog;
import com.trizions.ui.dashboard.DashBoardActivity;
import com.trizions.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class VerifyOTPActivity extends BaseActivity {

    @BindView(R.id.editTextOTP)
    EditText editTextOTP;

    @BindView(R.id.layoutVerify)
    LinearLayout layoutVerify;

    @BindView(R.id.mTextViewOTPError)
    TextView mTextViewOTPError;

    @BindView(R.id.progressBar)
    LinearLayout progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String mobileNumber, verificationId, currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otpactivity);
        verificationId = getIntent().getStringExtra("verificationId");
        mobileNumber = getIntent().getStringExtra("mobileNumber");
    }

    @OnClick(R.id.layoutVerify)
    void onLayoutVerify() {
        try {
            if (validate(editTextOTP.getText().toString())) {
                showProgressBar();
                Utils.hideSoftKeyboard(VerifyOTPActivity.this);
                try {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, editTextOTP.getText().toString());
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                hideProgressBar();
                                showCustomDialog("", "Verified Successfully", getResources().getString(R.string.ok), getResources().getString(R.string.success), onDismissListener);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgressBar();
                            showCustomDialog("", e.getMessage(), getResources().getString(R.string.ok), getResources().getString(R.string.warning), null);
                        }
                    });
                } catch (Exception exception) {
                    Log.e("Error ==> ", "" + exception);
                }
            }

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    CustomDialog.OnDismissListener onDismissListener = () -> {
        try {
            Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    };

    private boolean validate(String stringOTP) {
        boolean valid = true;
        try {
            if (stringOTP.isEmpty()) {
                editTextOTP.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewOTPError.setVisibility(View.VISIBLE);
                mTextViewOTPError.setText(getResources().getString(R.string.error_otp));
                valid = false;
            } else if (stringOTP.length() != 6) {
                editTextOTP.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewOTPError.setVisibility(View.VISIBLE);
                mTextViewOTPError.setText(getResources().getString(R.string.invalid_otp));
                valid = false;
            } else {
                editTextOTP.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewOTPError.setVisibility(GONE);
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