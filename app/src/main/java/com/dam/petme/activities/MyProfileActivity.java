package com.dam.petme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dam.petme.R;
import com.dam.petme.model.User;
import com.dam.petme.utils.GlideApp;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class MyProfileActivity extends AppCompatActivity {

    Toolbar myProfileToolbar;
    Button myCollectionButton;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView usernameTextView, ageTextView, cityProfileTextView, emailProfileTextView, phoneTextView,
            descriptionTextView, workSituationTextView;
    ImageView userProfileImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFb = mAuth.getCurrentUser();

        myCollectionButton = findViewById(R.id.myCollectionButton);
        myProfileToolbar = findViewById(R.id.myProfileToolbar);
        setSupportActionBar(myProfileToolbar);

        usernameTextView = findViewById(R.id.usernameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        cityProfileTextView = findViewById(R.id.cityProfileTextView);
        emailProfileTextView = findViewById(R.id.emailProfileTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        workSituationTextView = findViewById(R.id.workSituationTextView);
        userProfileImageView = findViewById(R.id.userProfileImageView);

        //TODO Usar listener para escuchar los cambios on edit
        mDatabase.child("users").child(userFb.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Log.d("firebase", String.valueOf(dataSnapshot.getValue()));
                User user = dataSnapshot.getValue(User.class);
                completeFields(user);

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e("firebase", "Error getting data");
            }
        });


        myCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, ProfileCollectionActivity.class);
                startActivity(intent);
            }
        });
    }

    public void completeFields(User user){
        String completeName = user.getName() + " " + user.getLastName();
        usernameTextView.setText(completeName);

        Long ageL = (new Date()).getTime() - user.getBirthday().getTime();
        Date age = new Date(ageL);
        String completeAge = (age.getYear() - 70) + " años";
        ageTextView.setText(completeAge);

        cityProfileTextView.setText(user.getCity());
        emailProfileTextView.setText(user.getEmail());

        if(user.getPhone()==null)
            phoneTextView.setText(getResources().getString(R.string.unknownPhone));
        else
            phoneTextView.setText(user.getPhone());

        if(user.getDescription()==null)
            descriptionTextView.setText(getResources().getString(R.string.unknownDescription));
        else
            descriptionTextView.setText(user.getDescription());

        if(user.getWorkSituation()==null)
            workSituationTextView.setText(getResources().getString(R.string.unknownSituation));
        else
            workSituationTextView.setText(user.getWorkSituation());

        if(user.getPhoto() != null) {
            // Reference to an image file in Cloud Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(user.getPhoto());
            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            GlideApp.with(userProfileImageView.getContext())
                    .load(storageReference)
                    .into(userProfileImageView);
        }
    }
}