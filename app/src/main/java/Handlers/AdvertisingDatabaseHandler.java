package Handlers;

/**
 * Created by Tohid on 5/5/2016 AD.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import Models.Advertising;
import Models.Product;

import static tools.AppConfig.advertising;
import static tools.AppConfig.product;

public class AdvertisingDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "fav_pros";

    // Login table name
    private static final String TABLE_LOGIN = "products";



    public AdvertisingDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOGIN + "("
                + "id" + " INTEGER PRIMARY KEY,"
                + "name" + " TEXT,"
                + "description" + " TEXT,"
                + "price" + " TEXT ,"
                + "stock" + " TEXT,"
                + "tags" + " TEXT,"
                + "sells_count" + " TEXT,"
                + "seen_count" + " TEXT,"
                + "rate" + " INTEGER,"
                + "is_special" + " TEXT,"
                + "expiration_date" + " TEXT,"
                + ");";
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
    public void addRow(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", product.id); //
        values.put("name", product.name);
        values.put("description", product.description);
        values.put("price", product.price); //  At
        values.put("stock", product.stock); //  At
        values.put("rate", product.rate); //  At

        //  At

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        Log.e("DatabaseHandler : "," added");
        db.close(); // Closing database connection
        Log.e("database","deleted"+ product.name);
        Log.e("database Rows",getRowCount()+"");

    }

    /**
     * Getting user data from database
     * */

    public ArrayList<Product> getProducts(){
        ArrayList<Product> productArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // Move to first row


        try
        {
            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
/*
                    Product product = new Product(c.getString(0),c.getString(1),c.getString(2),c.getString(3),
                            c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),
                            c.getString(9),c.getString(10),c.getString(11),c.getString(12),c.getString(13),
                            c.getString(14),c.getString(15),c.getString(16),c.getString(17),
                            c.getString(18),c.getString(19),c.getString(20),c.getString(21),
                            c.getString(22),c.getString(23),c.getString(24),c.getString(25),
                            c.getString(26),c.getString(27),c.getString(28),c.getString(29),
                            c.getString(30),c.getString(31),imagesArray,featuresArray);*/

                    productArrayList.add(product);


                }while (c.moveToNext());
            }
        }
        catch (SQLiteException e)
        {
            Log.d("SQL Error", e.getMessage());
            return null;
        }
        finally
        {
            //release all your resources
            c.close();
            db.close();
        }


        return productArrayList;
    }
    public HashMap getUserDetails(){
        HashMap user = new HashMap();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){


            user.put("id_customer", cursor.getString(0));
            user.put("gender", cursor.getString(1));
            user.put("fname", cursor.getString(2));
            user.put("lname", cursor.getString(3));
            user.put("email", cursor.getString(4));
            user.put("password", cursor.getString(5));
            user.put("birthdate", cursor.getString(6));
            user.put("mobile", cursor.getString(7));
            user.put("phone", cursor.getString(8));
            user.put("state", cursor.getString(9));
            user.put("city", cursor.getString(10));
            user.put("address", cursor.getString(11));
            user.put("alias", cursor.getString(12));
            user.put("optin", cursor.getString(13));
            user.put("optin", cursor.getString(14));
            user.put("newsletter", cursor.getString(15));
        }
        cursor.close();
        db.close();
        // return user
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

    public boolean ifExist(Product pro){
        String query = "SELECT  * FROM " + TABLE_LOGIN +" WHERE id = "+pro.id;
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

    public void delete(Product pro){
        if(ifExist(pro)){
            SQLiteDatabase db = this.getReadableDatabase();
            db.delete(TABLE_LOGIN,"id=?",new String[]{pro.id});


        }
    }

}
