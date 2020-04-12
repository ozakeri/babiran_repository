package Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Map;

import Adapters.ProductListAdapter;
import Models.Category;
import Models.Feature;
import Models.Image;
import Models.Product;
import tools.AppConfig;
import ui_elements.CardSearch;
import ui_elements.MyEditText;

public class SearchFrgment extends Fragment {

    private View v;

    private Category category;
    private MyEditText ed_name;
    private RelativeLayout progressLayout;
    private ListView searchResult;
    private RequestQueue queue;
    public static final String TAG = "TAG";
    private ProductListAdapter adp;
    private ArrayList<Product> products = new ArrayList<>();

    String[] filter = {"پر فروش ترین", "جدید ترین", "ارزان ترین", "گران ترین"};

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

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;


        ed_name = (MyEditText) v.findViewById(R.id.search);
        ImageView searchImg = (ImageView) v.findViewById(R.id.searchImage);
        RelativeLayout relativeLayout_search = (RelativeLayout) v.findViewById(R.id.RelativeLayout_search);

        searchResult = (ListView) v.findViewById(R.id.search_product);

        //searchTag = (LinearLayout) v.findViewById(R.id.searchtag);
        progressLayout = (RelativeLayout) v.findViewById(R.id.progressLayout);

        relativeLayout_search.getLayoutParams().height = (int) (width * 0.15);
        // searchResult.getLayoutParams().height = (int)(width *1) ;

        //getRandomPro();

        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ed_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        });

        searchImg.setOnClickListener(new View.OnClickListener() {
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
        });


        /*for (int i = 0; i < filter.length; i++) {
            CardSearch cardSearch = new CardSearch(getActivity(), filter[i], i);
            searchTag.addView(cardSearch);
        }*/

        return v;

    }

    public void onUserSelectValue(Category category) {
        // TODO add your implementation.
        // Toast.makeText(getActivity(),"oooops "+ category.name,Toast.LENGTH_SHORT).show();
        this.category = category;
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

                    if (MainActivity.productlist.getVisibility() == View.VISIBLE) {

                        MainActivity.productlist.setVisibility(View.INVISIBLE);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AppConfig.act);
                        builder.setTitle("می خواهید خارج شوید؟");
                        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (AppConfig.checkReciveSms == true) {
                                    AppConfig.checkReciveSms = false;
                                }
                                if (AppConfig.btnSubmitOk == true) {
                                    AppConfig.btnSubmitOk = false;
                                }

                                AppConfig.act.finish();


                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

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

    public void submit() {


        progressLayout.setVisibility(View.VISIBLE);
        //Volley Start


        queue = Volley.newRequestQueue(getActivity());
        String url = AppConfig.BASE_URL + "api/main/search";
        // Request a string response from the provided URL.

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            products = null;
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                ArrayList<Feature> featuresArray = new ArrayList<>();
                                ArrayList<Image> imagesArray = new ArrayList<>();
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

                                Product product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray);

                                products.add(product);
                            }
                            if (products.size() > 0) {
                                progressLayout.setVisibility(View.GONE);

                                //   AppConfig.fragmentManager.beginTransaction().replace(R.id.ProductListcontainer,new ProductListFragment(products)).commit();

                                adp = new ProductListAdapter(getActivity(), products);
                                adp.notifyDataSetChanged();
                                searchResult.setAdapter(adp);
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

                System.out.println("key====" + ed_name.getText().toString());

                if (ed_name.getText().toString().length() > 0) {
                    params.put("key", ed_name.getText().toString());
                }


                System.out.println("category====" + category);
                if (category != null) {
                    params.put("category_id", category.id);

                }


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
                            for (int i = 0; i < jsonArray.length(); i++) {

                                ArrayList<Feature> featuresArray = new ArrayList<>();
                                ArrayList<Image> imagesArray = new ArrayList<>();
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

                                Product product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray);

                                products.add(product);
                            }
                            if (products.size() > 0) {

                                ProductListAdapter adp = new ProductListAdapter(getActivity(), products);
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


}
