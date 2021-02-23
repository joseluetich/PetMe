package com.dam.petme.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.dam.petme.R;
import com.dam.petme.fragments.PetCardFragment;
import com.dam.petme.model.Pet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LostPetsActivity extends AppCompatActivity {

    Toolbar lostPetsToolbar;
    View petsFragment;
    private DatabaseReference mDatabase;

    private FragmentRefreshListener fragmentRefreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_pets);

        lostPetsToolbar = findViewById(R.id.lostPetsToolbar);
        setSupportActionBar(lostPetsToolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

//        Intent intent = new Intent(LogInActivity.this, AddPetActivity.class);
//        intent.putExtra("status", PetStatus.FOUND);
//        //TODO cambiar from a la actividad llamante
//        intent.putExtra("from","LogInActivity");
//        startActivity(intent);

        if (savedInstanceState == null) {
            ArrayList<Pet> pets = new ArrayList();
            // My top posts by number of stars
            Query myTopPostsQuery = mDatabase.child("pets").orderByChild("status").equalTo("FOUND");
            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot);
                    for (DataSnapshot petsSnapshot : dataSnapshot.getChildren()) {
                        System.out.println(petsSnapshot);
                        Pet petAux = petsSnapshot.getValue(Pet.class);
                        System.out.println(petAux);
                        pets.add(petAux);
                    }
                    PetCardFragment f = new PetCardFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("petsList", (ArrayList<? extends Parcelable>) pets);
                    f.setArguments(bundle);

                    if(getFragmentRefreshListener()!= null){
                        getFragmentRefreshListener().onRefresh();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("FB/Database", "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            });

            PetCardFragment f = new PetCardFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("petsList", (ArrayList<? extends Parcelable>) pets);
            f.setArguments(bundle);
        }
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    public interface FragmentRefreshListener{
        void onRefresh();
    }
}