package com.example.qr;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class AskRepair extends AppCompatActivity {
    EditText problem;
    TextView nameAsset;
    Button askProblem, returnBtn;
    Asset asset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_repair);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("QR-инвентарь");
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1144AA")));

        asset = getIntent().getParcelableExtra("compNumber");
        nameAsset=(TextView)findViewById(R.id.nameAsset);
        nameAsset.setText(asset.getName());

        problem=(EditText)findViewById(R.id.infoProblem);
        askProblem=(Button)findViewById(R.id.askProblemBtn);
        askProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(problem.getText().length()>10)
                    new AskRepair.ServerGetInfo("http://qrcodemosit.000webhostapp.com/askRepair.php").execute();
                else
                    Toast.makeText(getApplicationContext(), "Текст неисправности должен содержать 10 и более символов", Toast.LENGTH_SHORT).show();
            }
        });
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
            paramsToServer.put("problem", problem.getText().toString());
            serverConnect = new ServerMethods(server,paramsToServer);
            answer= serverConnect.GetAssetInfo();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(answer==14){
                Toast.makeText(getApplicationContext(), "Информация внесена", Toast.LENGTH_SHORT).show();
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
                //finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
