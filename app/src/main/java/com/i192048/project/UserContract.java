package com.i192048.project;

import android.provider.BaseColumns;

public class UserContract {

    public static String DB_NAME = "Project.db";
    public static int DB_VERSION = 1;

    public static class Project implements BaseColumns{
        public static String TABLE_NAME = "Users";
        public static String _ID = "_id";
        public static String _NAME = "_fullname";
        public static String _USERNAME = "_username";
        public static String _PHONE = "_phone";
        public static String _ADDRESS = "_address";
        public static String _EMAIL = "_email";
        public static String _PASSWORD = "_password";
    }

}
