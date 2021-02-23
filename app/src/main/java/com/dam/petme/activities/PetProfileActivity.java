package com.dam.petme.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dam.petme.R;
import com.dam.petme.model.Pet;
import com.dam.petme.model.User;
import com.dam.petme.viewModel.PetViewModel;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class PetProfileActivity extends AppCompatActivity {

    TextView genderTextView, weightTextView, cityTextView, descriptionTextView, raceTextView, petNameTextView,
            petAgeTextView, stateTextView, responsableTextView;
    Button contactResponsableButton, addToFavouriteButton;
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
        if(pet.getName()!=null) petNameTextView.setText(pet.getName());
        else petNameTextView.setText("Nombre desconocido");
        if(pet.getAge()!=null) petAgeTextView.setText(String.valueOf(pet.getAge()));
        else petAgeTextView.setText("Edad desconocida");
        genderTextView.setText(pet.getGender().toString());
        if(pet.getWeight()!=null) weightTextView.setText(String.valueOf(pet.getWeight()));
        else weightTextView.setText("Peso desconocido");
        cityTextView.setText(pet.getCity()+", "+pet.getProvince());
        descriptionTextView.setText(pet.getDescription());
        if(pet.getRace()!=null) raceTextView.setText(pet.getRace());
        else raceTextView.setText("Raza desconocida");
        stateTextView.setText(pet.getStatus().toString());

        setResponsable(pet);
    }

    public void setResponsable(Pet pet) {
        mDatabase.child("users").child(pet.getResponsableId()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Log.d("firebase", String.valueOf(dataSnapshot.getValue()));
                User user = dataSnapshot.getValue(User.class);
                responsableTextView.setText(user.getName() + " " + user.getLastName());
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e("firebase", "Error getting data");
            }
        });
    }
}