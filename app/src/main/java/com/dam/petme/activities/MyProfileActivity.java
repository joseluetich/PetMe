package com.dam.petme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dam.petme.R;
import com.dam.petme.model.User;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class MyProfileActivity extends AppCompatActivity {

    Toolbar myProfileToolbar;
    Button myCollectionButton;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView usernameTextView, ageTextView, cityProfileTextView, emailProfileTextView;


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

        //TODO Usar listener para escuchar los cambios on edit
        mDatabase.child("users").child(userFb.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Log.d("firebase", String.valueOf(dataSnapshot.getValue()));
                User user = dataSnapshot.getValue(User.class);
                usernameTextView.setText(user.getName() + " " + user.getLastName());
                Long ageL = (new Date()).getTime() - user.getBirthday().getTime();
                Date age = new Date(ageL);
                ageTextView.setText((age.getYear() - 70) + " años");
                cityProfileTextView.setText(user.getCity());
                emailProfileTextView.setText(user.getEmail());
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
}