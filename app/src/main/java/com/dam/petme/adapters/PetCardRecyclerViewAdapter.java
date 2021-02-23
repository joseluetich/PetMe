package com.dam.petme.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.petme.R;
import com.dam.petme.activities.LostPetsActivity;
import com.dam.petme.activities.PetProfileActivity;
import com.dam.petme.fragments.PetCardFragment;
import com.dam.petme.model.Pet;
import com.dam.petme.model.PetType;
import com.dam.petme.utils.GlideApp;
import com.dam.petme.utils.MyAppGlideModule;
import com.dam.petme.viewModel.PetViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PetCardRecyclerViewAdapter extends RecyclerView.Adapter<PetCardRecyclerViewAdapter.ViewHolder> {

    private final List<Pet> pets;
    private PetViewModel petViewModel;

    public PetCardRecyclerViewAdapter(List<Pet> items, PetViewModel viewModel) {
        pets = items;
        petViewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pet_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = pets.get(position);
        holder.nameTextView.setText(holder.mItem.getName());
        holder.locationTextView.setText(holder.mItem.getCity() + ", " + holder.mItem.getProvince());
        if(holder.mItem.getProfileImage() != null) {
            // Reference to an image file in Cloud Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(holder.mItem.getProfileImage());

            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            GlideApp.with(holder.profilePictureImageView.getContext())
                    .load(storageReference)
                    .into(holder.profilePictureImageView);
        }
        holder.typeImageView.setImageDrawable(holder.mItem.getType() == PetType.CAT ? holder.catIcon : holder.dogIcon);

        holder.viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petViewModel.selectPet(holder.mItem);
                //petViewModel.selectedPet.setValue(holder.mItem);
                Intent intent = new Intent(view.getContext(), PetProfileActivity.class);
                intent.putExtra("petId", holder.mItem.getId());
                System.out.println("animal "+holder.mItem.getId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameTextView;
        public final TextView locationTextView;
        public final ImageView profilePictureImageView;
        public final ImageView typeImageView;
        public Pet mItem;
        public Button viewProfileButton;
        public Drawable catIcon, dogIcon;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            locationTextView = (TextView) view.findViewById(R.id.locationTextView);
            profilePictureImageView = (ImageView) view.findViewById(R.id.profilePictureImageView);
            typeImageView = (ImageView) view.findViewById(R.id.typeImageView);
            viewProfileButton = (Button) view.findViewById(R.id.viewProfileButton);

            catIcon = view.getResources().getDrawable(R.drawable.ic_cat);
            dogIcon = view.getResources().getDrawable(R.drawable.ic_dog);

        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }

}