package com.dam.petme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.petme.R;
import com.dam.petme.activities.LostPetsActivity;
import com.dam.petme.adapters.PetCardRecyclerViewAdapter;
import com.dam.petme.model.Pet;
import com.dam.petme.viewModel.PetViewModel;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class PetCardFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<Pet> pets = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PetCardFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PetCardFragment newInstance(int columnCount) {
        PetCardFragment fragment = new PetCardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pets_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new PetCardRecyclerViewAdapter(this.pets));
            PetViewModel model = new ViewModelProvider(this).get(PetViewModel.class);
            model.getPetsByStatus("FOUND").observe(requireActivity(), pets -> {
                // update UI
                System.out.println("Fragment");
                System.out.println(pets);
                this.pets = (ArrayList<Pet>) pets;
                recyclerView.setAdapter(new PetCardRecyclerViewAdapter(this.pets));
                //recyclerView.getAdapter().notifyDataSetChanged();
            });
        }


        /*((LostPetsActivity) getActivity()).setFragmentRefreshListener(new LostPetsActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                // your method
                if (getArguments() != null) {
                    pets = getArguments().getParcelableArrayList("petsList");
                    System.out.println(pets);
                    System.out.println(pets.size());
                }
            }
        });*/
        return view;
    }

}