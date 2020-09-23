package net.babiran.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Fragments.BasketListFragment;
import Fragments.ShajeFrgment;
import Handlers.DatabaseHandler;
import Models.Product;
import tools.AppConfig;

public class AfterOrderActivity extends AppCompatActivity {
    private DatabaseHandler db;
    private String key = null;
    private String server = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_order);

        TextView txt_one = findViewById(R.id.txt_one);
        TextView txt_two = findViewById(R.id.txt_two);
        TextView txt_return = findViewById(R.id.txt_return);

        txt_one.setTypeface(Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));
        txt_two.setTypeface(Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));
        txt_return.setTypeface(Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));


        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri != null){
                server = uri.getAuthority();
            }
        }

        txt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (server != null) {
                    if (server.equals("credit")) {
                        startActivity(new Intent(AfterOrderActivity.this, CreditActivity.class));
                    }

                    if (server.equals("sharj")) {
                        startActivity(new Intent(AfterOrderActivity.this, MainActivity.class));

                    }

                    if (server.equals("pay")) {
                        getFactorId();
                    }
                }
                finish();
            }
        });
    }

    public void getFactorId() {
        //NullBasket();
        db = new DatabaseHandler(this);
        if (db.getRowCount() > 0) {
            HashMap<String, String> userDetailsHashMap = db.getUserDetails();
            String id = userDetailsHashMap.get("id");
            Intent intent1 = new Intent(this, FactorList.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("id", id);
            intent1.putExtra("page", "AfterOrderActivity");
            startActivity(intent1);
        }
    }

    public void NullBasket() {

        SharedPreferences pro_prefs;
        pro_prefs = getSharedPreferences("productsArray", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pro_prefs.getString("products", "");
        ArrayList<Product> arrayList = gson.fromJson(json, new TypeToken<List<Product>>() {
        }.getType());
        SharedPreferences.Editor editor = getSharedPreferences("productsArray", MODE_PRIVATE).edit();
        arrayList.removeAll(arrayList);
        AppConfig.products = arrayList;

        try {
            Gson gson1 = new Gson();
            String proObj = gson1.toJson(AppConfig.products);
            editor.putString("products", proObj);
            editor.commit();
            AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
