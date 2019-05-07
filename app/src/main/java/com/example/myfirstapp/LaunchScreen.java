package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LaunchScreen extends AppCompatActivity {

    Button elec, power;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch);

        elec = findViewById(R.id.bElec);
        power = findViewById(R.id.bPower);

        elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LaunchScreen.this, StartScreen.class);
                i.putExtra("mode",0);
                startActivity(i);
            }
        });

        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LaunchScreen.this, StartScreen.class);
                i.putExtra("mode",1);
                startActivity(i);
            }
        });

    }
}
