package com.dam.petme.adapters;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.petme.R;
import com.dam.petme.model.Pet;
import com.dam.petme.model.PetType;
import com.dam.petme.utils.GlideApp;
import com.dam.petme.utils.MyAppGlideModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PetCardRecyclerViewAdapter extends RecyclerView.Adapter<PetCardRecyclerViewAdapter.ViewHolder> {

    private final List<Pet> pets;

    public PetCardRecyclerViewAdapter(List<Pet> items) {
        pets = items;
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

        public Drawable catIcon, dogIcon;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            locationTextView = (TextView) view.findViewById(R.id.locationTextView);
            profilePictureImageView = (ImageView) view.findViewById(R.id.profilePictureImageView);
            typeImageView = (ImageView) view.findViewById(R.id.typeImageView);

            catIcon = view.getResources().getDrawable(R.drawable.ic_cat);
            dogIcon = view.getResources().getDrawable(R.drawable.ic_dog);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }

}