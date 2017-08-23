package com.example.section_1.xmlwebservice.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.section_1.xmlwebservice.entities.Doviz;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Section_1 on 23.8.2017.
 */

public class DovizServiceAsyncTask extends AsyncTask<String, String, ArrayList<Doviz>> {

    private Context context;
    private ListView listView;
    private ProgressDialog progressDialog;

    public DovizServiceAsyncTask(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = progressDialog.show(context, "Lütfen bekleyiniz", "işlem yürütülüyor.", true, true, new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressDialog.setMessage("Veri okuma işlemi iptal edildi.");
            }
        });
    }

    @Override
    protected ArrayList<Doviz> doInBackground(String... strings){
        ArrayList<Doviz> dovizler = new ArrayList<>();
        HttpURLConnection baglanti = null;
        try {
            publishProgress("Döviz kurları okunuyor");
            URL url = new URL(strings[0]);
            baglanti = (HttpURLConnection) url.openConnection();
            if(baglanti.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedInputStream stream = new BufferedInputStream(baglanti.getInputStream());
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document document = documentBuilder.parse(stream);
                NodeList dovizNodeList = document.getElementsByTagName("Currency");
                for (int i =0; i<dovizNodeList.getLength()-1; i++) {
                    publishProgress("Liste güncelleniyor.");
                    Element element = (Element) dovizNodeList.item(i);
                    NodeList nlbirim = element.getElementsByTagName("Unit");
                    NodeList nlisim = element.getElementsByTagName("Isim");
                    NodeList nlalis = element.getElementsByTagName("ForexBuying");
                    NodeList nlsatis = element.getElementsByTagName("ForexSelling");

                    Doviz doviz = new Doviz();
                    doviz.setIsim(nlisim.item(0).getFirstChild().getNodeValue());
                    doviz.setBirim(nlbirim.item(0).getFirstChild().getNodeValue());
                    doviz.setAlis(Double.valueOf(nlalis.item(0).getFirstChild().getNodeValue()));
                    doviz.setSatis(Double.valueOf(nlsatis.item(0).getFirstChild().getNodeValue()));

                    dovizler.add(doviz);
                }
            }else {
                publishProgress("İnternet bağlantısı bulunamadı");
            }
            return dovizler;
        }catch (Exception ex) {
            Toast.makeText(context, "XML okuma hatası", Toast.LENGTH_SHORT).show();
            return null;
        }finally {
            if(baglanti != null){
                baglanti.disconnect();
            }
        }

    }

    @Override
    protected void onProgressUpdate(String... values) {
        progressDialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Doviz> dovizs) {
        Toast.makeText(context, "Doviz okuma işlemi tamamlandı.", Toast.LENGTH_LONG).show();
        if(dovizs!=null && dovizs.size()!=0) {
            ArrayAdapter<Doviz> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, dovizs);
            listView.setAdapter(adapter);
            progressDialog.cancel();
        }

    }

    @Override
    protected void onCancelled(ArrayList<Doviz> dovizs) {
        super.onCancelled(dovizs);
    }
}
