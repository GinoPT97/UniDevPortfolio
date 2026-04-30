package com.LSO.cinebox;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.LSO.cinebox.Infrastructure.ServerConnect;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private ServerConnect serverConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.confirm_password);
        Button registerButton = findViewById(R.id.register_button);
        Button togglePasswordVisibilityButton = findViewById(R.id.toggle_password_visibility);
        Button toggleConfirmPasswordVisibilityButton = findViewById(R.id.toggle_confirm_password_visibility);
        TextView backToLoginLink = findViewById(R.id.back_to_login_link);
        serverConnect = new ServerConnect();

        togglePasswordVisibilityButton.setOnClickListener(v -> togglePasswordVisibility(passwordField));
        toggleConfirmPasswordVisibilityButton.setOnClickListener(v -> togglePasswordVisibility(confirmPasswordField));

        registerButton.setOnClickListener(v -> performRegistration());
        backToLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
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
                        Log.d(TAG, "Registration successful, starting LoginActivity...");
                        Toast.makeText(getApplicationContext(), "Registrazione riuscita!", Toast.LENGTH_SHORT).show();

                        try {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Log.d(TAG, "Intent created: " + intent);
                            startActivity(intent);
                            Log.d(TAG, "LoginActivity started");
                            finish();
                        } catch (Exception e) {
                            Log.e(TAG, "Error starting LoginActivity: " + e.getMessage(), e);
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
