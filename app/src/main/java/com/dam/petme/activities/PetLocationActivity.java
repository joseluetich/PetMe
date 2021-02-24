package com.dam.petme.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.petme.R;
import com.dam.petme.model.PetStatus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PetLocationActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, OnMapReadyCallback {
    private GoogleMap map;
    Toolbar petLocationToolbar;
    TextView descriptionLocationTextView;
    Button confirmLocationButton;
    LatLng location;
    Marker m = null;
    PetStatus petStatus;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_location);

        confirmLocationButton = findViewById(R.id.confirmLocationButton);
        descriptionLocationTextView = findViewById(R.id.descriptionLocationTextView);

        petLocationToolbar = findViewById(R.id.petLocationToolbar);
        setSupportActionBar(petLocationToolbar);//configuro la toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        status = (String) getIntent().getExtras().get("status");
        petStatus = PetStatus.valueOf(status);

        descriptionLocationTextView.setText("Situar en el mapa la ubicación en la que se "+petStatus.toStringPast()+" la mascota");

        final Context context = this;
        confirmLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m==null) {
                    Toast.makeText(context, "Seleccione una ubicacion en el mapa",Toast.LENGTH_LONG).show();
                }
                else {
                    double latitude = location.latitude;
                    double longitude = location.longitude;
                    Intent intent = new Intent(PetLocationActivity.this, AddPetActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    System.out.println("latitud: "+latitude+ " longitud: "+longitude);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng initialPoint = new LatLng(-31.636633, -60.699569);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(this);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPoint,15));
            }
        }


        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if(m == null) { // si todavia no existe
                    m = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Mascota "+petStatus.toString().toLowerCase()+" aquí")
                            .draggable(false));
                    if(petStatus.equals(PetStatus.LOST)) {
                        m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                    if(petStatus.equals(PetStatus.FOUND)) {
                        m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }
                    if(petStatus.equals(PetStatus.ADOPTED)) {
                        m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    }
                }
                else {
                    m.setPosition(latLng); //reubico el marcador
                }
                location = latLng;
            }
        });

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}