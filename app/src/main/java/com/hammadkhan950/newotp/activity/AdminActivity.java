package com.hammadkhan950.newotp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hammadkhan950.newotp.R;
import com.hammadkhan950.newotp.adapter.AdminAdapter;
import com.hammadkhan950.newotp.model.AdminData;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    DatabaseReference reference;
    String number = "", newNumber = "";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        number = String.valueOf(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber());
        for (int i = 3; i < number.length(); i++) {
            newNumber = newNumber + number.charAt(i);
        }
        reference = FirebaseDatabase.getInstance().getReference().child("Admin");
        ArrayList<AdminData> adminDataList = new ArrayList<>();
        reference.orderByChild("Date").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NotNull DataSnapshot dataSnapshot, String s) {
                AdminData adminData = dataSnapshot.getValue(AdminData.class);
                adminDataList.add(adminData);
                adapter = new AdminAdapter(adminDataList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        adapter = new AdminAdapter(adminDataList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_share) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}