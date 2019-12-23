package Handlers;

/**
 * Created by Tohid on 5/5/2016 AD.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import Models.Advertising;

import static tools.AppConfig.code;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "login";

    // Login table name
    private static final String TABLE_LOGIN = "login";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOGIN + "("
                + "id" + " INTEGER PRIMARY KEY, "
                + "name" + " TEXT, "
                + "phone1" + " TEXT, "
                + "phone2" + " TEXT, "
                + "address" + " TEXT, "
                + "address2" + " TEXT, "
                + "address3" + " TEXT, "
                + "email" + " TEXT, "
                + "password" + " TEXT, "
                + "balance" + " INTEGER, "
                + "code" + " TEXT, "
                + "is_active" + " INTEGER, "
                + "is_deleted" + " INTEGER, "
                + "created_at_jalali" + " TEXT, "
                + "updated_at_jalali" + " TEXT, "
                + "created_at" + " TEXT, "
                + "updated_at" + " TEXT " + ");";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */

    public void update(String id,String name ,String email,String address,String address2,String address3,String phone1,String phone2){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        values.put("id", id);
        values.put("name", name);
        values.put("email", email);
        values.put("address", address);
        values.put("address2", address2);
        values.put("address3", address3);
        values.put("phone1", phone1);
        values.put("phone2", phone2);


        db.update(TABLE_LOGIN, values, "id" + " = ?", new String[] { String.valueOf(id)});

    }

    public void updateCode(String id,String code){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        values.put("code", code);

        db.update(TABLE_LOGIN, values, "id" + " = ?", new String[] { String.valueOf(id)});

    }


    public void addUser(String id,String name ,String email,String address,String address2,String address3,String phone1,int balance ,String code) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("email", email);
        values.put("address", address);
        values.put("address2", address2);
        values.put("address3", address3);
        values.put("phone1", phone1);
        values.put("balance", balance);
        values.put("code", code);

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        Log.e("DatabaseHandler : ","user added");
        db.close(); // Closing database connection
    }


    public void addUser(String id,String phone1,String code) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", "");
        values.put("email", "");
        values.put("address", "");
        values.put("address2", "");
        values.put("address3", "");
        values.put("phone1", phone1);
        values.put("balance", 0);
        values.put("code", code);

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        Log.e("DatabaseHandler : ","user added");
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String,String> getUserDetails()
    {
        HashMap<String,String> user = new HashMap();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("id", cursor.getString(0));
            user.put("name", cursor.getString(1));
            user.put("phone1", cursor.getString(2));
            user.put("phone2", cursor.getString(3));
            user.put("address", cursor.getString(4));
            user.put("address2", cursor.getString(5));
            user.put("address3", cursor.getString(6));
            user.put("email", cursor.getString(7));
            user.put("password", cursor.getString(8));
            user.put("balance", cursor.getString(9));
            user.put("code", cursor.getString(10));
            user.put("is_active", cursor.getString(11));
            user.put("is_deleted", cursor.getString(12));

        }
        cursor.close();
        db.close();
        return user;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re create database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

    public boolean ifExist(Advertising adv){
        String query = "SELECT  * FROM " + TABLE_LOGIN +" WHERE id = "+adv.id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        if(rowCount >0){
            return true;
        }
        else{
            return false;
        }


    }
}
