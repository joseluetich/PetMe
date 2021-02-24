package com.dam.petme.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dam.petme.R;
import com.dam.petme.fragments.DatePickerFragment;
import com.dam.petme.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

    static final int CAMARA_REQUEST = 1;
    static final int GALERIA_REQUEST = 2;
    Toolbar createAccountToolbar;
    TextInputLayout birthDateTextInputLayout, lastNameTextInputLayout, nameTextInputLayout, emailTextInputLayout,
            passwordTextInputLayout;
    EditText birthdateEditText;
    Spinner citySpinner, provinceSpinner;
    Button createAccountButton, takePhotoButton, uploadPhotoButton;
    User user;
    ArrayList<String> cities = new ArrayList<>();
    ArrayList<String> provinces = new ArrayList<>();
    String lastName, name, email, password, birthdate, province, city;
    Date birth;
    byte[] photo;
    StorageReference userPhotoRef, storageRef;
    FirebaseStorage storage;
    ImageView photoImageView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createAccountToolbar = findViewById(R.id.createAccountToolbar);
        setSupportActionBar(createAccountToolbar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Get Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Creamos una referencia a nuestro Storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        lastNameTextInputLayout = findViewById(R.id.lastNameTextInputLayout);
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        birthDateTextInputLayout = findViewById(R.id.birthDateTextInputLayout);
        citySpinner = findViewById(R.id.citySpinner);
        provinceSpinner = findViewById(R.id.provinceSpinner);
        birthdateEditText = findViewById(R.id.birthDateEditText);
        createAccountButton = findViewById(R.id.createAccountButton);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        uploadPhotoButton = findViewById(R.id.uploadPhotoButton);
        photoImageView = findViewById(R.id.userProfileImageView);

        photoImageView.setVisibility(View.GONE);

        createAccountToolbar = findViewById(R.id.createAccountToolbar);
        setSupportActionBar(createAccountToolbar);

        birthdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        mandatoryFieldValidation(nameTextInputLayout);
        mandatoryFieldValidation(lastNameTextInputLayout);
        mandatoryFieldValidation(emailTextInputLayout);
        mandatoryFieldValidation(passwordTextInputLayout);
        mandatoryFieldValidation(birthDateTextInputLayout);

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
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(CreateAccountActivity.this, android.R.layout.simple_spinner_item, cities);
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

        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provinces);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(provinceAdapter);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastName = lastNameTextInputLayout.getEditText().getText().toString();
                name = nameTextInputLayout.getEditText().getText().toString();
                email = emailTextInputLayout.getEditText().getText().toString();
                password = passwordTextInputLayout.getEditText().getText().toString();
                birthdate = birthdateEditText.getText().toString();
                province = provinceSpinner.getSelectedItem().toString();
                city = citySpinner.getSelectedItem().toString();

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("es_AR"));
                try {
                    birth = dateFormat.parse(birthdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (validNewUser()) {
                    user = new User(lastName, name, email, password, birth, province, city);
                    savePhoto();
                    createUserInFireBase(user);
                }
            }
        });

    }

    private void createUserInFireBase(User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FB/Auth", "createUserWithEmail:success");
                            FirebaseUser userFb = mAuth.getCurrentUser();
                            // Create new post at /user-posts/$userid/$postid and at
                            // /posts/$postid simultaneously

                            Map<String, Object> userValues = user.toMap();
                            mDatabase.child("users").child(userFb.getUid())
                                    .setValue(userValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    Log.d("FB/DataBase", "saveUser:success");
                                    loginUser(user.getEmail(), user.getPassword());
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Write failed
                                            Log.w("FB/DataBase", "saveUser:failure", e);
                                        }
                                    });

                            Toast.makeText(CreateAccountActivity.this, "Se ha creado la cuenta exitosamente", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FB/Auth", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month + 1) + "/" + year;
                birthdateEditText.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
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

        if (lastName == null || lastName.isEmpty()) {
            lastNameTextInputLayout.setHelperTextEnabled(false);
            lastNameTextInputLayout.setError(getString(R.string.mandatoryField));
            allCompleted = false;
        }

        if (name == null || name.isEmpty()) {
            nameTextInputLayout.setHelperTextEnabled(false);
            nameTextInputLayout.setError(getString(R.string.mandatoryField));
            allCompleted = false;
        }

        if (email == null || email.isEmpty()) {
            emailTextInputLayout.setHelperTextEnabled(false);
            emailTextInputLayout.setError(getString(R.string.mandatoryField));
            allCompleted = false;
        }

        if (password == null || password.isEmpty()) {
            passwordTextInputLayout.setHelperTextEnabled(false);
            passwordTextInputLayout.setError(getString(R.string.mandatoryField));
            allCompleted = false;
        }

        if (birthdate == null || birthdate.isEmpty()) {
            birthDateTextInputLayout.setHelperTextEnabled(false);
            birthDateTextInputLayout.setError(getString(R.string.mandatoryField));
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
                } catch (FileNotFoundException e) {
                }
            }
        }

    }

    public void savePhoto() {
        final Context context = this;

        // Creamos una referencia a 'images/plato_id.jpg'
        userPhotoRef = storageRef.child("users/" + user.getEmail() + "-profile.jpg");

        // Cualquiera de los tres métodos tienen la misma implementación, se debe utilizar el que corresponda
        UploadTask uploadTask = userPhotoRef.putBytes(photo);
        // UploadTask uploadTask = platosImagesRef.putFile(file);
        // UploadTask uploadTask = platosImagesRef.putStream(stream);

        // Registramos un listener para saber el resultado de la operación
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continuamos con la tarea para obtener la URL
                return userPhotoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // URL de descarga del archivo
                    Uri downloadUri = task.getResult();
                    user.setPhoto(downloadUri.toString());
                } else {
                    Toast.makeText(context, "Error al cargar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("FB/Auth", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("FB/Auth", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
    }

}