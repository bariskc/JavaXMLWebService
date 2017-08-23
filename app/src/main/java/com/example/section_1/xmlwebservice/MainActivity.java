package com.example.section_1.xmlwebservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.section_1.xmlwebservice.async.DovizServiceAsyncTask;
import com.example.section_1.xmlwebservice.entities.Doviz;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String xmlUrl = "http://www.tcmb.gov.tr/kurlar/today.xml";
    ListView listView;
    ArrayList<Doviz> dovizler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        try {
            DovizServiceAsyncTask task = new DovizServiceAsyncTask(this, listView);
            task.execute(xmlUrl);
            dovizler = task.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Doviz seciliDoviz = dovizler.get(position);
                Toast.makeText(MainActivity.this, seciliDoviz.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DovizActivity.class);
                //intent.putExtra("Deneme", "1");
                intent.putExtra("Doviz", seciliDoviz);
                startActivity(intent);
            }
        });

    }
}
