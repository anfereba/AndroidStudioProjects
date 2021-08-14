package com.example.unilibre2021_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ActCheckBox extends AppCompatActivity {
    CheckBox chkRojo, chkVerde, chkAzul;
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_check_box);

        chkRojo = (CheckBox) findViewById(R.id.checkBox4);
        chkVerde = (CheckBox) findViewById(R.id.checkBox5);
        chkAzul = (CheckBox) findViewById(R.id.checkBox6);
        button = (Button) findViewById(R.id.button7);
        textView = (TextView) findViewById(R.id.textView3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();
                result.append("Rojo = ").append(chkRojo.isChecked()).append("\n");
                result.append("Verde = ").append(chkVerde.isChecked()).append("\n");
                result.append("Azul = ").append(chkAzul.isChecked()).append("\n");
                textView.setText(result);
            }
        });
    }
}