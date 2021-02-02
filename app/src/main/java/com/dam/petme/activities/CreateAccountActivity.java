package com.dam.petme.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.petme.R;
import com.dam.petme.model.User;
import com.dam.petme.fragments.DatePickerFragment;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

    TextInputLayout birthDateTextInputLayout, lastNameTextInputLayout, nameTextInputLayout, emailTextInputLayout,
            passwordTextInputLayout;
    EditText birthdateEditText;
    Spinner citySpinner, provinceSpinner;
    Button createAccountButton;
    User user;
    ArrayList<String> cities = new ArrayList<>();
    ArrayList<String> provinces = new ArrayList<>();
    String lastName, name, email, password, birthdate, province, city;
    Date birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        lastNameTextInputLayout = findViewById(R.id.lastNameTextInputLayout);
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout);
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        birthDateTextInputLayout = findViewById(R.id.birthDateTextInputLayout);
        citySpinner = findViewById(R.id.citySpinner);
        provinceSpinner = findViewById(R.id.provinceSpinner);
        birthdateEditText = findViewById(R.id.birthDateEditText);
        createAccountButton = findViewById(R.id.createAccountButton);

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
                if(!provinces.contains(prov))
                    provinces.add(prov);
            }
            Collections.sort(provinces);
            provinces.add(0,getResources().getString(R.string.select));
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
                        if(cityDetail.getString("admin_name").equals(provinceSpinner.getSelectedItem().toString())) {
                            String city = cityDetail.getString("city");
                            cities.add(city);
                        }
                    }
                    Collections.sort(cities);
                    if(cities.isEmpty()) {
                        citySpinner.setEnabled(false);
                    }
                    else {
                        citySpinner.setEnabled(true);
                    }
                    cities.add(0,getResources().getString(R.string.select));
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

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    birth = dateFormat.parse(birthdate);
                    System.out.println(birth.toString());
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                if(validNewUser()) {
                    //Intent intent = new Intent(LogInActivity.this, CreateAccountActivity.class);
                    //startActivity(intent);
                    user = new User(lastName,name,email,password,birth,province,city);
                    Toast.makeText(CreateAccountActivity.this, "Se ha creado la cuenta exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                birthdateEditText.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
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


}