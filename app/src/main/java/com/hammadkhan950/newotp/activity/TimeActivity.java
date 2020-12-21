package com.hammadkhan950.newotp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hammadkhan950.newotp.R;
import com.hammadkhan950.newotp.activity.ConfirmActivity;

public class TimeActivity extends AppCompatActivity implements View.OnClickListener {

    CardView at61, at62, at63, at64, at71, at72, at73, at74, at81, at82, at83, at84, at91, at92, at93, at94;
    CardView[] myCards = new CardView[16];
    Button nextButton;
    String time, date, category, details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        category = intent.getStringExtra("category");
        setTitle("Select your time");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        nextButton = findViewById(R.id.nextButton);
        at61 = findViewById(R.id.at61);
        at62 = findViewById(R.id.at62);
        at63 = findViewById(R.id.at63);
        at64 = findViewById(R.id.at64);
        at71 = findViewById(R.id.at71);
        at72 = findViewById(R.id.at72);
        at73 = findViewById(R.id.at73);
        at74 = findViewById(R.id.at74);
        at81 = findViewById(R.id.at81);
        at82 = findViewById(R.id.at82);
        at83 = findViewById(R.id.at83);
        at84 = findViewById(R.id.at84);
        at91 = findViewById(R.id.at91);
        at92 = findViewById(R.id.at92);
        at93 = findViewById(R.id.at93);
        at94 = findViewById(R.id.at94);
        at61.setOnClickListener(this);
        at62.setOnClickListener(this);
        at63.setOnClickListener(this);
        at64.setOnClickListener(this);
        at71.setOnClickListener(this);
        at72.setOnClickListener(this);
        at63.setOnClickListener(this);
        at64.setOnClickListener(this);
        at71.setOnClickListener(this);
        at72.setOnClickListener(this);
        at73.setOnClickListener(this);
        at74.setOnClickListener(this);
        at81.setOnClickListener(this);
        at82.setOnClickListener(this);
        at83.setOnClickListener(this);
        at84.setOnClickListener(this);
        at91.setOnClickListener(this);
        at92.setOnClickListener(this);
        at93.setOnClickListener(this);
        at94.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        myCards = new CardView[]{at61, at62, at63, at64, at71, at72, at73, at74, at81, at82, at83, at84, at91, at92, at93, at94};
    }


    public void cardSelector(CardView view, String str) {
        for (int j = 0; j < 16; j++) {
            if (myCards[j].toString().equals(view.toString())) {
                myCards[j].setCardBackgroundColor(getResources().getColor(R.color.teal_700));
                time = str;
            } else {
                myCards[j].setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.nextButton) {

            if (time == null) {
                Toast.makeText(this, "Please select any slot", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, ConfirmActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                intent.putExtra("category", category);
                startActivity(intent);
            }

        }
        if (view.getId() == R.id.at61) {
            cardSelector(at61, " 6:00 ");
        }
        if (view.getId() == R.id.at62) {
            cardSelector(at62, " 6:15");
        }
        if (view.getId() == R.id.at63) {
            cardSelector(at63, " 6:30");
        }
        if (view.getId() == R.id.at64) {
            cardSelector(at64, " 6:45");
        }
        if (view.getId() == R.id.at71) {
            cardSelector(at71, " 7:00");
        }
        if (view.getId() == R.id.at72) {
            cardSelector(at72, " 7:15");
        }
        if (view.getId() == R.id.at73) {
            cardSelector(at73, " 7:30");
        }
        if (view.getId() == R.id.at74) {
            cardSelector(at74, " 7:45");
        }
        if (view.getId() == R.id.at81) {
            cardSelector(at81, " 8:00");
        }
        if (view.getId() == R.id.at82) {
            cardSelector(at82, " 8:15");
        }
        if (view.getId() == R.id.at83) {
            cardSelector(at83, " 8:30");
        }
        if (view.getId() == R.id.at84) {
            cardSelector(at84, " 8:45");
        }
        if (view.getId() == R.id.at91) {
            cardSelector(at91, " 9:00");
        }
        if (view.getId() == R.id.at92) {
            cardSelector(at92, " 9:15");
        }
        if (view.getId() == R.id.at93) {
            cardSelector(at93, " 9:30");
        }
        if (view.getId() == R.id.at94) {
            cardSelector(at94, " 9:45");
        }
    }

}