package com.example.testevoluationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.testevoluationapp.Adapter.customAdapter;
import com.example.testevoluationapp.Database.DBOperations;
import com.example.testevoluationapp.model.testModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    ListView lv_data, lv_user;
    ArrayList<testModel> arrayList;
    SwipeRefreshLayout swipeRefresh;

    DBOperations dbOperations;
    private Object DBOperations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_data = findViewById(R.id.lv_data);
        swipeRefresh = findViewById(R.id.swipeRefresh);


        arrayList = new ArrayList<>();
        new fetchData().execute();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new fetchData().execute();
            }
        });

    }

    public class fetchData extends AsyncTask<String, String, String> {

        @Override
        public void onPreExecute() {
            super .onPreExecute();
            swipeRefresh.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            //arrayList.clear();
            String result = null;

            try {
                URL url = new URL("https://randomuser.me/api/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                }else  {
                    result = "error";
                }

            } catch (Exception  e) {
                e.printStackTrace();
            }

            System.out.println("Test" + result);

            return result;

        }

        @Override
        public void onPostExecute(String s) {
            super .onPostExecute(s);
            swipeRefresh.setRefreshing(false);
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("results");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonObject = array.getJSONObject(i);


                    String uName = jsonObject.optString("name");
                    String uCity = jsonObject.optString("city");
                    String uEmail = jsonObject.optString("email");
                    String uPhone = jsonObject.optString("phone");

                    testModel model = new testModel();

                    model.setFullname(uName);
                    model.setCity(uCity);
                    model.setEmail(uEmail);
                    model.setPhone(uPhone);
                    arrayList.add(model);


                    System.out.println("FullName :  " + uName);
                    System.out.println("City :  " + uCity);
                    System.out.println("Email :  " + uEmail);
                    System.out.println("Phone  :  " + uPhone);
                    DBOperations dbHelper = new DBOperations(MainActivity.this);

                    dbHelper.insertData(uName, uCity, uEmail, uPhone);
                    Toast.makeText(getApplicationContext(), "Details Saved Successfully", Toast.LENGTH_SHORT).show();


                    DBOperations = new DBOperations(MainActivity.this);

                    ArrayList<HashMap<String, String>> userList = dbOperations.GetUsers();

                    @SuppressLint("ResourceType") ListAdapter adapter = new SimpleAdapter(MainActivity.this,userList, R.id.lv_data ,new String[]{"FULLNAME","CITY","EMAIL","PHONE"},
                            new int[]{R.id.lname, R.id.lcity, R.id.lemail, R.id.lphone});
                    lv_data.setAdapter(adapter);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            customAdapter adapter = new customAdapter(MainActivity.this, arrayList);
            //lv_data.setAdapter(adapter);

        }

    }



}