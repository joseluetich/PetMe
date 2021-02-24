package com.dam.petme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import com.dam.petme.R;
import com.dam.petme.adapters.PetCardRecyclerViewAdapter;
import com.dam.petme.fragments.PetCardFragment;
import com.dam.petme.model.Pet;
import com.dam.petme.model.PetStatus;
import com.dam.petme.model.PetType;
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
    TextView titleTextView;
    View petCardFragment;
    private DatabaseReference mDatabase;

    public PetViewModel petViewModel;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_pets);

        status = (String) getIntent().getExtras().get("status");
        PetViewModel model = new ViewModelProvider(this).get(PetViewModel.class);
        model.setStatus(status);
        model.getPetsByStatus();


        lostPetsToolbar = findViewById(R.id.lostPetsToolbar);
        setSupportActionBar(lostPetsToolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        petCardFragment = findViewById(R.id.petsFragment);
        titleTextView = findViewById(R.id.titleTextView);
        addButton = findViewById(R.id.addButton);

        setTitle();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LostPetsActivity.this, AddPetActivity.class);
                intent.putExtra("status", status);
                intent.putExtra("from", "LostPetsActivity");
                startActivity(intent);
            }
        });

    }

    public void setTitle(){
        if(status.equals("FOUND")){
            titleTextView.setText(getResources().getString(R.string.foundPets));
        } else if(status.equals("LOST")){
            titleTextView.setText(getResources().getString(R.string.lostPets));
        } else{
            titleTextView.setText(getResources().getString(R.string.foundFriend));
        }

    }
}