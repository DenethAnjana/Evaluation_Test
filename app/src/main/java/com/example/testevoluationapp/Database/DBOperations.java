package com.example.testevoluationapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.testevoluationapp.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.testevoluationapp.Database.UserContract.Entry.CITY;
import static com.example.testevoluationapp.Database.UserContract.Entry.EMAIL;
import static com.example.testevoluationapp.Database.UserContract.Entry.FULLNAME;
import static com.example.testevoluationapp.Database.UserContract.Entry.PHONE;
import static com.example.testevoluationapp.Database.UserContract.Entry.TABLE_NAME;

public class DBOperations extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "user_info.db";
    private static final String CREATE_QUERY = "create table "+
            TABLE_NAME+
            "("+UserContract.Entry.FULLNAME+ " text,"+
            UserContract.Entry.CITY+ " text,"+
            UserContract.Entry.EMAIL+ " text,"+
            UserContract.Entry.PHONE+ " text);";

    public DBOperations(Context context) {
        super(context, DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Database is Created");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String name, String city, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FULLNAME,FULLNAME);
        contentValues.put(CITY,CITY);
        contentValues.put(EMAIL,EMAIL);
        contentValues.put(PHONE,PHONE);
        long newRowId = db.insert(TABLE_NAME,null, contentValues);
        db.close();
    }

    public ArrayList<HashMap<String, String>> GetUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT FULLNAME, CITY, EMAIL,PHONE  FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();

            user.put("FULLNAME",cursor.getString(cursor.getColumnIndex(FULLNAME)));
            user.put("CITY",cursor.getString(cursor.getColumnIndex(CITY)));
            user.put("EMAIL",cursor.getString(cursor.getColumnIndex(EMAIL)));
            user.put("PHONE",cursor.getString(cursor.getColumnIndex(PHONE)));

            userList.add(user);
        }
        return  userList;
    }
}








