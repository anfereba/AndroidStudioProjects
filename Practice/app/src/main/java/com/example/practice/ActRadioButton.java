package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ActRadioButton extends AppCompatActivity {
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_radio_button);

        radioGroup = (RadioGroup) findViewById(R.id.rgPrimeraPregunta);


        //Recogemos los valores enviados
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //Asignamos valores a las variables

        if (bundle!=null){
            String nombresRecibidos = (String) bundle.get("Nombres");
            boolean estado = (Boolean) bundle.get("Estado");

            //Asignamos resultado
            String resultado = "Nombres = "+nombresRecibidos+" estado = "+estado;
            ((TextView) findViewById(R.id.txtNombresRecibidos)).setText(resultado);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.rbAlto:
                        Toast.makeText(getApplicationContext(),
                                "Alto", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbMedio:
                        Toast.makeText(getApplicationContext(),
                                "Medio", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbBajo:
                        Toast.makeText(getApplicationContext(),
                                "Bajo", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
    }

}