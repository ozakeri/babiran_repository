package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.babiran.app.MainActivity;
import net.babiran.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.ProductListAdapter;
import Models.Color;
import Models.Feature;
import Models.Image;
import Models.Moshakhasat;
import Models.Product;
import tools.AppConfig;
import tools.Util;

public class SearchFrgment extends Fragment {

    View v;

    public static AppCompatEditText ed_name;
    ImageView searchImg;
    //RelativeLayout RelativeLayout_search;
    public static RelativeLayout progressLayout;
    LinearLayout searchTag;
    public static ListView searchResult;

    RequestQueue queue;
    public static final String TAG = "TAG";
    private ProductListAdapter adp = null;
    private JSONArray jsonArray;

    String[] filter = {"پر فروش ترین", "جدید ترین", "ارزان ترین", "گران ترین"};


    private TimerTask timerTask;
    private SearchView searchView;
    private Handler handler;

    private int offset = 1;
    private int limit = 10;
    private boolean isLoadMore = false;
    private Handler mHandler = new Handler();
    private boolean flag_loading = false;

    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms


    public SearchFrgment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_search_frgment, container, false);

        AppConfig.frag = SearchFrgment.this;
        handler = new Handler();

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;


        ed_name = v.findViewById(R.id.search);
        searchImg = (ImageView) v.findViewById(R.id.searchImage);
        // RelativeLayout_search = (RelativeLayout) v.findViewById(R.id.RelativeLayout_search);

        searchResult = (ListView) v.findViewById(R.id.search_product);

        searchTag = (LinearLayout) v.findViewById(R.id.searchtag);
        progressLayout = (RelativeLayout) v.findViewById(R.id.progressLayout);

        //RelativeLayout_search.getLayoutParams().height = (int) (width * 0.15);
        //searchResult.getLayoutParams().height = (int) (width * 1);

        //getRandomPro();

        searchResult.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

               /* int lastInScreen = firstVisibleItem + visibleItemCount;

                Log.d("TEST", "first : " + firstVisibleItem);
                Log.d("TEST", "visible : " + visibleItemCount);
                Log.d("TEST", "total : " + totalItemCount);


                if ((lastInScreen == totalItemCount) && !isLoadMore && (firstVisibleItem != 0)) {
                    isLoadMore = true;

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            submit(ed_name.getText().toString());
                            adp.notifyDataSetChanged();
                            isLoadMore = false;
                        }
                    }, 1500);

                }*/
            }
        });

        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // user is typing: reset already started timer (if existing)
                //progressLayout.setVisibility(View.VISIBLE);

                if (s.length() == 0) {
                    progressLayout.setVisibility(View.GONE);
                }
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // user typed: start the timer
                progressLayout.setVisibility(View.VISIBLE);
                if (arg0.length() == 0) {
                    progressLayout.setVisibility(View.GONE);
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
                          /*  if (arg0.length() == 0){
                                queue.cancelAll(new RequestQueue.RequestFilter() {
                                    @Override
                                    public boolean apply(Request<?> request) {
                                        return true;
                                    }
                                });
                            }*/
                            submit(arg0.toString());
                        }
                    }, 1500); // 600ms delay before the timer executes the „run“ method from TimerTask
                }

            }
        });


        /*ed_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ed_name.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(ed_name.getWindowToken(), 0);

                    try {
                        submit();
                    } catch (Exception e) {

                    }

                    return true;
                }
                return false;
            }
        });*/

       /* searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("search", "here");
                if (ed_name.getText().length() == 0) {

                } else {

                    ed_name.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(ed_name.getWindowToken(), 0);

                    try {
                        submit();
                    } catch (Exception e) {

                    }

                }
            }
        });*/


        /*for (int i = 0; i < filter.length; i++) {
            CardSearch cardSearch = new CardSearch(getActivity(), filter[i], i);
            searchTag.addView(cardSearch);
        }*/

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    MainActivity.search.setVisibility(View.INVISIBLE);
                    MainActivity.btnBack.setVisibility(View.GONE);
                    MainActivity.viewLogo.setVisibility(View.VISIBLE);
                    MainActivity.home.setVisibility(View.VISIBLE);

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    public void submit(final String s) {


        //Volley Start

        // ed_name.setEnabled(false);
        queue = Volley.newRequestQueue(getActivity());
        String url = AppConfig.BASE_URL + "api/main/search";
        // Request a string response from the provided URL.
        System.out.println("url=====" + url);
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<Product> products = new ArrayList<>();
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
                                progressLayout.setVisibility(View.GONE);

                                //   AppConfig.fragmentManager.beginTransaction().replace(R.id.ProductListcontainer,new ProductListFragment(products)).commit();

                                adp = new ProductListAdapter(getActivity(), products);
                                searchResult.setAdapter(adp);
                                adp.notifyDataSetChanged();
                            } else {
                                v.findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);
                                // v.findViewById(R.id.goosh).setVisibility(View.VISIBLE);
                                //Toast.makeText(getActivity(),"آگهی با ویژگی های مشخص شده پیدا نشد",Toast.LENGTH_SHORT).show();

                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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


    public void getRandomPro() {

        queue = Volley.newRequestQueue(getActivity());
        String url = "http://tondmarket.com/api/main/getRandomProducts/0/20";
        // Request a string response from the provided URL.

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("randomRes", response);
                            ArrayList<Product> products = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < 20; i++) {

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
                            if (products.size() > 0) {

                                adp = new ProductListAdapter(getActivity(), products);
                                searchResult.setAdapter(adp);
                                adp.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        jsonArrayRequest.setTag(TAG);
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                400000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);

    }


  /*  private void initSearch() {
        if (searchView != null && searchView.getQuery() != null) {
            String query = ed_name.getText().toString();
            setupAdapter(query);
        } else {
            setupAdapter(null);
        }
    }

    private void setupAdapter(String query) {
        if (query != null) {
            submit();
        } else {
            //clearResults();
        }
    }*/


    private class Search implements Runnable {
        @Override
        public void run() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 2000);
        }
    }

}
