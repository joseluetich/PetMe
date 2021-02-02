package com.dam.petme.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.petme.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {
    TextInputLayout emailLogInTextInputLayout, passwordLogInTextInputLayout;
    String email, password;
    TextView logInError;
    Button signInButton;
    TextView signUpTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailLogInTextInputLayout = findViewById(R.id.emailLogInTextInputLayout);
        passwordLogInTextInputLayout = findViewById(R.id.passwordLogInTextInputLayout);
        logInError = findViewById(R.id.logInError);
        signInButton = findViewById(R.id.signInButton);
        signUpTextView = findViewById(R.id.signUpTextView);

        mandatoryFieldValidation(emailLogInTextInputLayout);
        mandatoryFieldValidation(passwordLogInTextInputLayout);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailLogInTextInputLayout.getEditText().getText().toString();
                password = passwordLogInTextInputLayout.getEditText().getText().toString();

                if(validUser()) {
                    Toast.makeText(LogInActivity.this,"Ingresa",Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    public boolean validUser(){
        if(validateAllMandatoryFields()) {
            if(userExists()) {
                logInError.setVisibility(View.GONE);
                return true;
            }
            else {
                logInError.setVisibility(View.VISIBLE);
                return false;
            }
        }
        else {
            logInError.setVisibility(View.GONE);
            return false;
        }
    }

    public boolean userExists() {
        //TODO Verificar que el usuario se encuentre en la bdd
        return true;
    }

    public boolean validateAllMandatoryFields() {
        boolean allCompleted = true;

        if (email == null || email.isEmpty()) {
            emailLogInTextInputLayout.setHelperTextEnabled(false);
            emailLogInTextInputLayout.setError(getString(R.string.mandatoryField));
            allCompleted = false;
        }

        if (password == null || password.isEmpty()) {
            passwordLogInTextInputLayout.setHelperTextEnabled(false);
            passwordLogInTextInputLayout.setError(getString(R.string.mandatoryField));
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