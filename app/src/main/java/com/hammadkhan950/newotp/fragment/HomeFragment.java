package com.hammadkhan950.newotp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hammadkhan950.newotp.R;
import com.hammadkhan950.newotp.activity.ConfirmActivity;
import com.hammadkhan950.newotp.activity.TimeActivity;

public class HomeFragment extends Fragment implements View.OnClickListener {

    TextView tvDate;
    Button btnRegister;
    DatePicker datePicker;
    String category, date;
    NumberPicker spinnerCategory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tvDate = view.findViewById(R.id.tv_date);
        datePicker = view.findViewById(R.id.datePicker);
        btnRegister = view.findViewById(R.id.btnGetRegister);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        final String genders[] = {"Nerves", "Bone", "Heart", "Teeth", "Kidney","Eyes","Liver", "Others"};
        spinnerCategory.setMinValue(0);
        spinnerCategory.setMaxValue(genders.length - 1);
        spinnerCategory.setDisplayedValues(genders);
        spinnerCategory.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        NumberPicker.OnValueChangeListener myValChangedListener = (picker, oldVal, newVal) -> {
            category = genders[newVal];
        };
        spinnerCategory.setOnValueChangedListener(myValChangedListener);
        btnRegister.setOnClickListener(this);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        date = day + "/" + (month + 1) + "/" + year;
        return view;
    }


    @Override
    public void onClick(View view) {

        if (date == null || category == null) {
            Toast.makeText(getContext(), "Please select any option", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), TimeActivity.class);
            intent.putExtra("category", category);
            intent.putExtra("date", date);
            startActivity(intent);
        }
    }
}