package com.dam.petme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.dam.petme.R;

public class DonatePetActivity extends AppCompatActivity {

    Toolbar donatePetToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_pet);

        donatePetToolbar = findViewById(R.id.donatePetToolbar);
        setSupportActionBar(donatePetToolbar);
    }
}