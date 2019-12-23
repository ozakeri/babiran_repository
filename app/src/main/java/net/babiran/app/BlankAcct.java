package net.babiran.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import Handlers.DatabaseHandler;
import tools.AppConfig;

public class BlankAcct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("co.nkit.babiran", MODE_PRIVATE);
        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        if (db.getRowCount() > 0)
        {

            HashMap user = db.getUserDetails();
            AppConfig.id = user.get("id").toString();
            AppConfig.phone = user.get("phone1").toString();

            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
            finish();

        } else {
            if (prefs.getBoolean("firstrun", true)) {
                startActivity(new Intent(BlankAcct.this, Sms_Register.class));
                finish();
            } else {
                startActivity(new Intent(BlankAcct.this, MainActivity.class));
                finish();
            }
        }
    }
}
