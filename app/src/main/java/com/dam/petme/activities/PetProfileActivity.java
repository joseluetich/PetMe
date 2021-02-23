package com.dam.petme.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.petme.R;
import com.dam.petme.model.Pet;
import com.dam.petme.model.User;
import com.dam.petme.utils.GlideApp;
import com.dam.petme.viewModel.PetViewModel;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class PetProfileActivity extends AppCompatActivity {

    TextView genderTextView, weightTextView, cityTextView, descriptionTextView, raceTextView, petNameTextView,
            petAgeTextView, stateTextView, responsableTextView;
    Button contactResponsableButton, addToFavouriteButton;
    ImageView petProfileImageView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);


        String petId = (String) getIntent().getExtras().get("petId");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        genderTextView = findViewById(R.id.genderTextView);
        weightTextView = findViewById(R.id.weightTextView);
        cityTextView = findViewById(R.id.cityTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        raceTextView = findViewById(R.id.raceTextView);
        petNameTextView = findViewById(R.id.petNameTextView);
        petAgeTextView = findViewById(R.id.petAgeTextView);
        stateTextView = findViewById(R.id.stateTextView);
        responsableTextView = findViewById(R.id.responsableTextView);
        contactResponsableButton = findViewById(R.id.contactResponsableButton);
        addToFavouriteButton = findViewById(R.id.addToFavouriteButton);
        petProfileImageView = findViewById(R.id.petProfileImageView);

        mDatabase.child("pets").child(petId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Log.d("firebase", String.valueOf(dataSnapshot.getValue()));
                Pet pet = dataSnapshot.getValue(Pet.class);
                setPetFields(pet);
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e("firebase", "Error getting data");
            }
        });

        contactResponsableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO perfil del responsable sin botones abajo
            }
        });

        addToFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO agregar lista de favoritos a cada usuario por cada estado y agregar esa mascota a la lista
            }
        });

    }

    public void setPetFields(Pet pet) {
        System.out.println("RACE "+pet.getRace());
        System.out.println("NAME "+pet.getName());
        if(!pet.getName().equals(null) && !pet.getName().isEmpty())
            petNameTextView.setText(pet.getName());
        else
            petNameTextView.setText(getResources().getString(R.string.unknownName));

        if(pet.getAge()!=null)
            petAgeTextView.setText(String.valueOf(pet.getAge()));
        else
            petAgeTextView.setText(getResources().getString(R.string.unknownAge));

        if(pet.getWeight()!=null)
            weightTextView.setText(String.valueOf(pet.getWeight()));
        else
            weightTextView.setText(getResources().getString(R.string.unknownWeight));

        if(!pet.getRace().equals(null) && !pet.getRace().isEmpty())
            raceTextView.setText(pet.getRace());
        else
            raceTextView.setText(getResources().getString(R.string.unknownRace));

        String city = pet.getCity()+", "+pet.getProvince();
        cityTextView.setText(city);
        descriptionTextView.setText(pet.getDescription());
        stateTextView.setText(pet.getStatus().toString());
        genderTextView.setText(pet.getGender().toString());

        if(pet.getProfileImage() != null) {
            // Reference to an image file in Cloud Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(pet.getProfileImage());
            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            GlideApp.with(petProfileImageView.getContext())
                    .load(storageReference)
                    .into(petProfileImageView);
        }
        setResponsable(pet);
    }

    public void setResponsable(Pet pet) {
        mDatabase.child("users").child(pet.getResponsableId()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Log.d("firebase", String.valueOf(dataSnapshot.getValue()));
                User user = dataSnapshot.getValue(User.class);
                String name = user.getName() + " " + user.getLastName();
                responsableTextView.setText(name);
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e("firebase", "Error getting data");
            }
        });
    }
}