package com.example.testevoluationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testevoluationapp.MainActivity;
import com.example.testevoluationapp.R;
import com.example.testevoluationapp.model.testModel;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView ==  null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.test_list, parent, false);
        }

        ImageView profileImage;
        TextView fullName, city, email, phone;

        profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
        fullName = (TextView) convertView.findViewById(R.id.fullname);
        city = (TextView) convertView.findViewById(R.id.city);
        email = (TextView) convertView.findViewById(R.id.email);
        phone = (TextView) convertView.findViewById(R.id.phone);


       // profileImage.setImageResource(arrayList.get(position).getProfileImage());
        fullName.setText("Full Name : " +arrayList.get(position).getFullname());
        city.setText("City : " + arrayList.get(position).getCity());
        email.setText("Email : " + arrayList.get(position).getEmail());
        phone.setText("Phone Number : " + arrayList.get(position).getPhone());

        return convertView;
    }
}
