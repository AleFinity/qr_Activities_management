package com.example.qr;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.HashMap;
import java.util.List;

public class ServerGetInfo extends AsyncTask<String, String, String> {
    int answer;
    ServerMethods serverConnect;
    String server;
    boolean click;
    String idAsset;
    HashMap<String, String> paramsToServer=null;
    List<String> returnKafedra;

    ServerGetInfo(String server){
        this.server = server;
    }

    ServerGetInfo(String server, String idAsset, int returnMeaning){
        this(server);
        this.idAsset = idAsset;
        paramsToServer = new HashMap<String,String>();
        paramsToServer.put("number", idAsset);
    }

    ServerGetInfo(String server, boolean click, List<String> returnKafedra){
        this(server);
        this.click = click;
        this.returnKafedra=returnKafedra;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        serverConnect = new ServerMethods(server,paramsToServer);
        if(!click){
            returnKafedra = serverConnect.GetKafedrasList();
            Log.i("list", returnKafedra.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (click)
            ;
        click = false;
    }
}