package com.LSO.cinebox;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.LSO.cinebox.Infrastructure.ServerConnect;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameField, passwordField, confirmPasswordField;
    private Button registerButton, togglePasswordVisibilityButton, toggleConfirmPasswordVisibilityButton;
    private ServerConnect serverConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_button);
        togglePasswordVisibilityButton = findViewById(R.id.toggle_password_visibility);
        toggleConfirmPasswordVisibilityButton = findViewById(R.id.toggle_confirm_password_visibility);
        serverConnect = new ServerConnect();

        togglePasswordVisibilityButton.setOnClickListener(v -> togglePasswordVisibility(passwordField));
        toggleConfirmPasswordVisibilityButton.setOnClickListener(v -> togglePasswordVisibility(confirmPasswordField));

        registerButton.setOnClickListener(v -> performRegistration());
    }

    private void togglePasswordVisibility(EditText editText) {
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().length());
    }

    private void performRegistration() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Le password non coincidono!", Toast.LENGTH_SHORT).show();
            return;
        }

        serverConnect.sendMessage("REGISTER " + username + " " + password, new ServerConnect.MessageCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Errore di registrazione: risposta nulla dal server.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.equals("SUCCESS")) {
                        Log.d("RegisterActivity", "Registration successful, starting LoginActivity...");
                        Toast.makeText(getApplicationContext(), "Registrazione riuscita!", Toast.LENGTH_SHORT).show();

                        try {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Log.d("RegisterActivity", "Intent created: " + intent);
                            startActivity(intent);
                            Log.d("RegisterActivity", "LoginActivity started");
                            finish();
                        } catch (Exception e) {
                            Log.e("RegisterActivity", "Error starting LoginActivity: " + e.getMessage(), e);
                            Toast.makeText(getApplicationContext(), "Failed to start LoginActivity", Toast.LENGTH_SHORT).show();
                        }
                    } else if (response != null && response.contains("username already exists")) {
                        Toast.makeText(getApplicationContext(), "Username già registrato!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Errore di connessione: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
