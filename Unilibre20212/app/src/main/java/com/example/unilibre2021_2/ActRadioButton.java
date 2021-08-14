package com.example.unilibre2021_2;

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

        radioGroup = (RadioGroup) findViewById(R.id.primeraPregunta);

        // Recogemos los valores enviados

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            ((TextView) findViewById(R.id.textView7)).setText("Nombres = "
                    + bundle.get("Nombres") + " Estado = "+bundle.get("Estado"));
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton4:
                        Toast.makeText(getApplicationContext(), "Seleccionaste alto",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioButton5:
                        Toast.makeText(getApplicationContext(), "Seleccionaste medio",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioButton6:
                        Toast.makeText(getApplicationContext(), "Seleccionaste bajo",Toast.LENGTH_LONG).show();
                        break;
                }

            }
        });
    }
}