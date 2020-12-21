package com.hammadkhan950.newotp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hammadkhan950.newotp.R;
import com.hammadkhan950.newotp.fragment.HomeFragment;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    Timer timer;
    FirebaseUser mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (mAuth != null) {
            onStart();
        } else {

            setContentView(R.layout.activity_splash);
            timer();
        }


    }

    public void timer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }, 1500);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth != null) {
            String newNumber = String.valueOf(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber());
            if (newNumber.equals("+919839870495")) {
                Intent intent = new Intent(this, AdminActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        }
    }


}