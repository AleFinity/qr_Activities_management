package com.example.qr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiveEmployee extends AppCompatActivity{

    AutoCompleteTextView listEmployee;
    TextView nameAss, currentKafedra;
    ArrayAdapter<String> adapter;
    AdapterView.OnItemSelectedListener itemSelectedListener;
    String chooseEmployee;
    Asset asset;
    Button giveBtn, returnBtn;
    boolean click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_employee);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("QR-инвентарь");
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1144AA")));

        asset = getIntent().getParcelableExtra("compNumber");   // получаем информацию о МА от прошлого окна
        click = false;

        nameAss = (TextView) findViewById(R.id.nameInv);
        nameAss.setText(asset.getName());
        currentKafedra = (TextView) findViewById(R.id.currentKafedra);
        currentKafedra.setText(asset.getKafedra());

        listEmployee = (AutoCompleteTextView) findViewById(R.id.fio);
        new GiveEmployee.ServerGetInfo("http://qrcodemosit.000webhostapp.com/employeeList.php").execute();// Получаем список кафедр
        // Buttons
        giveBtn=(Button)findViewById(R.id.giveBtn);
        giveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseEmployee = listEmployee.getText().toString();
                new GiveEmployee.ServerGetInfo("http://qrcodemosit.000webhostapp.com/giveEmplAsset.php",asset.getId()).execute();
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

    class ServerGetInfo extends AsyncTask<String, String, String> {
        int answer;
        ServerMethods serverConnect;
        String server;
        Map<String, String> paramsToServer=null;
        List<String> returnEmployee;

        ServerGetInfo(String server){
            this.server = server;
            paramsToServer = new HashMap<String,String>();
            paramsToServer.put("number", asset.getId());
        }
        ServerGetInfo(String server, String idAsset){
            this(server);
            paramsToServer.put("fio", chooseEmployee);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            serverConnect = new ServerMethods(server,paramsToServer);
            if(!click)
                returnEmployee = serverConnect.GetKafedrasList();
            else
                answer= serverConnect.GetAssetInfo();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!click){
                adapter = new ArrayAdapter<String>(GiveEmployee.this, R.layout.support_simple_spinner_dropdown_item, returnEmployee);
                listEmployee.setAdapter(adapter);
                itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Получаем выбранный объект
                        chooseEmployee = (String)parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                listEmployee.setOnItemSelectedListener(itemSelectedListener);
                click = true;
            }
            else{
                if(answer==16){
                    Toast.makeText(getApplicationContext(), "МА закреплён за сотрудником", Toast.LENGTH_SHORT).show();
                    GiveEmployee.this.finish();
                }
                else if(answer==4){
                    Toast.makeText(getApplicationContext(), "Сотрудник с данными ФИО не найден", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Проблемы с отправкой данных!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
