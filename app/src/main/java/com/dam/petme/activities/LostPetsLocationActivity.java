package com.dam.petme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.dam.petme.R;
import com.dam.petme.adapters.PetCardRecyclerViewAdapter;
import com.dam.petme.model.Pet;
import com.dam.petme.model.PetStatus;
import com.dam.petme.viewModel.PetViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class LostPetsLocationActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, OnMapReadyCallback {
    private GoogleMap map;
    Toolbar lostPetsLocationToolbar;
    PetStatus petStatus;
    Button viewProfileButton;
    TextView titleTextView;
    Marker m;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_pets_location);

        status = (String) getIntent().getExtras().get("status");
        petStatus = PetStatus.valueOf(status);

        lostPetsLocationToolbar = findViewById(R.id.lostPetsLocationToolbar);
        setSupportActionBar(lostPetsLocationToolbar);

        viewProfileButton = findViewById(R.id.viewProfileButton);
        titleTextView = findViewById(R.id.titleTextView);

        String title = "Mascotas "+petStatus.toStringPlural().toLowerCase();
        titleTextView.setText(title);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng initialPoint = new LatLng(-31.636633, -60.699569); //Ubicacion por defecto en santa fe

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(this);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPoint,15));
            }
        }

        PetViewModel model = new ViewModelProvider(this).get(PetViewModel.class);

        //Obtengo todos las mascotas encontradas
        model.setStatus(petStatus.toStringEnglish());
        model.getPetsByStatus().observe(this, pets -> {
            System.out.println("pets ++ "+pets); //Mascotas encontradas
            System.out.println("status "+petStatus.toStringEnglish());
            for(Pet pet : pets) {
                System.out.println(pet.getName()+" "+pet.getStatus()); //Obtengo el nombre y estado
                System.out.println("Latitud: "+pet.getLatitude()); //Salida de latitud
                System.out.println("Longitud: "+pet.getLongitude()); //Salida de longitud
                if(pet.getLatitude()!=null && pet.getLongitude()!=null
                && !pet.getLatitude().equals("null") && !pet.getLongitude().equals("null")) { //Si la latitud ni la longitud iguala a null
                    double latitude = Double.parseDouble(pet.getLatitude()); //Entonces asigna la latitud a esta variable
                    double longitude = Double.parseDouble(pet.getLongitude()); //Y la longitud a esta variable
                    LatLng marker = new LatLng(latitude, longitude); //Ubicacion por defecto en santa fe
                    m = map.addMarker(new MarkerOptions()
                            .position(marker)
                            .title("Mascota "+petStatus.toString().toLowerCase()+" aquí")
                            .draggable(false));
                }
            }
        });

        /*map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if(m == null) { // si todavia no existe
                    m = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Mascota "+status.toString().toLowerCase()+" aquí")
                            .draggable(false));
                    if(status.equals(PetStatus.LOST)) {
                        m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                    if(status.equals(PetStatus.FOUND)) {
                        m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }
                    if(status.equals(PetStatus.ADOPTED)) {
                        m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    }
                }
                else {
                    m.setPosition(latLng); //reubico el marcador
                }
                location = latLng;
            }
        });*/

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}