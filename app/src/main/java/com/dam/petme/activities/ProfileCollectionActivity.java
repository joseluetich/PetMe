package com.dam.petme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.dam.petme.R;

public class ProfileCollectionActivity extends AppCompatActivity {

    Toolbar profileCollectionToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_collection);

        profileCollectionToolbar = findViewById(R.id.profileCollectionToolbar);
        setSupportActionBar(profileCollectionToolbar);

    }
}