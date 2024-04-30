package com.affc.ait.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "JIT";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create the tables

        final String sqlForStudent = "CREATE TABLE Student (" +
                "    Student_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    name TEXT,          " +
                "    email TEXT,         " +
                "    login_code TEXT,         " +
                "    address TEXT,        " +
                "    city TEXT,        " +
                "    phone TEXT,          " +
                "    gender TEXT,             " +
                "    DOB TEXT                    " +
                ");";
        final String sqlForAdmin = "CREATE TABLE Admin (" +
                "admin_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "admin_email TEXT," +
                "admin_password TEXT" +
                ");";
        final String sqlForBranch = "CREATE TABLE Branch (" +
                "branch_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "branch_name TEXT," +
                "location TEXT" +
                ");";

        final String sqlForCourse = "CREATE TABLE Course (" +
                "     course_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    course_name TEXT,          " +
                "    start_date TEXT,         " +
                "    end_date TEXT,        " +
                "    fee REAL,        " +
                "    description TEXT,          " +
                "    max_p INTEGER             " +
                ");";
        sqLiteDatabase.execSQL(sqlForStudent);
        sqLiteDatabase.execSQL(sqlForBranch);
        sqLiteDatabase.execSQL(sqlForCourse);
        sqLiteDatabase.execSQL(sqlForAdmin);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE Student");
        sqLiteDatabase.execSQL("DROP TABLE Admin");
        sqLiteDatabase.execSQL("DROP TABLE Branch");
        sqLiteDatabase.execSQL("DROP TABLE Course");
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    /*
    list of methods needed to work with the db:
    add user
    add profile picture
    verify user email
    generate and update login code

    fetch all courses (limit to 10)
    query for courses by name
    fetch single course info
    fetch branches for given course

    verify admin
    add new course
    add new branch
    fetch all users (limit to 10)
    query for users by name
     */


}
