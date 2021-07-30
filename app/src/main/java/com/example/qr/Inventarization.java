package com.example.qr;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Inventarization extends AppCompatActivity {

    Button showAssetsBtn,scanBtn;
    AutoCompleteTextView listRooms;
    TextView chooseRoomText;
    ArrayAdapter<String> adapterRoom;
    StateAdapter adapter;
    RecyclerView recyclerView;

    AdapterView.OnItemSelectedListener itemSelectedListener;

    LinkedHashMap<String,String> returnAssets;
    String chosenRoom;
    boolean click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventarization);
        click=false;

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("QR-инвентарь");
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1144AA")));

        listRooms = (AutoCompleteTextView) findViewById(R.id.roomNumber);
        new Inventarization.ServerGetInfo("http://qrcodemosit.000webhostapp.com/roomsList.php").execute();// Получаем список кабинетов

        chooseRoomText = (TextView)findViewById(R.id.chooseRoomText);

        // С помощью адаптера производим вывод МА в RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.list);

        // Buttons
        // show assets belong to one room btn
        showAssetsBtn=(Button)findViewById(R.id.showAssetsBtn);
        showAssetsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Inventarization.ServerGetInfo("http://qrcodemosit.000webhostapp.com/assetsInRoom.php",listRooms.getText().toString()).execute();
            }
        });
        // scanning btn
        scanBtn=(Button)findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });
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

    IntentIntegrator integrator;
    private void scanCode(){
        integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Просканируйте код");
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String activityNumber = result.getContents();
        if(returnAssets.containsKey(activityNumber)){
            returnAssets.remove(activityNumber);
            adapter = new StateAdapter(Inventarization.this, returnAssets);    // создаем адаптер
            recyclerView.setAdapter(adapter);
        }
        else
            Toast.makeText(getApplicationContext(), "МА не из этого кабинета", Toast.LENGTH_SHORT).show();
    }

    class ServerGetInfo extends AsyncTask<String, String, String> {
        int answer;
        ServerMethods serverConnect;
        String server;
        Map<String, String> paramsToServer=null;
        List<String> returnRooms;

        ServerGetInfo(String server){
            this.server = server;
        }
        ServerGetInfo(String server, String room){
            this(server);
            paramsToServer = new HashMap<String,String>();
            paramsToServer.put("room", room);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            serverConnect = new ServerMethods(server,paramsToServer);
            if(!click)
                returnRooms = serverConnect.GetKafedrasList();
            else{
                answer=serverConnect.GetAssetsList();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!click) {
                adapterRoom = new ArrayAdapter<String>(Inventarization.this, R.layout.support_simple_spinner_dropdown_item, returnRooms);
                listRooms.setAdapter(adapterRoom);
                itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Получаем выбранный объект
                        chosenRoom = (String) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                listRooms.setOnItemSelectedListener(itemSelectedListener);
                click = true;
            } else {
                if(answer==18){
                    returnAssets = serverConnect.assets;
                    adapter = new StateAdapter(Inventarization.this, returnAssets);    // создаем адаптер
                    recyclerView.setAdapter(adapter);   // устанавливаем для списка адаптер
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chooseRoomText.setText(listRooms.getText());
                            findViewById(R.id.activity_main).setVisibility(View.VISIBLE);
                            chooseRoomText.setText(listRooms.getText());
                            findViewById(R.id.showAssetsBtn).setVisibility(View.GONE);
                            findViewById(R.id.roomNumber).setVisibility(View.GONE);
                            scanBtn.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else if(answer==6)
                    Toast.makeText(getApplicationContext(), "Кабинет с данным номером не найден", Toast.LENGTH_SHORT).show();
                else if(answer==7)
                    Toast.makeText(getApplicationContext(), "В данном кабинете нет МА", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getApplicationContext(), "Проблемы с отправкой данных!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
