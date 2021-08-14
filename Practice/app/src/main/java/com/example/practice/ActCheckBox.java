package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ActCheckBox extends AppCompatActivity {

    //Declaracion variables

    CheckBox chkRojo, chkVerde, chkAzul;
    Button btnEvaluar;
    TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_check_box);

        //Se recogen los elementos

        chkRojo = (CheckBox) findViewById(R.id.chkRojo);
        chkVerde = (CheckBox) findViewById(R.id.chkVerde);
        chkAzul = (CheckBox) findViewById(R.id.chkAzul);
        btnEvaluar = (Button) findViewById(R.id.btnEvaluar);
        tvResultado = (TextView) findViewById(R.id.tvResultado);

        //Asignamos evento

        btnEvaluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer result = new StringBuffer();
                result.append(" Rojo = ").append(chkRojo.isChecked());
                result.append(" Verde = ").append(chkVerde.isChecked());
                result.append(" Azul = ").append(chkAzul.isChecked());

                tvResultado.setText(result.toString());
            }
        });
    }
}