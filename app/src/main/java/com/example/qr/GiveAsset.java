package com.example.qr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiveAsset extends AppCompatActivity{
    AutoCompleteTextView listKafedra;
    TextView nameAss, currentKafedra;
    ArrayAdapter<String> adapter;
    AdapterView.OnItemSelectedListener itemSelectedListener;
    String chooseKafedra;
    Asset asset;
    Button giveBtn, returnBtn;
    boolean click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_asset);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("QR-инвентарь");
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1144AA")));

        asset = getIntent().getParcelableExtra("compNumber");   // получаем информацию о МА от прошлого окна
        if(asset.getStateN().equals("3"))
            Toast.makeText(getApplicationContext(), "Актив находится в процессе передачи!", Toast.LENGTH_SHORT).show();
        click = false;

        nameAss = (TextView) findViewById(R.id.nameInv);
        nameAss.setText(asset.getName());
        currentKafedra = (TextView) findViewById(R.id.currentKafedra);
        currentKafedra.setText(asset.getKafedra());

        listKafedra = (AutoCompleteTextView) findViewById(R.id.newNameKafedra);
        new ServerGetInfo("http://qrcodemosit.000webhostapp.com/kafedrasList.php").execute();// Получаем список кафедр
        // Buttons
        giveBtn=(Button)findViewById(R.id.giveBtn);
        giveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseKafedra=listKafedra.getText().toString();
                if(chooseKafedra.equals(asset.getKafedra()))
                    Toast.makeText(getApplicationContext(), "Выберите другую кафедру!", Toast.LENGTH_SHORT).show();
                else
                    new ServerGetInfo("http://qrcodemosit.000webhostapp.com/giveAsset.php",asset.getId()).execute();
            }
        });
    }

    class ServerGetInfo extends AsyncTask<String, String, String> {
        int answer;
        ServerMethods serverConnect;
        String server;
        Map<String, String> paramsToServer = new HashMap<String,String>();
        List<String> returnKafedra;

        ServerGetInfo(String server){
            this.server = server;
            paramsToServer.put("number", asset.getId());
            Log.d("params",paramsToServer.toString());
        }
        ServerGetInfo(String server, String idAsset){
            this(server);
            paramsToServer.put("kafedraOld",asset.getKafedra());
            paramsToServer.put("kafedraNew",chooseKafedra);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            serverConnect = new ServerMethods(server,paramsToServer);
            if(!click)
                returnKafedra = serverConnect.GetKafedrasList();
            else
                answer= serverConnect.GetAssetInfo();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!click){
                adapter = new ArrayAdapter<String>(GiveAsset.this, R.layout.support_simple_spinner_dropdown_item, returnKafedra);
                listKafedra.setAdapter(adapter);
                itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Получаем выбранный объект
                        chooseKafedra = (String)parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                listKafedra.setOnItemSelectedListener(itemSelectedListener);
                click = true;
            }
            else{
                if(answer==15){
                    Toast.makeText(getApplicationContext(), "Информация о передаче успешно внесена", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(answer==5){
                    Toast.makeText(getApplicationContext(), "Кафедра с данным названием не найдена", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Проблемы с отправкой данных!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

