package com.example.qr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Information extends AppCompatActivity {

    Button repBtn;
    Button scanAgain;

    Asset asset;
    StateAdapter adapter;
    RecyclerView recyclerView;
    boolean admin;

    String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("QR-инвентарь");
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1144AA")));


        asset = getIntent().getParcelableExtra("compNumber");   // получаем информацию о МА от прошлого окна
        admin = getIntent().getExtras().getBoolean("authorization");
        login = getIntent().getExtras().getString("login");


        TextView nameAsset = (TextView)findViewById(R.id.nameAsset);
        nameAsset.setText(asset.getName());
            // С помощью адаптера производим вывод характеристик в RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.list);
        adapter = new StateAdapter(this, asset.listCharacteristicCreator(admin));    // создаем адаптер
        recyclerView.setAdapter(adapter);   // устанавливаем для списка адаптер
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item, menu);
        MenuItem item;
        if(asset.getStateN().equals("0") || asset.getStateN().equals("4") || asset.getStateN().equals("5")){
            item = menu.findItem(R.id.item1);
            item.setVisible(false);
        }
        if(admin){
            if(asset.getStateN().equals("0") || asset.getStateN().equals("3") || asset.getStateN().equals("4")){
                item = menu.findItem(R.id.item2);
                item.setVisible(true);
            }
            if(asset.getStateN().equals("1") || asset.getStateN().equals("2")){
                item = menu.findItem(R.id.item3);
                item.setVisible(true);
            }
        }
        return true;
    }

    // actionBar back home
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.item1: // сообщить о поломке
                Intent intent = new Intent(Information.this, AskRepair.class);
                intent.putExtra("compNumber", asset);
                startActivity(intent);
                finish();
                return true;
            case R.id.item2: // ввести МА
                new Information.ServerGetInfo("http://qrcodemosit.000webhostapp.com/acceptAsset.php").execute();
                finish();
                return true;
            case R.id.item3: // вывести МА
                new Information.ServerGetInfo("http://qrcodemosit.000webhostapp.com/setOutExploit.php").execute();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ServerGetInfo extends AsyncTask<String, String, String> {
        int answer;
        ServerMethods serverConnect;
        String server;

        ServerGetInfo(String server){
            this.server = server;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> paramsToServer = new HashMap<String,String>();
            paramsToServer.put("number", asset.getId());
            paramsToServer.put("login", login);
            serverConnect = new ServerMethods(server,paramsToServer);
            answer= serverConnect.GetAssetInfo();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(answer==2){
                Toast.makeText(getApplicationContext(), "Актив не принадлежит вашей кафедре!", Toast.LENGTH_SHORT).show();
            }
            else if(answer==19){
                Toast.makeText(getApplicationContext(), "Актив помечен как \"Выведенный\"", Toast.LENGTH_SHORT).show();
                asset.setState("5");
                finish();
            }
            else if(answer==20){
                Toast.makeText(getApplicationContext(), "Актив закреплён за кафедрой", Toast.LENGTH_SHORT).show();
                asset.setState("1");
                finish();
            }
            else if(answer==21){
                Toast.makeText(getApplicationContext(), "Актив возвращен на кафедру!", Toast.LENGTH_SHORT).show();
                asset.setState("1");
                adapter = new StateAdapter(Information.this, asset.listCharacteristicCreator(admin));    // создаем адаптер
                recyclerView.setAdapter(adapter);
            }
            else
                Toast.makeText(getApplicationContext(), "Проблемы с отправкой данных!", Toast.LENGTH_SHORT).show();
        }
    }
}