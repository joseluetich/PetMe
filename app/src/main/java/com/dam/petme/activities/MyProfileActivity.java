package com.dam.petme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dam.petme.R;

public class MyProfileActivity extends AppCompatActivity {

    Toolbar myProfileToolbar;
    Button myCollectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        myCollectionButton = findViewById(R.id.myCollectionButton);
        myProfileToolbar = findViewById(R.id.myProfileToolbar);
        setSupportActionBar(myProfileToolbar);


        myCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, ProfileCollectionActivity.class);
                startActivity(intent);
            }
        });


    }
}