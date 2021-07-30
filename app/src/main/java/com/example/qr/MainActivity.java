package com.example.qr;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    Button ScanBtn, loginBtn,giveBtn, employeeBtn,repairBtn,inventarizationBtn,replaceBtn;
    String activityNumber; // номер актива, полученный при сканировании
    AlertDialog dialog;
    IntentIntegrator integrator;
    Map<String,String> paramsToServer;
    boolean authorization;
    int btnChoise;
    String server;
    String log="",pas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); // << это вставить именно сюда
        setContentView(R.layout.activity_main);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("QR-инвентарь");
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1144AA")));

        authorization = false;
        paramsToServer = new HashMap<String,String>();

        //authorization=true; // !!!delete
        if (authorization==true)
            HideAuthorization();

        loginBtn=findViewById(R.id.loginBtn); // Объявили кнопку "Войти"
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChoise=0;
                log = ((EditText) findViewById(R.id.editTextTextEmailAddress)).getText().toString();
                pas = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();
                if(log!="" || pas!=""){
                    server="http://qrcodemosit.000webhostapp.com/authorization.php";
                    String value = log.replace('@', '_');
                    Log.d("mail",value);
                    paramsToServer.put("login", value);
                    paramsToServer.put("password",pas);
                    new ServerGetInfo().execute();
                }
            }
        });
        ScanBtn=findViewById(R.id.scanBtn); // Объявили кнопку "Сканировать"
        ScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChoise=1;
                server = "http://qrcodemosit.000webhostapp.com/idInventory.php";
                scanCode(); // выполнение сканирования
            }
        });
        giveBtn=findViewById(R.id.giveBtn); // Передать кафедре
        giveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChoise=2;
                server = "http://qrcodemosit.000webhostapp.com/idInventory.php";
                scanCode(); // выполнение сканирования
            }
        });
        employeeBtn =findViewById(R.id.emplBtn); // Выдать сотруднику
        employeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChoise=3;
                server = "http://qrcodemosit.000webhostapp.com/idInventory.php";
                scanCode(); // выполнение сканирования
            }
        });
        replaceBtn=findViewById(R.id.replaceBtn); // Переместить МА
        replaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChoise=4;
                server = "http://qrcodemosit.000webhostapp.com/idInventory.php";
                scanCode();
            }
        });
        repairBtn=findViewById(R.id.repairBtn); // Отремонтировать актив
        repairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChoise=5;
                server = "http://qrcodemosit.000webhostapp.com/idInventory.php";
                scanCode(); // выполнение сканирования
            }
        });
        inventarizationBtn=findViewById(R.id.inventarizationBtn); // Инвентаризация
        inventarizationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChoise=6;
                Intent intent = new Intent(MainActivity.this, Inventarization.class);
                startActivity(intent);
            }
        });
    }

    private void HideAuthorization(){
        findViewById(R.id.loginBtn).setVisibility(View.GONE);
        findViewById(R.id.login).setVisibility(View.GONE);
        findViewById(R.id.password).setVisibility(View.GONE);
        findViewById(R.id.editTextTextEmailAddress).setVisibility(View.GONE);
        findViewById(R.id.editTextTextPassword).setVisibility(View.GONE);
        //findViewById(R.id.enterBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.giveBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.emplBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.replaceBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.repairBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.inventarizationBtn).setVisibility(View.VISIBLE);
        //findViewById(R.id.outexplBtn).setVisibility(View.VISIBLE);
    }

    private void scanCode(){
        integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Просканируйте код");
        integrator.initiateScan();
    }
    ServerGetInfo err;
    AlertDialog.Builder builder;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){ //если qr-код содержит информацию
            activityNumber = result.getContents(); //Получаем информацию из qr-кода (номер)
            IsOnline connectionInternet = new IsOnline(); // подключение к интернету
            if (connectionInternet.isOnline(this)==true) { // если есть подключение к Интернету, обращаемся к серверу для получения более подробной информации

                //err=new ServerGetInfo();
                //err.execute(); // Асинхронный процесс для обращения к ресурсу интернета и получения информации

                // Настраиваем и выводим диалоговое окно для ожидания загрузки информации
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Ожидание ответа от сервера");
                builder.setMessage("Загрузка...");

                builder.setPositiveButton("Повторное сканирование", new DialogInterface.OnClickListener() { // Отсканировать заново
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        scanCode();
                    }
                }).setNegativeButton("Выйти", new DialogInterface.OnClickListener() { // Закрыть приложение
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //err.cancel(true);
                        finish();
                    }
                });
                new ServerGetInfo().execute();

                dialog = builder.create();
                dialog.show();

            } else { // подключение к интернету отсутствует
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private class ServerGetInfo extends AsyncTask<String, String, String> {
        boolean answerHTTP=false; // Ответ от сервера
        int answer=0;
        ServerMethods serverConnect;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if(btnChoise!=0){
                paramsToServer.put("number", activityNumber.toString());
                if(authorization && btnChoise!=1){
                    String value = log.replace('@', '_');
                    Log.d("mail",value);
                    paramsToServer.put("login", value);
                }
            }

            IsOnline connectionInternet = new IsOnline(); // подключение к интернету
            if (connectionInternet.isOnline(MainActivity.this)==true){
                serverConnect = new ServerMethods(server,paramsToServer);
                answer= serverConnect.GetAssetInfo();
            }
            paramsToServer.clear();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent intent;
            String value = log.replace('@', '_');
            Log.d("mail",value);
            if(answer>10)
                switch (btnChoise) {
                    case 0: // Авторизация
                        authorization = true;
                        HideAuthorization();
                        break;
                    case 1: // Получеине информации о МА
                        dialog.dismiss();
                        intent = new Intent(MainActivity.this, Information.class);
                        Log.d("inventory",serverConnect.answerHTTP.toString());
                        intent.putExtra("compNumber", serverConnect.inventory);
                        intent.putExtra("authorization", authorization);
                        intent.putExtra("login", value);
                        startActivity(intent);
                        break;
                    case 2: // Передать кафедре
                        dialog.dismiss();
                        Log.d("getStateN",serverConnect.inventory.getStateN());
                        if(     !serverConnect.inventory.getStateN().equals("0") &&
                                !serverConnect.inventory.getStateN().equals("4") &&
                                !serverConnect.inventory.getStateN().equals("5"))
                        {
                            intent = new Intent(MainActivity.this, GiveAsset.class);
                            intent.putExtra("compNumber", serverConnect.inventory);
                            intent.putExtra("login", value);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Актив нельзя передать другой кафедре", Toast.LENGTH_SHORT).show();
                        break;
                    case 3: // Выдать сотруднику
                        dialog.dismiss();
                        if(serverConnect.inventory.getStateN().equals("4"))
                            Toast.makeText(getApplicationContext(), "Актив находится в пользовании сотрудником", Toast.LENGTH_SHORT).show();
                        else if(!serverConnect.inventory.getStateN().equals("0") &&
                                !serverConnect.inventory.getStateN().equals("3") &&
                                !serverConnect.inventory.getStateN().equals("5"))
                        {
                            intent = new Intent(MainActivity.this, GiveEmployee.class);
                            intent.putExtra("compNumber", serverConnect.inventory);
                            intent.putExtra("login", value);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Актив нельзя выдать сотруднику", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        dialog.dismiss();
                        if(     serverConnect.inventory.getStateN().equals("1") ||
                                serverConnect.inventory.getStateN().equals("2"))
                        {
                            intent = new Intent(MainActivity.this, Replace.class);
                            intent.putExtra("compNumber", serverConnect.inventory);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Актив нельзя переместить", Toast.LENGTH_SHORT).show();
                        break;
                    case 5: // Отремонтировать актив
                        dialog.dismiss();
                        if(serverConnect.inventory.getProblem()!=null)
                        {
                            intent = new Intent(MainActivity.this, RepairAsset.class);
                            intent.putExtra("compNumber", serverConnect.inventory);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Для данного актива неисправности не обнаружены", Toast.LENGTH_SHORT).show();


                        break;
                    case 6: // Инвентаризация
                        dialog.dismiss();
                        intent = new Intent(MainActivity.this, Inventarization.class);
                        intent.putExtra("compNumber", serverConnect.inventory.getId());
                        intent.putExtra("login", value);
                        startActivity(intent);
                        break;
                }
            else{
                try{
                    Toast.makeText(getApplicationContext(), dialogMessage(), Toast.LENGTH_SHORT).show();
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
                catch (Exception dif){
                    //Toast.makeText(getApplicationContext(), "Неправильный ввод данных!", Toast.LENGTH_SHORT).show();
                }
            }
            }

        String dialogMessage(){
            switch(answer){
                case 0: return ("Отсутствует подключение к Интернету!");
                case 1: return ("Ошибка подключения к БД");
                case 2: return ("Отказано в доступе");
                case 3: return ("МА с данным QR не найден");
            }
            return "Ошибка. Обратитесь к администратору";
        }
    }
}


