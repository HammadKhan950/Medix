package com.hammadkhan950.newotp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hammadkhan950.newotp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ConfirmActivity extends AppCompatActivity {
    String date, category, number, time, name;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Button btnConfirm;
    String newName;

    TextView tvName, tvDate, tvTime, tvCategories, tvMobile;
    String newNumber = "";
    int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        btnConfirm = findViewById(R.id.btnGetRegister);
        tvName = findViewById(R.id.tv_naam);
        tvDate = findViewById(R.id.tv_dates);
        tvTime = findViewById(R.id.tv_times);
        tvCategories = findViewById(R.id.tv_categories);
        tvMobile = findViewById(R.id.tv_mobiles);
        rootNode = FirebaseDatabase.getInstance();
        setTitle("Confirmation page");
        number = String.valueOf(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber());

        for (int i = 3; i < number.length(); i++) {
            newNumber = newNumber + number.charAt(i);
        }
        reference = FirebaseDatabase.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newName = dataSnapshot.child("Users").child(newNumber).child("name").getValue().toString();
                tvName.setText(newName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ConfirmActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();


            }
        });
        Intent newIntent = getIntent();
        date = newIntent.getStringExtra("date");
        category = newIntent.getStringExtra("category");
        time = newIntent.getStringExtra("time");
        tvName.setText(name);
        tvMobile.setText(newNumber);
        tvDate.setText(date);
        tvCategories.setText(category);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Appointment", "My Appointment", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }


        btnConfirm.setOnClickListener(view -> {
            if (InternetConnection(ConfirmActivity.this)) {
                String status = "";
                String paymentCancel = "";
                String approvalRefNo = "";

                if (status.equals("success")) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ConfirmActivity.this, "Appointment");
                    builder.setContentTitle("Appointment");
                    builder.setContentText("Your appointment is successful");
                    builder.setSmallIcon(R.drawable.ic_baseline_message_24);
                    builder.setAutoCancel(true);

                    Intent intent = new Intent(ConfirmActivity.this,
                            PaymentActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    PendingIntent pendingIntent = PendingIntent.getActivity(ConfirmActivity.this
                            , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentIntent(pendingIntent);
                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(ConfirmActivity.this);
                    managerCompat.notify(1, builder.build());

                }
            }

            Uri uri =
                    new Uri.Builder()
                            .scheme("upi")
                            .authority("pay")
                            .appendQueryParameter("pa", "9839870495@okbizaxis")
                            .appendQueryParameter("pn", "your-merchant-name")
                            .appendQueryParameter("mc", "")
                            .appendQueryParameter("tr", "25584545")
                            .appendQueryParameter("tn", "Pay to Medix")
                            .appendQueryParameter("am", "1")
                            .appendQueryParameter("cu", "INR")
                            .appendQueryParameter("url", "your-transaction-url")
                            .build();

            Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
            upiPayIntent.setData(uri);

            Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

            if (null != chooser.resolveActivity(getPackageManager())) {
                startActivityForResult(chooser, UPI_PAYMENT);
            } else {
                Toast.makeText(ConfirmActivity.this, "No UPI app found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean InternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()
                    && networkInfo.isConnectedOrConnecting()
                    && networkInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Notification();
        if (requestCode == UPI_PAYMENT) {
            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String text = data.getStringExtra("response");
                    Log.e("UPI", "onActivityResult: " + text);
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(text);
                    upiPaymentOperation(dataList);

                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentOperation(dataList);

                }
            } else {
                Log.e("UPI", "onActivityResult: " + "Return data is null");
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentOperation(dataList);
            }
        }
    }

    public void Notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ConfirmActivity.this, "Appointment");
        builder.setContentTitle("Appointment Successful");
        builder.setContentText("Your appointment for " + category + " related problem is successfully fixed at " + time + " on " + date + ".");
        builder.setSmallIcon(R.drawable.ic_baseline_message_24);
        builder.setAutoCancel(true);

        Intent intent = new Intent(ConfirmActivity.this,
                PaymentActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(ConfirmActivity.this
                , 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(ConfirmActivity.this);
        managerCompat.notify(1, builder.build());
    }

    private void upiPaymentOperation(ArrayList<String> data) {
        if (InternetConnection(ConfirmActivity.this)) {
            String str = data.get(0);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) ||
                            equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[i];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {

                HashMap<String, String> appMap = new HashMap<>();
                appMap.put("Date", date);
                appMap.put("Time", time);
                appMap.put("Category", category);
                appMap.put("Name", newName);
                reference.child("Users").child(newNumber).child("appointment").push().setValue(appMap);
                reference.child("Admin").push().setValue(appMap);

                Intent myIntent = new Intent(ConfirmActivity.this, PaymentActivity.class);
                startActivity(myIntent);

            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(ConfirmActivity.this, "Payment cancelled by user", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ConfirmActivity.this, "Internet connection is not available", Toast.LENGTH_SHORT).show();

            }
        }
    }
}