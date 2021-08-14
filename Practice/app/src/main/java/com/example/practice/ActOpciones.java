package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class ActOpciones extends AppCompatActivity implements View.OnClickListener {
    //Declaramos variables

    Button btnRadioButton, btnSeekBar, btnListView, btnToast, btnSnackBar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_opciones);
        //Recuperamos botones

        btnRadioButton = ((Button) findViewById(R.id.btnActRadioButton));
        btnSeekBar = ((Button) findViewById(R.id.btnActSeekBar));
        btnListView = ((Button) findViewById(R.id.btnActListView));
        btnToast = (Button) findViewById(R.id.btnToast);
        btnSnackBar = (Button) findViewById(R.id.btnSnackBar);

        //Asignamos eventos y metodos

        btnRadioButton.setOnClickListener(this);
        btnSeekBar.setOnClickListener(this);
        btnListView.setOnClickListener(this);
        btnToast.setOnClickListener(this);
        btnSnackBar.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        //Intent para llamar a actividad

        switch (view.getId()){
            case R.id.btnActRadioButton:
                intent = new Intent(this, ActRadioButton.class);

                //Obtenemos texto
                String nombres = ((EditText) findViewById(R.id.txt_nombres)).getText().toString();

                //Enviar valores a la otra actividad (Nombre y valor)
                intent.putExtra("Nombres",nombres);
                intent.putExtra("Estado",true);
                startActivity(intent);

                break;
            case R.id.btnActSeekBar:
                intent = new Intent(this, ActSeekBar.class);
                startActivity(intent);
                break;
            case R.id.btnActListView:
                intent = new Intent(this, ActListView.class);
                startActivity(intent);
                break;
            case R.id.btnToast:
                Toast.makeText(this, "Normalmente soy usado para mensajes de alerta", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnSnackBar:

                /*
                Snackbar.make(view, "Soy mas eficiente que el Toast",Snackbar.LENGTH_SHORT)
                        .setAction("Action",null).show(); */

                Snackbar.make(view,"Aca Viene la pregunta",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(),
                                        "Estoy dentro del snackbar", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                break;

        }
    }
}