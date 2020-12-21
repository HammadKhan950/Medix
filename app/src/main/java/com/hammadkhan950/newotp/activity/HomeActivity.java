package com.hammadkhan950.newotp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hammadkhan950.newotp.fragment.ContactFragment;
import com.hammadkhan950.newotp.R;
import com.hammadkhan950.newotp.fragment.AppointmentFragment;
import com.hammadkhan950.newotp.fragment.HomeFragment;
import com.hammadkhan950.newotp.fragment.ProfileFragment;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    DatabaseReference reference;
    String number = "", newNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        number = String.valueOf(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber());

        for (int i = 3; i < number.length(); i++) {
            newNumber = newNumber + number.charAt(i);
        }

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View convertView =
                navigationView.getHeaderView(0);
        TextView username = convertView.findViewById(R.id.tvName);
        ImageView imgProfile = convertView.findViewById(R.id.imgProfile);
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(newNumber);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String newName = dataSnapshot.child("name").getValue().toString();
                if (dataSnapshot.child("profile").exists()) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH);
                    Glide.with(HomeActivity.this)
                            .load(dataSnapshot.child("profile").getValue().toString())
                            .apply(options)
                            .into(imgProfile);
                } else {


                    Glide.with(HomeActivity.this)
                            .load(R.drawable.ic_myprofile)
                            .into(imgProfile);

                }
                username.setText(newName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            getSupportActionBar().setTitle(" Dashboard");
            navigationView.setCheckedItem(R.id.home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                getSupportActionBar().setTitle(" Dashboard");
                break;
            case R.id.myprofile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                getSupportActionBar().setTitle(" My Details");
                break;
            case R.id.appHistory:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AppointmentFragment()).commit();
                getSupportActionBar().setTitle(" My Appointments");
                break;
            case R.id.contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ContactFragment()).commit();
                getSupportActionBar().setTitle(" Contact Us");
                break;
            case R.id.logout:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Are you sure you want to log out?");
                alert.setPositiveButton("Yes",
                        (arg0, arg1) -> {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                        });
                alert.setNegativeButton("No", null);
                AlertDialog al = alert.create();
                al.show();
                break;
            case R.id.nav_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://play.google.com/store/apps/details?id=com.hammadkhan950.mybooksapp";
                String shareSubject = "Your body here";
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                startActivity(Intent.createChooser(sharingIntent, "Share Using"));
                break;
            case R.id.about:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("This app is designed and developed by - \nHammad Khan \nFuzail Khan \nMd Ejaz Ansari");
                alertDialogBuilder.setPositiveButton("Visit",
                        (arg0, arg1) -> {
                            Intent webIntent = new Intent(getApplicationContext(), WebActivity.class);
                            webIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(webIntent);

                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnected(this)) {
            showCustomDialog();
        }

    }

    private boolean isConnected(HomeActivity homeActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) homeActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Please connect to the Internet")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}