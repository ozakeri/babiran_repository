package net.babiran.app;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.SearchProductListAdapter;
import Fragments.ProductFragment;
import Models.Color;
import Models.Feature;
import Models.Image;
import Models.Moshakhasat;
import Models.Product;
import tools.AppConfig;
import tools.RecyclerItemClickListener;
import tools.Util;
import ui_elements.MyEditText;

public class SearchActivity extends AppCompatActivity {

    private MyEditText search;
    private RecyclerView recycler_view;
    private CircularProgressView circularProgressView;
    private Timer timer = new Timer();
    private RequestQueue queue;
    private static final String TAG = "TAG";
    private SearchProductListAdapter adp = null;
    private JSONArray jsonArray;
    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = findViewById(R.id.search);
        recycler_view = findViewById(R.id.recycler_view);
        circularProgressView = findViewById(R.id.progressBarCircularIndeterminate);
        circularProgressView.setVisibility(View.GONE);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // user is typing: reset already started timer (if existing)
                //progressLayout.setVisibility(View.VISIBLE);

                if (s.length() == 0) {
                    circularProgressView.setVisibility(View.GONE);
                }
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // user typed: start the timer
                circularProgressView.setVisibility(View.VISIBLE);
                if (arg0.length() == 0) {
                    circularProgressView.setVisibility(View.GONE);
                    queue.cancelAll(new RequestQueue.RequestFilter() {
                        @Override
                        public boolean apply(Request<?> request) {
                            return true;
                        }
                    });
                }
                if (arg0.length() >= 2) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // do your actual work here
                            submit(arg0.toString());
                        }
                    }, 1500); // 600ms delay before the timer executes the „run“ method from TimerTask
                }

            }
        });

        recycler_view.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recycler_view.setItemAnimator(new DefaultItemAnimator());

        /*recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(SearchActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println("position=========" + products.get(position));
                //AppConfig.fragmentManager.beginTransaction().replace(R.id.Productcontainer, new ProductFragment(products.get(position))).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.Productcontainer, new ProductFragment(products.get(position))).commit();
            }
        }));*/
    }

    public void submit(final String s) {

        queue = Volley.newRequestQueue(SearchActivity.this);
        String url = AppConfig.BASE_URL + "api/main/search";
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            products = new ArrayList<>();
                            jsonArray = new JSONArray(response);
                            System.out.println("jsonArray====" + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {

                                ArrayList<Feature> featuresArray = new ArrayList<>();
                                ArrayList<Image> imagesArray = new ArrayList<>();
                                ArrayList<Moshakhasat> moshakhasatArrayList = new ArrayList<>();
                                ArrayList<Color> colorArrayList = new ArrayList<>();
                                JSONObject c = jsonArray.getJSONObject(i);

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
                                for (int img = 0; img < images.length(); img++) {

                                    try {
                                        JSONObject im = images.getJSONObject(img);
                                        Image image = new Image(
                                                im.getString("image_link"));
                                        imagesArray.add(img, image);
                                    } catch (JSONException ex) {

                                    }
                                }

                                if (!c.isNull("moshakhasat")) {
                                    JSONArray jsonArray1 = c.getJSONArray("moshakhasat");

                                    for (int mo = 0; mo < jsonArray1.length(); mo++) {

                                        try {
                                            JSONObject im = jsonArray1.getJSONObject(mo);
                                            Moshakhasat moshakhasat = new Moshakhasat(im.getString("name"), im.getString("val"));
                                            moshakhasatArrayList.add(mo, moshakhasat);
                                        } catch (JSONException ex) {

                                        }
                                    }
                                }

                                if (!c.isNull("rang")) {
                                    JSONArray colorJSONArray = c.getJSONArray("rang");
                                    for (int iColor = 0; iColor < colorJSONArray.length(); iColor++) {

                                        try {
                                            JSONObject im = colorJSONArray.getJSONObject(iColor);
                                            Color color = new Color(Util.createTransactionID(), im.getString("name"), im.getString("val"));
                                            colorArrayList.add(iColor, color);
                                        } catch (JSONException ex) {

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }


                                Product product = new Product(c.getString("category_id1"), c.getString("id"), c.getString("name"), c.getString("description"),
                                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, moshakhasatArrayList, colorArrayList, c.getString("provider_name"));

                                products.add(product);

                            }

                            if (products != null && products.size() > 0) {
                                circularProgressView.setVisibility(View.GONE);

                                //   AppConfig.fragmentManager.beginTransaction().replace(R.id.ProductListcontainer,new ProductListFragment(products)).commit();

                                adp = new SearchProductListAdapter(SearchActivity.this, products);
                                recycler_view.setAdapter(adp);
                                adp.notifyDataSetChanged();
                            } else {
                                circularProgressView.setVisibility(View.GONE);
                                // v.findViewById(R.id.goosh).setVisibility(View.VISIBLE);
                                //Toast.makeText(getActivity(),"آگهی با ویژگی های مشخص شده پیدا نشد",Toast.LENGTH_SHORT).show();

                                AlertDialog alertDialog = new AlertDialog.Builder(SearchActivity.this).create();
                                alertDialog.setTitle("محصول با ویژگی های مشخص شده پیدا نشد");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "باشه",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                            }
                                        });
                                alertDialog.show();
                            }


                            //ed_name.setEnabled(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();
                //params.put("offset", String.valueOf(offset));
                //params.put("limit", String.valueOf(limit));
                params.put("key", s);
                JSONObject obj = new JSONObject(params);
                System.out.println("params====" + obj);


                return params;
            }

        };
        jsonArrayRequest.setTag(TAG);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                400000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);


        //Volley End
    }
}
