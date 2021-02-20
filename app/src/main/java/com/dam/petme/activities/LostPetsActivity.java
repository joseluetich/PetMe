package com.dam.petme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.dam.petme.R;

public class LostPetsActivity extends AppCompatActivity {

    Toolbar lostPetsToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_pets);

        lostPetsToolbar = findViewById(R.id.lostPetsToolbar);
        setSupportActionBar(lostPetsToolbar);
    }
}