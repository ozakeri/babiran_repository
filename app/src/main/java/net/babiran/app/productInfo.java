package net.babiran.app;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;

import com.google.gson.Gson;

import Adapters.ProductFeaAdapter;
import Models.Product;
import ui_elements.MyTextView;

/**
 * Created by Mohammad on 7/27/2017.
 */

public class productInfo extends AppCompatActivity {

    private Product product = null;
    private RecyclerView recycler_view;
    private MyTextView name, nofeature;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.product_info);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        if (product != null && product.getMo() != null) {

            if (product.getMo().size() > 0) {
                if (!product.name.equals("") && !product.name.equals("null") && product.name != null) {
                    name.setText(product.name);
                }

                System.out.println("size=======" + product.getMo().size());
                ProductFeaAdapter adp = new ProductFeaAdapter(productInfo.this, product.getMo());
                recycler_view.setAdapter(adp);
                //adp.notifyDataSetChanged();
            } else {
                if (!product.name.equals("") && !product.name.equals("null") && product.name != null) {
                    name.setText(product.name);
                }
                nofeature.setVisibility(View.VISIBLE);
            }

        }


    }
}
