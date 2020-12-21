package com.hammadkhan950.newotp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.InternalTokenProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hammadkhan950.newotp.R;
import com.hammadkhan950.newotp.activity.HomeActivity;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    TextView tvName, tvNumber, tvGender, tvAge;
    DatabaseReference reference;
    String number = "";
    String newNumber = "";
    ImageView imageView;
    public static final int PICK_IMAGE = 1;
    Uri imageUri;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvName = view.findViewById(R.id.tv_name);
        tvNumber = view.findViewById(R.id.tv_number);
        tvGender = view.findViewById(R.id.tv_gender);
        tvAge = view.findViewById(R.id.tv_age);
        imageView = view.findViewById(R.id.imgProfile);
        number = String.valueOf(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber());

        for (int i = 3; i < number.length(); i++) {
            newNumber = newNumber + number.charAt(i);
        }
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(newNumber);


        imageView.setOnClickListener(view1 -> {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select your profile"), PICK_IMAGE);
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String newName = dataSnapshot.child("name").getValue().toString();
                String newNumber = dataSnapshot.child("number").getValue().toString();
                String newAge = dataSnapshot.child("age").getValue().toString();
                String newGender = dataSnapshot.child("gender").getValue().toString();
                if (dataSnapshot.child("profile").exists()) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH);
                    Glide.with(Objects.requireNonNull(getContext()))
                            .load(dataSnapshot.child("profile").getValue().toString())
                            .apply(options)
                            .into(imageView);


                } else {


                    Glide.with(Objects.requireNonNull(getContext()))
                            .load(R.drawable.ic_myprofile)
                            .into(imageView);

                }

                tvName.setText(newName);
                tvNumber.setText(newNumber);
                tvAge.setText(newAge);
                tvGender.setText(newGender);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading..");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                storageReference.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            reference.child("profile").setValue(imageUri.toString());
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                        })
                        .addOnProgressListener(snapshot -> {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}