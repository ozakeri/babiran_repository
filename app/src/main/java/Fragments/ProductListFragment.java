package Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.ProductListAdapter;

import Models.Category;

import net.babiran.app.MainActivity;
import net.babiran.app.R;

import Models.Feature;
import Models.Image;
import Models.Product;
import tools.AppConfig;

import static tools.AppConfig.categories;


public class ProductListFragment extends Fragment {

    public String category_id = "0";
    static public String category_parent_id = "-1";
    public ArrayList<Product> products = new ArrayList<>();
    View v;
    RequestQueue queue;
    ListView listView;
    String prev = "";
    String id;

    Category category;


    public static final String TAG = "TAG";
    public boolean backToCategory = true;

    public ProductListFragment(ArrayList<Product> productsArrayList, String p_id, String prev) {
        this.products = productsArrayList;
        this.category_parent_id = p_id;
        this.prev = prev;
        // backToCategory = false;
    }


    public ProductListFragment(ArrayList<Product> pp) {
        this.products = pp;

    }

    public ProductListFragment(ArrayList<Product> pp, String prev) {
        this.products = pp;
        this.prev = prev;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.pro_list_fragment, container, false);

        AppConfig.tempActivity = (MainActivity) getActivity();
        listView = (ListView) v.findViewById(R.id.category_listView);

        //Toast.makeText(getActivity(),category_id,Toast.LENGTH_SHORT).show();

        if (products.size() > 0) {
            v.findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);
            MainActivity.productlist.setVisibility(View.VISIBLE);
            ProductListAdapter adp = new ProductListAdapter(getActivity(), products);
            listView.setAdapter(adp);
        } else if (prev.equals("category")) {
            //Volley Start
            v.findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);
            MainActivity.productlist.setVisibility(View.VISIBLE);

            queue = Volley.newRequestQueue(getActivity());
            final ProgressDialog d = new ProgressDialog(getActivity());
            d.setMessage("چند لحظه صبرکنید ...");
            d.setIndeterminate(true);
            d.setCancelable(false);
            d.show();

            String url = AppConfig.BASE_URL + "api/main/search";
            // Request a string response from the provided URL.

            StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            d.dismiss();
                            try {
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
                                    v.findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);

                                    AppConfig.fragmentManager.beginTransaction().replace(R.id.ProductListcontainer, new ProductListFragment(products)).commit();

                                } else {
                                    v.findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);
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

                    if (id != null) {
                        Log.d("cat id", id);
                        params.put("category_id", id);
                        System.out.println("category_id===" + id);

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


        } else {
            /////////////////////
            // refresh favs
            //getFavsFromServer();

            ///////////////////////////
        }

        return v;

    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    public void dialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("آگهی وجود ندارد");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "باشه",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();

        MainActivity.viewLogo.setVisibility(View.GONE);

        MainActivity.btnBack.setVisibility(View.VISIBLE);
        MainActivity.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Resume", category_parent_id);
                MainActivity.productlist.setVisibility(View.INVISIBLE);
                Log.e("previous", prev);
                if (category_parent_id != null) {
                    if (category_parent_id.equals("0") || category_parent_id.equals("-1")) {
                        MainActivity.secondcategory.setVisibility(View.INVISIBLE);
                    } else {
                        if (!prev.equals("notcat")) {
                            if (getCategoryID(category_parent_id).equals("0") || getCategoryID(category_parent_id).equals("-1")) {
                                MainActivity.secondcategory.setVisibility(View.INVISIBLE);

                            } else {
                                if (prev.equals("second")) {
                                    AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer, new SecondCategoryFragment(category_parent_id, "backToSecond")).commit();
                                } else {
                                    AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer, new SecondCategoryFragment(category_parent_id)).commit();
                                }
                            }

                        }

                    }
                }
                MainActivity.viewLogo.setVisibility(View.VISIBLE);

                MainActivity.btnBack.setVisibility(View.GONE);
            }
        });
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener

                    Log.e("Resume", category_parent_id);
                    MainActivity.productlist.setVisibility(View.INVISIBLE);
                    Log.e("previous", prev);
                    if (category_parent_id != null) {
                        if (category_parent_id.equals("0") || category_parent_id.equals("-1")) {
                            MainActivity.secondcategory.setVisibility(View.INVISIBLE);
                        } else {
                            if (!prev.equals("notcat")) {
                                if (getCategoryID(category_parent_id).equals("0") || getCategoryID(category_parent_id).equals("-1")) {
                                    MainActivity.secondcategory.setVisibility(View.INVISIBLE);

                                } else {
                                    if (prev.equals("second")) {
                                        AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer, new SecondCategoryFragment(category_parent_id, "backToSecond")).commit();
                                    } else {
                                        AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer, new SecondCategoryFragment(category_parent_id)).commit();
                                    }
                                }

                            }

                        }
                    }
                    MainActivity.viewLogo.setVisibility(View.VISIBLE);

                    MainActivity.btnBack.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });

    }

    public String getCategoryID(String ID) {

        String parent_id = "0";

        for (int i = 0; i < categories.length(); i++) {
            try {
                JSONObject c = categories.getJSONObject(i);

                Category category = new Category(c.getString("id"), c.getString("name"), c.getString("parent_id")
                        , c.getString("icon"), c.getString("slide_image"));

                if (category.id.equals(ID)) {
                    parent_id = category.parent_id;
                }

            } catch (JSONException e) {

                e.printStackTrace();
                AppConfig.error(e);

            }
        }
        return parent_id;
    }


}
