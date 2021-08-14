package com.example.unilibre2021_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class ActOpciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_opciones);

        ((Button) findViewById(R.id.button12)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Para ir de una actividad a otra
                /* Intent Implicitos: Llamar desde mi app elementos de mi app*/
                /* Intent Explicitos: Llamar desde mi app elementos de otra app (Correo)(Llamada)(Alarma)*/

                Intent intent = new Intent(getApplicationContext(), ActRadioButton.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.button13)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActSeekBar.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.button14)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActListView.class);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.button15)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActRadioButton.class);
                String cad = ((EditText) findViewById(R.id.Nombres)).getText().toString();
                intent.putExtra("Nombres", cad);
                intent.putExtra("Estado",true);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.button16)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Normalmente soy usado para mensajes de alerta",Toast.LENGTH_LONG).show();
            }
        });

        ((Button) findViewById(R.id.button17)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Snackbar.make(v, "Soy mas eficiente que el toast",
                //        Snackbar.LENGTH_LONG,).setAction("Action",null).show();
                Snackbar.make(v,"Aca viene la pregunta", Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Estoy dentro del snackbar",Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });
    }
}