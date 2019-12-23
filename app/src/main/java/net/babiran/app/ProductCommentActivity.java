package net.babiran.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import Adapters.ProductFeaAdapter;
import Models.Product;
import ui_elements.MyTextView;

/**
 * Created by Mohammad on 7/27/2017.
 */

public class ProductCommentActivity extends AppCompatActivity {

    Product product = null;
    ListView Feautures;
    MyTextView name, nofeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activit_product_comment);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.btnBack.setVisibility(View.GONE);
                MainActivity.viewLogo.setVisibility(View.VISIBLE);
                finish();
            }
        });
        name = (MyTextView) findViewById(R.id.pro_name_fea_txt);
        nofeature = (MyTextView) findViewById(R.id.nofeature);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("product") != null) {
                Gson gson = new Gson();
                Product obj = gson.fromJson(extras.getString("product"), Product.class);
                Log.e("prolist", obj + "");
                if (obj != null) {
                    product = obj;
                }
            }
        }

        Feautures = (ListView) findViewById(R.id.Features);
        if (product != null) {
            if (!product.name.equals("") && !product.name.equals("null") && product.name != null) {
                name.setText(product.name);
            }
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.btnBack.setVisibility(View.GONE);
        MainActivity.viewLogo.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }
}
