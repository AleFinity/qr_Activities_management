package com.example.qr;

import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServerMethods{

    String answerHTTP; // Полученный от сервера ответ
    String server; // Сервер, к которому производится запрос
    public Asset inventory; // Объект инвентарь
    HashMap<String, String> postDataParams;

    public ServerMethods(String server, Map<String,String> params){
        this.server=server;
        if(params!=null){
            postDataParams = new HashMap<String, String>();
            postDataParams.putAll(params);
        }
    }

    protected int GetAssetInfo() {
        // Запрос к серверу
        answerHTTP = performGetCall(server, postDataParams).toString();
        if (answerHTTP==null || answerHTTP=="")
            return -1;
        switch (answerHTTP){
            case "0": return 0; // нет подключения к интернету
            case "1": return 1; // ошибка подключения к БД
            case "2": return 2; // отказано в доступе (ошибка поиска пользователя в БД)
            case "3": return 3; // МА с данным номером не найден
            case "4": return 4; // Сотрудник с данными ФИО не найден
            case "5": return 5;   //Кафедра с данным названием не найдена
            case "6": return 6;   //Кабинет с данным номером не найден

            case "12": return 12; // пользователь (ответственное лицо) найден
            case "14": return 14; // поломка зарегестрирована
            case "15": return 15; // МА в процессе передачи на кафедру
            case "16": return 16; // МА в пользовании сотрудника
            case "17": return 17; // информация о поломке передана
            case "18": return 18; // список МА составлен
            case "19": return 19; // МА отмечен как "Выведенный"
            case "20": return 20; // МА принят на кафедру
            case "21": return 21; // МА возвращен на кафедру
            case "22": return 22; // МА перемещен
            case "23": return 23; // МА отремонтирован

            default:
                Log.d("json",answerHTTP.toString());
                inventory = new Gson().fromJson(answerHTTP, Asset.class);

                return 100;   // Получены данные об инвентаре
        }
    }

    protected List<String> GetKafedrasList(){
        answerHTTP = performGetCall(server, postDataParams).toString();
        Log.d("json",answerHTTP);
        return (new Gson().fromJson(answerHTTP, List.class));
    }

    protected List<String> GetRoomsList(){
        answerHTTP = performGetCall(server, null).toString();
        return (new Gson().fromJson(answerHTTP, List.class));
    }

    LinkedHashMap<String,String> assets;
    protected int GetAssetsList(){
        answerHTTP = performGetCall(server, postDataParams).toString();
        switch (answerHTTP){
            case "1": return 1;
            case "6": return 6;
            case "7": return 7;
            default:
                assets = new Gson().fromJson(answerHTTP, LinkedHashMap.class);
                return 18;
        }
    }

    public String performGetCall(String requestURL,
                                 HashMap<String, String> getDataParams) {
        String response = "";

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            if(getDataParams != null)
                url = new URL(requestURL + "?" + getDataString(getDataParams));
            else
                url = new URL(requestURL);

            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                response = convertInputStreamToString(urlConnection.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return response;
    }

    // Функция перевода HashMap в строку нужного вида.
    private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private String convertInputStreamToString(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}