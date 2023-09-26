package com.example.provaapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    int numerocasuale = 0;
    TextView tv;
    EditText ed;
    Button b;
    Button backButton;
    Button rigenera;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        gioco();

        Back();
    }

    public void Back() {
        backButton = findViewById(R.id.ReturnButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numerocasuale = 0;
                finish(); // Torna all'activity precedente (MainActivity)
            }
        });
    }

    public void gioco() {
        numerocasuale = GeneraNumeroCasuale();
        tv = findViewById(R.id.ResultTV);
        ed = findViewById(R.id.InsertNumber);
        b = findViewById(R.id.InsertButton);
        b.setOnClickListener(this::onClick);
        Rigenera();
    }

    public void Rigenera() {
        rigenera = findViewById(R.id.RigeneraButton);
        rigenera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numerocasuale = GeneraNumeroCasuale();
            }
        });
    }

    int GeneraNumeroCasuale() {
        Random r = new Random();
        numerocasuale = r.nextInt(100) + 0;
        return numerocasuale;
    }

    @Override
    public void onClick(View view) {
        int numero = Integer.parseInt(ed.getText().toString());
        if (numero == numerocasuale) tv.setText(numero + " Il numero è esatto.");
        else if (numero < numerocasuale) tv.setText(numero + " Il numero è troppo basso");
        else if (numero > numerocasuale) tv.setText(numero + " Il numero è troppo alto");
    }
}
