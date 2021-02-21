package com.dam.petme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dam.petme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity {

    Button myProfileButton, adoptPetButton, donatePetButton, foundPetsButton, lostPetsButton, foundVeterinariesButton;
    TextView signOutTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userFb = mAuth.getCurrentUser();

        myProfileButton = findViewById(R.id.myProfileButton);
        adoptPetButton = findViewById(R.id.adoptPetButton);
        donatePetButton = findViewById(R.id.donatePetButton);
        foundPetsButton = findViewById(R.id.foundPetsButton);
        lostPetsButton = findViewById(R.id.lostPetsButton);
        foundVeterinariesButton = findViewById(R.id.findVeterinaryButton);
        signOutTextView = findViewById(R.id.signOutTextView);

        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });

        adoptPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AdoptPetActivity.class);
                startActivity(intent);
            }
        });

        donatePetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, DonatePetActivity.class);
                startActivity(intent);
            }
        });

        foundPetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FoundPetsActivity.class);
                startActivity(intent);
            }
        });

        lostPetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LostPetsActivity.class);
                startActivity(intent);
            }
        });

        foundVeterinariesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FoundVeterinariesActivity.class);
                startActivity(intent);
            }
        });

        signOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(HomeActivity.this, LogInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}