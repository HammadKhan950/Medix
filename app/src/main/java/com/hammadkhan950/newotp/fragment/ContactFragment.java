package com.hammadkhan950.newotp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hammadkhan950.newotp.activity.MapsActivity;
import com.hammadkhan950.newotp.R;


public class ContactFragment extends Fragment {
    CardView btnCall;
    CardView btnLocation;
    public static final int REQUEST_CALL = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        inflater.inflate(R.layout.fragment_contact, container, false);
        btnCall = view.findViewById(R.id.btnCall);
        btnLocation = view.findViewById(R.id.btnLocation);
        btnCall.setOnClickListener(view1 -> makePhoneCall());
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void makePhoneCall() {
        String number = "9839870495";

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }

    }

}