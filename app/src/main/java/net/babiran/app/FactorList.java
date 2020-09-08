package net.babiran.app;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Adapters.FactorListAdapter;
import Handlers.DatabaseHandler;
import Models.EventbusModel;
import Models.Factor;
import Models.Feature;
import Models.Image;
import Models.Product;
import tools.AppConfig;
import ui_elements.MyTextView;

/**
 * Created by Mohammad on 7/15/2017.
 */

public class FactorList extends AppCompatActivity {

    private ListView factorList;
    private FactorListAdapter factorListAdapter;
    private Toolbar toolbar;
    private String id = "-1";
    private String pageName = null;
    private ArrayList<Product> productArrayList;
    private ArrayList<Factor> factorArrayList;
    private String What = AppConfig.NULLBASKET;
    private DatabaseHandler db;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factor_list);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(1);

        /*Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {

            db = new DatabaseHandler(getApplicationContext());
            if (db.getRowCount() > 0) {
                HashMap<String, String> userDetailsHashMap = db.getUserDetails();
                id = userDetailsHashMap.get("id");
            }

            Uri uri = intent.getData();
            System.out.println("===uri===" + uri);
            if (uri != null) {
                String success = uri.getQueryParameter("success");
                showGuideDialog(success);
                System.out.println("===success===" + success);
            }
        }*/


        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pageName != null && pageName.equals("AfterOrderActivity")) {
                    AppConfig.NULLBASKET = "";
                    Intent intent = new Intent(getApplicationContext(), BlankAcct.class);
                    startActivity(intent);
                    finish();
                    return;
                }


                if (TextUtils.isEmpty(What)) {
                    finish();
                } else {
                    AppConfig.NULLBASKET = "";
                    Intent intent = new Intent(getApplicationContext(), BlankAcct.class);

                    startActivity(intent);
                    finish();
                }

            }
        });
        factorList = (ListView) findViewById(R.id.factor_listView);

        //    toolbar = (Toolbar) findViewById(R.id.toolbarFactor);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            id = extras.getString("id");
            pageName = extras.getString("page");
            System.out.println("pageName====" + pageName);
            //  id="55";
            getFactor();
            getCreditRequest();
        }


        factorList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (factorList.getLastVisiblePosition() - factorList.getHeaderViewsCount() -
                        factorList.getFooterViewsCount()) >= (factorListAdapter.getCount() - 1)) {
                    getFactorLazy();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }


    public void getFactor() {

        RequestQueue queue = Volley.newRequestQueue(FactorList.this);

        final String url = AppConfig.BASE_URL + "api/factor/getAUserFactorsLazy/" + id + "/10/0";
        System.out.println("url=====" + url);
        // final String url = AppConfig.BASE_URL + "api/factor/getAUserFactorsLazy/" + id + "/10/0";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            System.out.println("response=====" + response);

                            factorArrayList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    JSONArray orderArray = jsonObject.getJSONArray("orders");

                                    Object status_name = jsonObject.get("status_name");
                                    Object A1 = jsonObject.get("carrierName");
                                    Object A2 = jsonObject.get("carrierImage");
                                    Object A3 = jsonObject.get("carrierMobile");

                                    Object A4 = jsonObject.get("track_code");
                                    Object A5 = jsonObject.get("ref_id");

                                    String A11 = A1.toString();
                                    String A22 = A2.toString();
                                    String A33 = A3.toString();

                                    String A44 = A4.toString();
                                    String A55 = A5.toString();

                                    if (TextUtils.isEmpty(A11)) {
                                        A11 = "null";
                                    }
                                    if (TextUtils.isEmpty(A22)) {
                                        A22 = "null";
                                    }
                                    if (TextUtils.isEmpty(A33)) {
                                        A33 = "null";
                                    }

                                    if (TextUtils.isEmpty(A44)) {
                                        A44 = "null";
                                    }
                                    if (TextUtils.isEmpty(A55)) {
                                        A55 = "null";
                                    }

                                    String Full = A11 + "##" + A22 + "##" + A33 + "##" + A44 + "##" + A55;

                                    //  String Full=A11+"##"+A22+"##"+A33;

                                    Log.e("SASASSAS", status_name.toString());

                                    Log.e("Full", Full);
                                    ArrayList<Feature> featuresArray;
                                    ArrayList<Image> imagesArray;
                                    productArrayList = new ArrayList<>();


                                    Log.e("orderAArray", orderArray.length() + "");
                                    for (int p = 0; p < orderArray.length(); p++) {
                                        featuresArray = new ArrayList<>();
                                        imagesArray = new ArrayList<>();
                                        try {
                                            JSONObject c = orderArray.getJSONObject(p);

                                            Log.e("orderObj", c.length() + "");
                                            JSONArray features = c.getJSONArray("features");
                                            for (int fea = 0; fea < features.length(); fea++) {
                                                try {
                                                    JSONObject f = features.getJSONObject(fea);
                                                    Feature feature = new Feature(
                                                            f.getString("value"), f.getString("name"));
                                                    featuresArray.add(fea, feature);
                                                } catch (JSONException ex) {

                                                }
                                            }

                                            JSONArray images = c.getJSONArray("images");
                                            ;
                                            for (int img = 0; img < images.length(); img++) {

                                                try {
                                                    JSONObject im = images.getJSONObject(img);
                                                    Image image = new Image(
                                                            im.getString("image_link"));
                                                    imagesArray.add(img, image);
                                                } catch (JSONException ex) {

                                                }
                                            }

                                            Product product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                                    c.getString("price"), c.getString("stock"), c.getString("count"), c.getString("discount_price"), imagesArray, featuresArray);
                                            productArrayList.add(product);

                                        } catch (JSONException e) {

                                            AppConfig.error(e);

                                        }

                                    }

                                    if (productArrayList != null) {

                                        Factor factor = new Factor(Full, status_name.toString(), jsonObject.getString("id"), jsonObject.getString("price_under_discount"), jsonObject.getString("price"),
                                                jsonObject.getString("paymentName"), jsonObject.getString("created_at_jalali"), productArrayList, jsonObject.getString("address"), jsonObject.getString("pay_again"));

                                        factorArrayList.add(factor);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (Exception e) {

                            AppConfig.error(e);
                        }

                        if (factorArrayList != null) {
                            factorListAdapter = new FactorListAdapter(FactorList.this, factorArrayList);
                            factorList.setAdapter(factorListAdapter);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppConfig.error(error);
                    }
                }
        );
        queue.add(getRequest);

    }

    public void getFactorLazy() {

        RequestQueue queue = Volley.newRequestQueue(FactorList.this);
        final String url = AppConfig.BASE_URL + "api/factor/getAUserFactorsLazy/" + id + "/10/" + factorArrayList.size();
        //final String url = AppConfig.BASE_URL + "api/factor/getAUserFactorsLazy/" + id + "/10/" + factorArrayList.size();

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    JSONArray orderArray = jsonObject.getJSONArray("orders");

                                    ArrayList<Feature> featuresArray;
                                    ArrayList<Image> imagesArray;
                                    productArrayList = new ArrayList<>();

                                    Object status_name = jsonObject.get("status_name");
                                    Object A1 = jsonObject.get("carrierName");
                                    Object A2 = jsonObject.get("carrierImage");
                                    Object A3 = jsonObject.get("carrierMobile");

                                    Object A4 = jsonObject.get("track_code");
                                    Object A5 = jsonObject.get("ref_id");

                                    String A11 = A1.toString();
                                    String A22 = A2.toString();
                                    String A33 = A3.toString();

                                    String A44 = A4.toString();
                                    String A55 = A5.toString();

                                    if (TextUtils.isEmpty(A11)) {
                                        A11 = "null";
                                    }
                                    if (TextUtils.isEmpty(A22)) {
                                        A22 = "null";
                                    }
                                    if (TextUtils.isEmpty(A33)) {
                                        A33 = "null";
                                    }

                                    if (TextUtils.isEmpty(A44)) {
                                        A44 = "null";
                                    }
                                    if (TextUtils.isEmpty(A55)) {
                                        A55 = "null";
                                    }

                                    String Full = A11 + "##" + A22 + "##" + A33 + "##" + A44 + "##" + A55;


                                    // String Full=A11+"##"+A22+"##"+A33;

                                    Log.e("orderAArray", orderArray.length() + "");
                                    for (int p = 0; p < orderArray.length(); p++) {
                                        featuresArray = new ArrayList<>();
                                        imagesArray = new ArrayList<>();
                                        try {
                                            JSONObject c = orderArray.getJSONObject(p);

                                            Log.e("orderObj", c.length() + "");
                                            JSONArray features = c.getJSONArray("features");
                                            for (int fea = 0; fea < features.length(); fea++) {
                                                try {
                                                    JSONObject f = features.getJSONObject(fea);
                                                    Feature feature = new Feature(
                                                            f.getString("value"), f.getString("name"));
                                                    featuresArray.add(fea, feature);
                                                } catch (JSONException ex) {

                                                }
                                            }

                                            JSONArray images = c.getJSONArray("images");
                                            ;
                                            for (int img = 0; img < images.length(); img++) {

                                                try {
                                                    JSONObject im = images.getJSONObject(img);
                                                    Image image = new Image(
                                                            im.getString("image_link"));
                                                    imagesArray.add(img, image);
                                                } catch (JSONException ex) {

                                                }
                                            }

                                            Product product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                                    c.getString("price"), c.getString("stock"), c.getString("count"), c.getString("discount_price"), imagesArray, featuresArray);
                                            productArrayList.add(product);

                                        } catch (JSONException e) {

                                            AppConfig.error(e);

                                        }

                                    }

                                    if (productArrayList != null) {

                                        Factor factor = new Factor(Full, status_name.toString(), jsonObject.getString("id"), jsonObject.getString("price_under_discount"), jsonObject.getString("price"),
                                                jsonObject.getString("paymentName"), jsonObject.getString("created_at_jalali"), productArrayList, jsonObject.getString("address"), jsonObject.getString("pay_again"));

                                        factorArrayList.add(factor);
                                        factorListAdapter.notifyDataSetChanged();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (Exception e) {

                            AppConfig.error(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppConfig.error(error);
                    }
                }
        );
        queue.add(getRequest);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("pageName====" + pageName);
        if (pageName != null && pageName.equals("AfterOrderActivity")) {
            AppConfig.NULLBASKET = "";
            Intent intent = new Intent(getApplicationContext(), BlankAcct.class);
            startActivity(intent);
            finish();
            return;
        }

        if (TextUtils.isEmpty(What)) {
            finish();
        } else {
            AppConfig.NULLBASKET = "";
            Intent intent = new Intent(getApplicationContext(), BlankAcct.class);
            startActivity(intent);
            finish();
        }
    }

    @Override



    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void showGuideDialog(String success) {
        final Dialog alert = new Dialog(FactorList.this);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.custom_dialog_after_pay);
        TextView buttonTextView = alert.findViewById(R.id.txt_action);
        MyTextView txt_status = alert.findViewById(R.id.txt_status);
        AppCompatImageView statusIcon = alert.findViewById(R.id.statusIcon);

        new Thread(new Task()).start();

        if (success != null && success.equals("1")) {
            statusIcon.setBackgroundResource(R.drawable.success);
            txt_status.setText("پرداخت با موفقیت انجام شد");

        } else {
            statusIcon.setBackgroundResource(R.drawable.unsucceess);
            txt_status.setText("پرداخت نا موفق");
        }

        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.dismiss();
            }
        });
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private class Task implements Runnable {
        @Override
        public void run() {
            db = new DatabaseHandler(getApplicationContext());
            if (db.getRowCount() > 0) {
                HashMap<String, String> userDetailsHashMap = db.getUserDetails();
                id = userDetailsHashMap.get("id");
                System.out.println("id===========" + id);
                getFactor();
            }
        }
    }

    public void getCreditRequest() {

        RequestQueue queue = Volley.newRequestQueue(FactorList.this);

        final String url = AppConfig.BASE_URL + "api/main/getCredit/" + id;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                String credit = response.getString("credit");

                                if (response.getString("credit") == null) {
                                    credit = "0";
                                }
                                EventBus.getDefault().post(new EventbusModel(credit));
                                System.out.println("====credit====" + credit);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppConfig.error(error);
                    }
                }
        );

        queue.add(getRequest);

    }

}
