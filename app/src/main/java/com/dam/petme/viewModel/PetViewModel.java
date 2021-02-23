package com.dam.petme.viewModel;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dam.petme.fragments.PetCardFragment;
import com.dam.petme.model.Pet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PetViewModel extends ViewModel {

    public MutableLiveData<Pet> selectedPet = new MutableLiveData<Pet>();
    private MutableLiveData<List<Pet>> pets;
    private DatabaseReference mDatabase;

    public LiveData<List<Pet>> getPetsByStatus(String status) {
        if (pets == null) {
            pets = new MutableLiveData<List<Pet>>();
            loadPets(status);
        }
        return pets;
    }

    public void selectPet(Pet pet) {
        System.out.println("seleccionn "+pet);
        selectedPet.setValue(pet);
        //selectedPet = new MutableLiveData<Pet>(pet);
    }

    public LiveData<Pet> getSelectedPet() {
        if(selectedPet == null) {
            selectedPet = new MutableLiveData<Pet>();
        }
        System.out.println(selectedPet);
        return selectedPet;
    }

    public void setPets(ArrayList<Pet> petArrayList) {
        pets.setValue(petArrayList);
    }

    private void loadPets(String status) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Do an asynchronous operation to fetch users.
        ArrayList<Pet> petsAux = new ArrayList();

        Query myTopPostsQuery = mDatabase.child("pets").orderByChild("status").equalTo("FOUND");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                petsAux.clear();
                for (DataSnapshot petsSnapshot : dataSnapshot.getChildren()) {
                    Pet petAux = petsSnapshot.getValue(Pet.class);
                    petsAux.add(petAux);
                }
                pets.setValue(petsAux);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FB/Database", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }
}
