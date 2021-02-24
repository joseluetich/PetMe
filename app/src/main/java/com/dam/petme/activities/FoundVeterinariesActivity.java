package com.dam.petme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.dam.petme.R;

public class FoundVeterinariesActivity extends AppCompatActivity {

    Toolbar foundVeterinariesToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_veterinaries);

        foundVeterinariesToolbar = findViewById(R.id.foundVeterinariesToolbar);
        setSupportActionBar(foundVeterinariesToolbar);
    }
}