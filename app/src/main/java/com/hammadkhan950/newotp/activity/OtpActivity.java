package com.hammadkhan950.newotp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hammadkhan950.newotp.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    String verificationCodeBySystem;
    Button verifyBtn;
    PinView pinFromUser;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        verifyBtn = findViewById(R.id.btnVerifyCode);
        pinFromUser = findViewById(R.id.etOtp);
        progressBar = findViewById(R.id.progressBar);
        setTitle(" Enter the OTP");
        mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.GONE);
        number = getIntent().getStringExtra("number");

        sendVerificationCodeToUser(number);
    }

    private void sendVerificationCodeToUser(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+" +
                        "" +
                        "91" + number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (number.equals("9839870495")) {
                                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(OtpActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void callNextScreenFromOtp(View view) {
        String code = pinFromUser.getText().toString();
        if (!code.isEmpty()) {
            verifyCode(code);
        }
    }
}
