package com.hammadkhan950.newotp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hammadkhan950.newotp.R;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    CountryCodePicker countryCodePicker;
    TextInputLayout number;
    Button registerButton, continueButton;
    FirebaseUser mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        countryCodePicker = findViewById(R.id.countryPicker);
        number = findViewById(R.id.mobNumberField);
        registerButton = findViewById(R.id.btnRegister);
        continueButton = findViewById(R.id.btnlogincontinue);
        mAuth=FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Admin");

        setTitle(" Login");
    }

    public void btnRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean validateFields() {
        String phone = Objects.requireNonNull(number.getEditText()).getText().toString().trim();
        if (phone.isEmpty()) {
            number.setError("Field can not be empty");
            return false;
        } else {
            number.setError(null);
            number.setErrorEnabled(false);
            return true;
        }
    }

    public void letTheUserLoggedIn(View view) {

        if (!isConnected(this)) {
            showCustomDialog();
        }

        //Validate phone number
        if (!validateFields()) {
            return;
        } else {
            continueButton.setOnClickListener(v -> {

                String userEnteredNumber = Objects.requireNonNull(number.getEditText()).getText().toString().trim();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                Query checkUser = reference.orderByChild("number").equalTo(userEnteredNumber);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            number.setError(null);
                            number.setErrorEnabled(false);
                            String numberFromDB = snapshot.child(userEnteredNumber).child("number").getValue(String.class);
                            if (Objects.requireNonNull(numberFromDB).equals(userEnteredNumber)) {


                                Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                                intent.putExtra("number", number.getEditText().getText().toString());
                                startActivity(intent);
                            } else {
                                number.setError("No such user exist!");
                                number.requestFocus();
                            }
                        } else {

                            number.setError("No such user exist!");
                            number.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }


    //Check Internet Connection
    private boolean isConnected(LoginActivity loginActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Please connect to the Internet")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}