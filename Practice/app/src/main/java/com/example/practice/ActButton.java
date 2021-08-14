package com.example.practice;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActButton extends AppCompatActivity implements View.OnClickListener {

        // Declaracion de Botones y LinearLayout Fondo
        Button btnRed, btnGreen, btnBlue;
        LinearLayout lnFondo;
        TextView txtvRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_button);

        //Obtenemos id de botones y LinearLayout

        btnRed = (Button) findViewById(R.id.btnRojo);
        btnGreen = (Button) findViewById(R.id.btnVerde);
        btnBlue = (Button) findViewById(R.id.btnAzul);
        lnFondo = (LinearLayout) findViewById(R.id.fondo);
        txtvRespuesta = (TextView) findViewById(R.id.txtvRespuesta);

        ((Button) findViewById(R.id.btnMetodoDos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnFondo.setBackgroundColor(Color.MAGENTA);
                ((TextView) findViewById(R.id.txtvRespuesta)).setText("Seleccionaste Magenta");
            }
        });

        ((Button)findViewById(R.id.btnsaludar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) findViewById(R.id.txtvRespuesta)).setText("Hola " +
                        ((EditText) findViewById(R.id.txtNombres)).getText()
                );
            }
        });

        //Asignamos propiedad (Implementar OnClickrListener En la clase)

        btnRed.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAzul:
                lnFondo.setBackgroundColor(Color.BLUE);
                txtvRespuesta.setText("Seleccionaste Azul");
                break;
            case R.id.btnRojo:
                lnFondo.setBackgroundColor(Color.RED);
                txtvRespuesta.setText("Seleccionaste Rojo");
                break;
            case R.id.btnVerde:
                lnFondo.setBackgroundColor(Color.GREEN);
                txtvRespuesta.setText("Seleccionaste Verde");
                break;
        }
    }
    public void cambiarColor(View view){
        ((LinearLayout) findViewById(R.id.fondo)).setBackgroundColor(Color.YELLOW);
        ((TextView)findViewById(R.id.txtvRespuesta)).setText("Seleccionaste Amarillo");
    }
}