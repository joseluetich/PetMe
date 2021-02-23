package com.dam.petme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.dam.petme.R;
import com.dam.petme.model.Pet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LostPetsActivity extends AppCompatActivity {

    Toolbar lostPetsToolbar;
    View petsFragment;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_pets);

        lostPetsToolbar = findViewById(R.id.lostPetsToolbar);
        setSupportActionBar(lostPetsToolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (savedInstanceState == null) {
            ArrayList<Pet> pets = new ArrayList();
            // My top posts by number of stars
            Query myTopPostsQuery = mDatabase.child("pets");
            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot);
                    for (DataSnapshot petsSnapshot: dataSnapshot.getChildren()) {
                        // TODO: handle the post
                        System.out.println(petsSnapshot);
                        pets.add(dataSnapshot.getValue(Pet.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("FB/Database", "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            });

            Fragment f = new Fragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("petsList", (ArrayList<? extends Parcelable>) pets);
            f.setArguments(bundle);
        }
    }
}