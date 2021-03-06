package com.dam.petme.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.petme.R;
import com.dam.petme.model.Gender;
import com.dam.petme.model.Pet;
import com.dam.petme.model.PetStatus;
import com.dam.petme.model.PetType;
import com.dam.petme.utils.NotificationPublisher;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class AddPetActivity extends AppCompatActivity {

    static final int CAMARA_REQUEST = 1;
    static final int GALERIA_REQUEST = 2;
    static final int MAP_CODE = 3;
    Toolbar uploadFoundPetToolbar;
    TextInputLayout nameTextInputLayout, raceTextInputLayout, descriptionTextInputLayout;
    Spinner provinceSpinner, citySpinner, genderSpinner, typeSpinner;
    Button locationButton, uploadFoundPetButton, takePhotoButton, uploadPhotoButton;
    TextView uploadFoundPetTextView, errorPhotoTextView;
    ArrayList<String> cities = new ArrayList<>();
    ArrayList<String> provinces = new ArrayList<>();
    ArrayList<String> genders = new ArrayList<>();
    ArrayList<String> types = new ArrayList<>();
    String name, race, description, province, city;
    Pet pet;
    Gender gender;
    PetType type;
    byte[] photo;
    StorageReference petPhotoRef, storageRef;
    FirebaseStorage storage;
    ImageView photoImageView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    PetStatus petStatus;
    String status;
    Double latitude = null, longitude = null;
    FirebaseUser userFb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        if (getIntent().getExtras().get("status") != null) {
            status = (String) getIntent().getExtras().get("status");
            petStatus = PetStatus.valueOf(status);
        }
        if (getIntent().getExtras().get("latitude") != null)
            latitude = (Double) getIntent().getExtras().get("latitude");
        if (getIntent().getExtras().get("longitude") != null)
            longitude = (Double) getIntent().getExtras().get("longitude");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        userFb = mAuth.getCurrentUser();

        //Get Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Creamos una referencia a nuestro Storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        uploadFoundPetToolbar = findViewById(R.id.uploadFoundPetToolbar);
        setSupportActionBar(uploadFoundPetToolbar);
        uploadFoundPetToolbar.setTitle("Mascotas "+petStatus.toStringPlural().toLowerCase());

        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        raceTextInputLayout = findViewById(R.id.raceTextInputLayout);
        genderSpinner = findViewById(R.id.genderSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);
        descriptionTextInputLayout = findViewById(R.id.descriptionTextInputLayout);
        provinceSpinner = findViewById(R.id.provinceSpinner);
        citySpinner = findViewById(R.id.citySpinner);
        locationButton = findViewById(R.id.uploadLocationFoundPetButton);
        uploadFoundPetButton = findViewById(R.id.uploadFoundPetButton);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        uploadPhotoButton = findViewById(R.id.uploadPhotoButton);
        photoImageView = findViewById(R.id.petImageView);
        uploadFoundPetTextView = findViewById(R.id.uploadFoundPetTextView);
        errorPhotoTextView = findViewById(R.id.errorPhotoTextView);

        uploadFoundPetTextView.setText("Agregar Mascota " + petStatus.toString());

        photoImageView.setVisibility(View.GONE);

        mandatoryFieldValidation(descriptionTextInputLayout);


        genders.add("Seleccione");
        genders.add("Macho");
        genders.add("Hembra");
        genders.add("Desconocido");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(AddPetActivity.this, android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        types.add("Seleccione");
        types.add("Perro");
        types.add("Gato");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(AddPetActivity.this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        try {
            JSONArray provincesArray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < provincesArray.length(); i++) {
                JSONObject provinceDetail = provincesArray.getJSONObject(i);
                String prov = provinceDetail.getString("admin_name");
                if (!provinces.contains(prov))
                    provinces.add(prov);
            }
            Collections.sort(provinces);
            provinces.add(0, getResources().getString(R.string.select));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    JSONArray citiesArray = new JSONArray(loadJSONFromAsset());
                    cities.clear();
                    for (int j = 0; j < citiesArray.length(); j++) {
                        JSONObject cityDetail = citiesArray.getJSONObject(j);
                        if (cityDetail.getString("admin_name").equals(provinceSpinner.getSelectedItem().toString())) {
                            String city = cityDetail.getString("city");
                            cities.add(city);
                        }
                    }
                    Collections.sort(cities);
                    if (cities.isEmpty()) {
                        citySpinner.setEnabled(false);
                    } else {
                        citySpinner.setEnabled(true);
                    }
                    cities.add(0, getResources().getString(R.string.select));
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(AddPetActivity.this, android.R.layout.simple_spinner_item, cities);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provinces);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(provinceAdapter);

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camaraIntent, CAMARA_REQUEST);
            }
        });

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galeriaIntent, GALERIA_REQUEST);
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPetActivity.this, PetLocationActivity.class);
                intent.putExtra("status", status);
                startActivityForResult(intent, MAP_CODE);
            }
        });

        uploadFoundPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = nameTextInputLayout.getEditText().getText().toString();
                description = descriptionTextInputLayout.getEditText().getText().toString();
                race = raceTextInputLayout.getEditText().getText().toString();
                province = provinceSpinner.getSelectedItem().toString();
                city = citySpinner.getSelectedItem().toString();

                if (genderSpinner.getSelectedItem().toString().equals("Hembra"))
                    gender = Gender.FEMALE;
                if (genderSpinner.getSelectedItem().toString().equals("Macho"))
                    gender = Gender.MALE;
                if (genderSpinner.getSelectedItem().toString().equals("Desconocido"))
                    gender = Gender.UNKNOWN;

                if (typeSpinner.getSelectedItem().toString().equals("Perro")) type = PetType.DOG;
                if (typeSpinner.getSelectedItem().toString().equals("Gato")) type = PetType.CAT;

                if (validateAllMandatoryFields()) {
                    pet = new Pet(name, race, null, null, description, gender, province, city, petStatus, type);
                    if(latitude==null) pet.setLatitude(null);
                    else pet.setLatitude(String.valueOf(latitude));
                    if(longitude==null)pet.setLongitude(null);
                    else pet.setLongitude(String.valueOf(longitude));
                    createPetInFireBase();
                }
            }
        });
    }

    public String loadJSONFromAsset() { //Lee y retorna el json como string
        String json = null;
        try {
            InputStream is = getAssets().open("CitiesJson");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public boolean validNewUser() {
        return validateAllMandatoryFields();
    }

    public boolean validateAllMandatoryFields() {
        boolean allCompleted = true;

        if (gender == null || gender.toString().toLowerCase().equals(getResources().getString(R.string.select).toLowerCase())) {
            ((TextView) genderSpinner.getSelectedView()).setError(" ");
            allCompleted = false;
        }

        if (type == null || type.toString().toLowerCase().equals(getResources().getString(R.string.select).toLowerCase())) {
            ((TextView) typeSpinner.getSelectedView()).setError(" ");
            allCompleted = false;
        }

        if (description == null || description.isEmpty()) {
            descriptionTextInputLayout.setHelperTextEnabled(false);
            descriptionTextInputLayout.setError(getString(R.string.mandatoryField));
            allCompleted = false;
        }

        if (province == null || province.isEmpty() || province.equals(getResources().getString(R.string.select))) {
            ((TextView) provinceSpinner.getSelectedView()).setError(" ");
            allCompleted = false;
        }

        if (city == null || city.isEmpty() || city.equals(getResources().getString(R.string.select))) {
            ((TextView) citySpinner.getSelectedView()).setError(" ");
            allCompleted = false;
        }

        if (photo == null) {
            //takePhotoButton.setError(getResources().getString(R.string.mustAddAPhoto));
            //uploadPhotoButton.setError(getResources().getString(R.string.mustAddAPhoto));
            errorPhotoTextView.setVisibility(View.VISIBLE);
            allCompleted = false;
        }

        return allCompleted;
    }

    public void mandatoryFieldValidation(final TextInputLayout field) {
        Objects.requireNonNull(field.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(field.getEditText().getText())) {
                    // Clear error text
                    field.setHelperTextEnabled(true);
                    field.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(field.getEditText().getText())) {
                    field.setHelperTextEnabled(false);
                    field.setError(getString(R.string.mandatoryField));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMARA_REQUEST) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                photo = baos.toByteArray(); // Imagen en arreglo de bytes

                photoImageView.setVisibility(View.VISIBLE); //preview
                photoImageView.setImageBitmap(imageBitmap);

                if (photo != null) {
                    errorPhotoTextView.setVisibility(View.GONE);
                }

            } else if (requestCode == GALERIA_REQUEST) {
                Uri selectedImage = data.getData();
                InputStream is;
                try {
                    is = getContentResolver().openInputStream(selectedImage);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    Bitmap bitmap = BitmapFactory.decodeStream(bis);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    photo = baos.toByteArray(); // Imagen en arreglo de bytes

                    photoImageView.setImageBitmap(bitmap);
                    photoImageView.setVisibility(View.VISIBLE);

                    if (photo != null) {
                        errorPhotoTextView.setVisibility(View.GONE);
                    }

                } catch (FileNotFoundException e) {
                }
            } else if (requestCode == MAP_CODE) {
                //Obtengo la latitud y longitud seleccionadas
                latitude = data.getDoubleExtra("latitude", 0d);
                longitude = data.getDoubleExtra("longitude", 0d);
            }
        }

    }

    public void savePhoto(Consumer<Boolean> onSuccess) {
        final Context context = this;

        // Creamos una referencia a 'images/plato_id.jpg'
        petPhotoRef = storageRef.child("pets/" + pet.getId() + "-profile.jpg");

        // Cualquiera de los tres métodos tienen la misma implementación, se debe utilizar el que corresponda
        UploadTask uploadTask = petPhotoRef.putBytes(photo);

        // Registramos un listener para saber el resultado de la operación
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continuamos con la tarea para obtener la URL
                return petPhotoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // URL de descarga del archivo
                    Uri downloadUri = task.getResult();
                    pet.setProfileImage("pets/" + pet.getId() + "-profile.jpg");
                    onSuccess.accept(true);
                } else {
                    Toast.makeText(context, "Error al cargar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createPetInFireBase() {
        pet.setId(mDatabase.child("pets").push().getKey());
        pet.setResponsableId(userFb.getUid());
        Map<String, Object> petValues = pet.toMap();
        mDatabase.child("pets").child(pet.getId())
                .setValue(petValues).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                Log.d("FB/DataBase", "savePet:success");
                savePhoto(bool -> {
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/pets/" + pet.getId(), pet.toMap());
                    mDatabase.updateChildren(childUpdates);
                    new SimpleAsyncTask(uploadFoundPetButton).execute();
                    finish();
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Write failed
                Log.w("FB/DataBase", "savePet:failure", e);
            }
        });

    }

    private static class SimpleAsyncTask extends AsyncTask<Void, Void, String> {

        private WeakReference<Button> mButton;

        SimpleAsyncTask(Button button) {
            mButton = new WeakReference<>(button);
        }

        @Override
        protected String doInBackground(Void... voids) {

            final int SLEEP_TIME = 1000;

            // Sleep for the random amount of time
            try {
                Log.println(Log.INFO, "G", "Going to sleep for " + SLEEP_TIME + " milliseconds!");
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Return a String result
            Log.println(Log.INFO, "G", "Awake at last after sleeping for " + SLEEP_TIME + " milliseconds!");
            return null;
        }

        protected void onPostExecute(String result) {
            Log.println(Log.INFO, "G","Awaking");
            NotificationPublisher notificationPublisher = NotificationPublisher.getInstance();
            Intent intent = new Intent();
            notificationPublisher.onReceive(mButton.get().getContext(),intent);
        }
    }
}