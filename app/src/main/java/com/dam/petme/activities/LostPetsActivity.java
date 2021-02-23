package com.dam.petme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dam.petme.R;
import com.dam.petme.fragments.PetCardFragment;
import com.dam.petme.model.Pet;
import com.dam.petme.model.PetStatus;
import com.dam.petme.viewModel.PetViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LostPetsActivity extends AppCompatActivity {

    Toolbar lostPetsToolbar;
    FloatingActionButton addButton;
    View petsFragment;
    private DatabaseReference mDatabase;

    //private FragmentRefreshListener fragmentRefreshListener;
    private PetViewModel petViewModel;
    private LostPetsActivity thisLosPetsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_pets);

        lostPetsToolbar = findViewById(R.id.lostPetsToolbar);
        setSupportActionBar(lostPetsToolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LostPetsActivity.this, AddPetActivity.class);
                intent.putExtra("status", PetStatus.FOUND);
                intent.putExtra("from", "LostPetsActivity");
                startActivity(intent);
            }
        });

    }
}