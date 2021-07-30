package com.example.qr;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepairAsset extends AppCompatActivity {
    TextView problem;
    TextView nameAsset;
    Button repairBtn;
    Asset asset;

    String number;

    boolean click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_asset);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("QR-инвентарь");
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1144AA")));

        asset = getIntent().getParcelableExtra("compNumber");
        number=asset.getId();
        nameAsset=(TextView)findViewById(R.id.nameAsset);
        nameAsset.setText(asset.getName());

        click=false;

        problem=(TextView)findViewById(R.id.repairText);
        problem.setText(asset.getProblem());

        repairBtn =(Button)findViewById(R.id.repairBtn);
        repairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RepairAsset.ServerGetInfo("http://qrcodemosit.000webhostapp.com/repair.php").execute();
            }
        });
    }
    private class ServerGetInfo extends AsyncTask<String, String, String> {
        int answer;
        ServerMethods serverConnect;
        String server;
        Map<String, String> paramsToServer=null;
        int returnProblems;

        ServerGetInfo(String server){
            this.server = server;
            paramsToServer = new HashMap<String,String>();
            paramsToServer.put("number", number);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            serverConnect = new ServerMethods(server,paramsToServer);
            returnProblems = serverConnect.GetAssetInfo();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(returnProblems==23){
                Toast.makeText(getApplicationContext(), "Информация о ремонте внесена", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "Проблемы с отправкой данных!", Toast.LENGTH_SHORT).show();
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
