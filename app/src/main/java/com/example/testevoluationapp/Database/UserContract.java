package com.example.testevoluationapp.Database;

public class UserContract {

    UserContract(){}

    public static  abstract class Entry{

        public static final String FULLNAME = "fullname";
        public static final String CITY = "city";
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";
        public static final String TABLE_NAME = "UserInfo";

    }
}
