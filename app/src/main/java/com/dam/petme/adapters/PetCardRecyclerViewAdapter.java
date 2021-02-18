package com.dam.petme.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dam.petme.R;
import com.dam.petme.dummy.DummyContent.DummyItem;
import com.dam.petme.model.Pet;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
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
        holder.profilePictureImageView.setImageBitmap(Bitmap.createBitmap(holder.mItem.getProfileImage()));
        holder.typeImageView = (ImageView) view.findViewById(R.id.typeImageView);
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

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            locationTextView = (TextView) view.findViewById(R.id.locationTextView);
            profilePictureImageView = (ImageView) view.findViewById(R.id.profilePictureImageView);
            typeImageView = (ImageView) view.findViewById(R.id.typeImageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}