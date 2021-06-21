package com.example.testevoluationapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBOperations extends SQLiteOpenHelper {

    public DBOperations(@Nullable Context context) {
        super(context, "UserDatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableUser ="create table Users(fullname text,city text, email text, phone text, picture text)";
        db.execSQL(tableUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertData(String fullname,String city, String email, String phone, String picture)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("fullname",fullname);
        values.put("city",city);
        values.put("email",email);
        values.put("phone",phone);
        values.put("picture",picture);

        sqLiteDatabase.insert("Users",null,values);
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Users", null);
        return res;
    }

    public ArrayList fetchData()
    {
        ArrayList<String>stringArrayList=new ArrayList<String>();
        String fetchdata="select * from Users";
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(fetchdata, null);
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(0));
                stringArrayList.add(cursor.getString(1));
                stringArrayList.add(cursor.getString(2));
                stringArrayList.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
}








