package com.hammadkhan950.newotp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hammadkhan950.newotp.adapter.AppointmentHistoryAdapter;
import com.hammadkhan950.newotp.R;
import com.hammadkhan950.newotp.model.Appointment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;


public class AppointmentFragment extends Fragment {
    DatabaseReference reference;
    String number = "", newNumber = "";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());


        number = String.valueOf(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber());

        for (int i = 3; i < number.length(); i++) {
            newNumber = newNumber + number.charAt(i);
        }
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(newNumber).child("appointment");

        ArrayList<Appointment> appointmentList = new ArrayList<>();

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NotNull DataSnapshot dataSnapshot, String s) {
                Appointment appointment = dataSnapshot.getValue(Appointment.class);
                appointmentList.add(appointment);
                adapter = new AppointmentHistoryAdapter(appointmentList);
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

        adapter = new AppointmentHistoryAdapter(appointmentList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        return view;

    }
}