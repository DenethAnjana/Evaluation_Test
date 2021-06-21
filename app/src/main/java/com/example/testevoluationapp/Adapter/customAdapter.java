package com.example.testevoluationapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.testevoluationapp.MainActivity;
import com.example.testevoluationapp.R;
import com.example.testevoluationapp.model.testModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class customAdapter extends BaseAdapter {

    Context context;
    ArrayList<testModel> arrayList;

    public customAdapter(Context context, ArrayList<testModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView ==  null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.test_list, parent, false);
        }

        TextView fullName, city, email, phone, profileImage;

        fullName = convertView.findViewById(R.id.fullname);
        city = (TextView) convertView.findViewById(R.id.city);
        email = (TextView) convertView.findViewById(R.id.email);
        phone = (TextView) convertView.findViewById(R.id.phone);
        profileImage = (TextView) convertView.findViewById(R.id.profileImage);

        fullName.setText("Full Name : " +arrayList.get(position).getFullname());
        city.setText("City            : " + arrayList.get(position).getCity());
        email.setText("Email         : " + arrayList.get(position).getEmail());
        phone.setText("Phone Number : " + arrayList.get(position).getPhone());
        profileImage.setText("Profile Image Link : " + arrayList.get(position).getProfileImage());

        profileImage.setVisibility(View.INVISIBLE);

        return convertView;
    }


}
