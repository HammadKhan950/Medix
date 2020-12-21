package com.hammadkhan950.newotp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hammadkhan950.newotp.R;
import com.hammadkhan950.newotp.activity.OtpActivity;
import com.hammadkhan950.newotp.model.UserHelperClass;

import java.util.Calendar;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {


    TextInputLayout regName, regMobile;
    RadioGroup radioGroup;
    DatePicker datePicker;
    Button regBtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String name, number, gender, age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regName = findViewById(R.id.fieldSignUpName);
        regMobile = findViewById(R.id.fieldSignUpMobileNumberRegister);
        datePicker = findViewById(R.id.datePicker);
        radioGroup = findViewById(R.id.radioGroup);
        regBtn = findViewById(R.id.btnGetRegister);
        radioGroup = findViewById(R.id.radioGroup);
        rootNode = FirebaseDatabase.getInstance();
        setTitle(" Register");
        reference = rootNode.getReference("Users");

    }

    private boolean validateName() {
        String val = Objects.requireNonNull(regName.getEditText()).getText().toString().trim();
        if (val.isEmpty()) {
            regName.setError("Field can not be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateMobile() {
        String val = regMobile.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            regMobile.setError("Field can not be empty");
            return false;
        } else {
            regMobile.setError(null);
            regMobile.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid > 120) {
            Toast.makeText(this, "Enter Valid Age", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    //This function will execute when user click on Register Button
    public void registerUser(View view) {
        if (!validateName() | !validateName() | !validateMobile() | !validateGender() | !validateAge()) {
            return;
        } else {
            //save data on firebase on button click
            regBtn.setOnClickListener(v -> {

                name = regName.getEditText().getText().toString();

                number = regMobile.getEditText().getText().toString();

                int checkedId = radioGroup.getCheckedRadioButtonId();

                if (checkedId == R.id.radioBtnMale) {
                    gender = "Male";
                } else if (checkedId == R.id.radioBtnFemale) {
                    gender = "Female";
                } else if (checkedId == R.id.radioBtnOther) {
                    gender = "Other";
                } else {
                    Toast.makeText(getBaseContext(), "Select Gender", Toast.LENGTH_LONG).show();
                }

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                age = day + "/" + (month + 1) + "/" + year;
                UserHelperClass addNewUser = new UserHelperClass(name, number, gender, age);
                reference.child(number).setValue(addNewUser);
                Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                intent.putExtra("number", number);
                startActivity(intent);
            });
        }
    }


}

