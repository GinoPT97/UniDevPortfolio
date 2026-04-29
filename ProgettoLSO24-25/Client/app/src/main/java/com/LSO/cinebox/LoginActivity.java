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

public class LoginActivity extends AppCompatActivity {
    private EditText usernameField, passwordField;
    private Button loginButton, togglePasswordVisibilityButton;
    private TextView registerLink;
    private ServerConnect serverConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        togglePasswordVisibilityButton = findViewById(R.id.toggle_password_visibility);
        registerLink = findViewById(R.id.register_link);
        serverConnect = new ServerConnect();

        // Inizializza la connessione persistente
        initializePersistentConnection();

        togglePasswordVisibilityButton.setOnClickListener(v -> togglePasswordVisibility(passwordField));

        loginButton.setOnClickListener(v -> performLogin());
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void initializePersistentConnection() {
        new Thread(() -> {
            serverConnect.startPersistentConnection(new ServerConnect.ConnectionCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Connesso al server", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Errore connessione: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            });
        }).start();
    }

    private void togglePasswordVisibility(EditText editText) {
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().length());
    }

    private void performLogin() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        serverConnect.sendMessage("LOGIN " + username + " " + password, new ServerConnect.MessageCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    if (response.startsWith("SUCCESS")) {
                        Log.d("LoginActivity", "Login successful, starting MainActivity...");
                        String[] parts = response.split(" ");
                        int userId = Integer.parseInt(parts[1]);

                        try {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("userId", userId);
                            // Passa la connessione persistente alla MainActivity
                            MainActivity.setPersistentServerConnect(serverConnect);
                            Log.d("LoginActivity", "Intent created: " + intent);
                            startActivity(intent);
                            Log.d("LoginActivity", "MainActivity started");
                            finish();
                        } catch (Exception e) {
                            Log.e("LoginActivity", "Error starting MainActivity: " + e.getMessage(), e);
                            Toast.makeText(getApplicationContext(), "Failed to start MainActivity", Toast.LENGTH_SHORT).show();
                        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Solo chiudi la connessione se non è stata passata alla MainActivity
        if (MainActivity.getSharedServerConnect() == null && serverConnect != null) {
            new Thread(() -> {
                serverConnect.stopPersistentConnection();
            }).start();
        }
    }
}