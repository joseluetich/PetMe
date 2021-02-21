package com.dam.petme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.dam.petme.R;

public class FoundPetsActivity extends AppCompatActivity {

    Toolbar foundPetsToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_pets);

        foundPetsToolbar = findViewById(R.id.foundPetsToolbar);
        setSupportActionBar(foundPetsToolbar);
    }
}