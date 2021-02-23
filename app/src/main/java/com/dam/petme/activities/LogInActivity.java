package com.dam.petme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dam.petme.R;
import com.dam.petme.model.PetStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {
    TextInputLayout emailLogInTextInputLayout, passwordLogInTextInputLayout;
    String email, password;
    TextView logInError;
    Button signInButton;
    TextView signUpTextView;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

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

                if (validUser()) {
                    Toast.makeText(LogInActivity.this, "Ingresa", Toast.LENGTH_SHORT).show();

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

    public boolean validUser() {
        if (validateAllMandatoryFields()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("FB/Auth", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                                //startActivity(intent);
                                Intent intent = new Intent(LogInActivity.this, AddPetActivity.class);
                                intent.putExtra("status", PetStatus.FOUND);
                                //TODO cambiar from a la actividad llamante
                                intent.putExtra("from","LogInActivity");
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("FB/Auth", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            return true;
        } else {
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