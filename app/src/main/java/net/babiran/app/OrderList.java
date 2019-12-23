package net.babiran.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import net.babiran.app.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import Adapters.OrderListAdapter;
import Models.Product;

/**
 * Created by Mohammad on 7/15/2017.
 */

public class OrderList extends AppCompatActivity {

    ListView orderList;
    Toolbar toolbar;
    OrderListAdapter orderListAdapter;

    ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        orderList = (ListView) findViewById(R.id.order_listView);

        toolbar = (Toolbar) findViewById(R.id.toolbarOrder);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Gson gson = new Gson();
            String json = extras.getString("prolist");
            ArrayList<Product> obj = gson.fromJson(json, new TypeToken<List<Product>>() {
            }.getType());
            Log.e("objGson", obj + "");

            if (obj != null) {
                products = obj;
                orderListAdapter = new OrderListAdapter(OrderList.this, products);
                orderList.setAdapter(orderListAdapter);
                orderListAdapter.notifyDataSetChanged();

            }
        }
    }


}
