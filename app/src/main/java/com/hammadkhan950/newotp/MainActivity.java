package com.hammadkhan950.newotp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    DatabaseReference reference;
    // String number,newNumber;
    ImageView imageView;
    public static final int PICK_IMAGE = 1;
    Uri imageUri;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imgProfile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select your profile"), PICK_IMAGE);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading..");
                progressDialog.show();

                storageReference.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            Log.i("TAG",taskSnapshot.toString());
                            reference.child("9839870495").child("profile").setValue(imageUri);
                            progressDialog.dismiss();

                            Toast.makeText(MainActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        })
                        .addOnProgressListener(snapshot -> {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            Log.i("Uploading", "Uploaded " + (int) progress + "%");
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");

                        });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}