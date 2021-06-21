package com.example.testevoluationapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.ContextMenu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity{

    ListView lv_data;
    Button data;
    WebView dp;
    TextView link;
    ArrayList<testModel> arrayList;
    SwipeRefreshLayout swipeRefresh;

    DBOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_data = findViewById(R.id.lv_data);
        data = findViewById(R.id.btnData);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        dp = findViewById(R.id.dp);
        link = findViewById(R.id.profileImage);

        dbOperations = new DBOperations(this);
        dbOperations.getWritableDatabase();

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
            arrayList.clear();
            String result = null;

            try {
                URL url = new URL("https://randomuser.me/api/?inc=name,location,email,phone,picture");
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

           // System.out.println("Test" + result);

            return result;

        }

        @Override
        public void onPostExecute(String s) {
            super .onPostExecute(s);
            swipeRefresh.setRefreshing(false);

            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.optJSONArray("results");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsonObject = array.optJSONObject(i);

                    JSONObject jtitle = array.getJSONObject(i);
                    JSONObject title = jtitle.getJSONObject("name");
                    String utitle = title.optString("title");

                    JSONObject jfirst = array.getJSONObject(i);
                    JSONObject first = jtitle.getJSONObject("name");
                    String ufirst = first.optString("first");

                    JSONObject jolast = array.getJSONObject(i);
                    JSONObject last = jtitle.getJSONObject("name");
                    String ulast = last.optString("last");

                    JSONObject jcity = array.getJSONObject(i);
                    JSONObject location = jcity.getJSONObject("location");
                    String uCity= location.optString("city");

                    String uEmail = jsonObject.optString("email");
                    String uPhone = jsonObject.optString("phone");


                    JSONObject jimg = array.getJSONObject(i);
                    JSONObject img = jcity.getJSONObject("picture");
                    String uPicture= img.optString("large");

                    String uFullname = utitle +". " + ufirst+ " " + ulast;


                    testModel model = new testModel();

                    model.setFullname(uFullname);
                    model.setCity(uCity);
                    model.setEmail(uEmail);
                    model.setPhone(uPhone);
                    model.setProfileImage(uPicture);


                    arrayList.add(model);

                    dbOperations.insertData(uFullname, uCity, uEmail, uPhone,uPicture);
                    Toast.makeText(MainActivity.this,"Data Inserted To Sqlite Database",Toast.LENGTH_LONG).show();


                    System.out.println("FullName :  " + uFullname);
                    System.out.println("City :  " + uCity);
                    System.out.println("Email :  " + uEmail);
                    System.out.println("Phone  :  " + uPhone);
                    System.out.println("Image  :  " + uPicture);

                    String strUrl = uPicture.toString();

                    if(strUrl.length()>0)
                        dp.loadUrl(strUrl);
                    else
                        Toast.makeText(MainActivity.this,"No dp",Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            customAdapter adapter = new customAdapter(MainActivity.this, arrayList);
            lv_data.setAdapter(adapter);


            data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cursor res = dbOperations.getAllData();
                    if(res.getCount() == 0){
                        showMessage("Error", "Nothing found");
                        return;
                    }
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()){
                        buffer.append("Full Name : "+res.getString(0)+"\n");
                        buffer.append("City            : "+res.getString(1)+"\n");
                        buffer.append("Email         : "+res.getString(2)+"\n");
                        buffer.append("Phone        : "+res.getString(3)+"\n");
                        buffer.append("Picture       : "+res.getString(4)+"\n\n\n");


                    }
                    showMessage("Users Table" , buffer.toString());
                }
            });
        }
    }

    private void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setIcon(R.drawable.usr);
        builder.show();
    }


}