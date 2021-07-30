package com.example.qr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Replace extends AppCompatActivity{
    AutoCompleteTextView listRooms;
    TextView nameAss,currentRoom;
    ArrayAdapter<String> adapter;
    AdapterView.OnItemSelectedListener itemSelectedListener;
    String chooseRoom;
    Asset asset;
    Button giveBtn;
    boolean click;
    short state; // 1-складирован, 2-актив

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replace);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("QR-инвентарь");
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1144AA")));

        asset = getIntent().getParcelableExtra("compNumber");   // получаем информацию о МА от прошлого окна
        click = false;
        state = 2;

        RadioButton redRadioButton = findViewById(R.id.activRB);
        redRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton greenRadioButton = (RadioButton)findViewById(R.id.sleepRB);
        greenRadioButton.setOnClickListener(radioButtonClickListener);

        nameAss = (TextView) findViewById(R.id.nameInv);
        nameAss.setText(asset.getName());

        currentRoom = (TextView) findViewById(R.id.currentRoom);
        currentRoom.setText(asset.getNumberClass());

        listRooms = (AutoCompleteTextView) findViewById(R.id.newNameKafedra);
        new ServerGetInfo("http://qrcodemosit.000webhostapp.com/roomsList.php").execute();// Получаем список кафедр
        // Buttons
        giveBtn=(Button)findViewById(R.id.giveBtn);
        giveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRoom =listRooms.getText().toString();
                new ServerGetInfo("http://qrcodemosit.000webhostapp.com/replace.php",asset.getId()).execute();
            }
        });
    }

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()) {
                case R.id.activRB:
                    state=2;
                    break;
                case R.id.sleepRB:
                    state=1;
                    break;
                default:
                    break;
            }
        }
    };

    class ServerGetInfo extends AsyncTask<String, String, String> {
        int answer;
        ServerMethods serverConnect;
        String server;
        Map<String, String> paramsToServer = new HashMap<String,String>();
        List<String> returnKafedra;

        ServerGetInfo(String server){
            this.server = server;
        }
        ServerGetInfo(String server, String idAsset){
            this(server);
            paramsToServer.put("number", asset.getId());
            paramsToServer.put("newRoom",chooseRoom);
            paramsToServer.put("state",String.valueOf(state));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            serverConnect = new ServerMethods(server,paramsToServer);
            if(!click)
                returnKafedra = serverConnect.GetRoomsList();
            else
                answer= serverConnect.GetAssetInfo();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!click){
                adapter = new ArrayAdapter<String>(Replace.this, R.layout.support_simple_spinner_dropdown_item, returnKafedra);
                listRooms.setAdapter(adapter);
                itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Получаем выбранный объект
                        chooseRoom = (String)parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                listRooms.setOnItemSelectedListener(itemSelectedListener);
                click = true;
            }
            else{
                if(answer==22){
                    Toast.makeText(getApplicationContext(), "Информация о перемещении внесена", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(answer==6){
                    Toast.makeText(getApplicationContext(), "Кабинет с данным номером не найден", Toast.LENGTH_SHORT).show();
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

