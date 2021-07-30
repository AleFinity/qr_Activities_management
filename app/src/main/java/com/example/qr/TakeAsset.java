package com.example.qr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeAsset extends AppCompatActivity {
    Button returnBtn, takeBtn;
    String idAsset;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_asset);

        idAsset = getIntent().getParcelableExtra("compNumber");   // получаем информацию о МА от прошлого окна

        takeBtn=(Button)findViewById(R.id.emplBtn);
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ServerGetInfo("http://qrcodemosit.000webhostapp.com/takeAsset.php",idAsset).execute();
            }
        });
        returnBtn=(Button)findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class ServerGetInfo extends AsyncTask<String, String, String> {
        int answer;
        ServerMethods serverConnect;
        String server;
        boolean click;
        Map<String, String> paramsToServer=null;
        List<String> returnKafedra;

        ServerGetInfo(String server){
            this.server = server;
        }
        ServerGetInfo(String server, String idAsset){
            this(server);
            paramsToServer = new HashMap<String,String>();
            paramsToServer.put("number", idAsset);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            serverConnect = new ServerMethods(server,paramsToServer);
            answer= serverConnect.GetAssetInfo();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(answer==6){
                Toast.makeText(getApplicationContext(), "МА находится в распоряжении кафедры!", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "Проблемы с отправкой данных!", Toast.LENGTH_SHORT).show();
        }
    }
}
