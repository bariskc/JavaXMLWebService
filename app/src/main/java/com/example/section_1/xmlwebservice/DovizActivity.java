package com.example.section_1.xmlwebservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.section_1.xmlwebservice.entities.Doviz;

/**
 * Created by Section_1 on 23.8.2017.
 */

public class DovizActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doviz);
        textView = (TextView) findViewById(R.id.detay);
        Intent intent = getIntent();
        //String mesaj = intent.getStringExtra("Deneme");
        Doviz gelenDoviz = (Doviz) intent.getSerializableExtra("doviz");
        //textView.setText(mesaj);
    }
}
