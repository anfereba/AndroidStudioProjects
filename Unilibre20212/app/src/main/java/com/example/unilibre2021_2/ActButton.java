package com.example.unilibre2021_2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActButton extends AppCompatActivity implements View.OnClickListener {
    Button btnred, btngreen, btnblue;
    LinearLayout fondo;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_button);

        //Asignar control sobre XML
        btnred =(Button) findViewById(R.id.button);
        btngreen =(Button) findViewById(R.id.button2);
        btnblue =(Button) findViewById(R.id.button3);
        fondo = (LinearLayout) findViewById(R.id.fondo);
        textview = (TextView) findViewById(R.id.textView);

        //Asignar Eventos sobre la actividad
        btnred.setOnClickListener(this);
        btngreen.setOnClickListener(this);
        btnblue.setOnClickListener(this);

        //Metodo Dos
        //((Button) findViewById(R.id.button4)).setOnClickListener(this);

        //Metodo tres
        ((Button) findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fondo.setBackgroundColor(Color.YELLOW);
                ((TextView) findViewById(R.id.textView)).setText("Seleccionaste el color Amarillo");
            }
        });

        ((Button) findViewById(R.id.button6)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.textView)).setText("Hola "+
                        ((EditText) findViewById(R.id.editText)).getText());
            }
        });

    }

    @Override
    public void onClick(View view) {
        //View: Sobre que objeto dio click
        switch (view.getId()){
            case R.id.button:
                fondo.setBackgroundColor(Color.RED);
                textview.setText("Seleccionaste el color Rojo");
                break;
            case R.id.button2:
                fondo.setBackgroundColor(Color.GREEN);
                textview.setText("Seleccionaste el color Verde");
                break;
            case R.id.button3:
                fondo.setBackgroundColor(Color.BLUE);
                textview.setText("Seleccionaste el color Azul");
                break;
                /*
                METODO DOS
            case R.id.button4:
                fondo.setBackgroundColor(Color.YELLOW);
                textview.setText("Seleccionaste el color Amarillo");
                break;         */
            default:
                break;
        }

    }
    public void CambiarColor(View view){
        fondo.setBackgroundColor(Color.MAGENTA);
        ((TextView) findViewById(R.id.textView)).setText("Seleccionaste el color Magenta");
    }
}