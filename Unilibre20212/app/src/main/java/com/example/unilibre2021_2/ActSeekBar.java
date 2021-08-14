package com.example.unilibre2021_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class ActSeekBar extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    SeekBar seekBar;
    TextView start, stop, actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_seek_bar);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        start = (TextView) findViewById(R.id.textView8);
        stop = (TextView) findViewById(R.id.textView9);
        actual = (TextView) findViewById(R.id.textView10);

        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        actual.setText("Valor actual: " + progress);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        start.setText("Inicio: "+seekBar.getProgress());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        stop.setText(" Detuvo: "+seekBar.getProgress());
    }
}