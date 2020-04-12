package ui_elements;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import net.babiran.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.ProductListAdapter;
import Fragments.SearchFrgment;
import Models.Feature;
import Models.Image;
import Models.Product;
import tools.AppConfig;


public class CardSearch extends RelativeLayout {


    public Context contex;
	public MyTextView tag;
    public String filter = "" ;
    public int index = 0 ;
    RequestQueue queue ;
    ArrayList<Product> products ;
    ProductListAdapter adp ;


    public CardSearch(Context context , String filter , int index) {
        super(context);

        this.index = index ;
        this.filter = filter ;
        init(context,filter,index);
    }

    public CardSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init(context);
    }



    public CardSearch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //init(context);
    }


    public void init(final Context context, final String filter , int index) {


        this.index = index ;
        this.filter = filter ;
        contex = context;
        inflate(getContext(), R.layout.search_card, this);

        this.tag = (MyTextView) findViewById(R.id.tag_txt);


        this.tag.setText(filter);
        findViewById(R.id.filter_search).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit();

                }
            });



    }

    public void submit(){


        //SearchFrgment.progressLayout.setVisibility(View.VISIBLE);
        //Volley Start


        queue = Volley.newRequestQueue(contex);
        String url = AppConfig.BASE_URL + "/api/main/search";
        // Request a string response from the provided URL.

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("search",response);
                            products = new ArrayList<>();
                            JSONArray jsonArray =  new JSONArray(response);
                            for(int i= 0 ; i < jsonArray.length(); i++){

                                ArrayList<Feature> featuresArray = new ArrayList<>();
                                ArrayList<Image> imagesArray  = new ArrayList<>();
                                JSONObject c = jsonArray.getJSONObject(i);

                                JSONArray features = c.getJSONArray("features") ;
                                for(int fea = 0 ; fea < features.length() ; fea ++){
                                    try{
                                        JSONObject f = features.getJSONObject(fea);
                                        Feature feature = new Feature(
                                                f.getString("value"),f.getString("name"));
                                        featuresArray.add(fea,feature);
                                    }
                                    catch (JSONException ex){

                                    }
                                }

                                JSONArray images = c.getJSONArray("images") ; ;
                                for(int img = 0 ; img < images.length() ; img ++){

                                    try{
                                        JSONObject im = images.getJSONObject(img);
                                        Image image = new Image(
                                                im.getString("image_link"));
                                        imagesArray.add(img,image);
                                    }
                                    catch (JSONException ex){

                                    }
                                }

                                Product  product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                        c.getString("price"), c.getString("stock"),"",c.getString("discount_price"), imagesArray, featuresArray);

                                products.add(product);
                            }
                            if(products.size()>0){
                              //SearchFrgment.progressLayout.setVisibility(View.GONE);

                                adp = new ProductListAdapter(contex, products);
                               // searchResult.setAdapter(adp);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley",error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();

              /*  Log.e("searchKey",ed_name.getText().toString());
                Log.e("tag",tag.getText().toString());
                if(ed_name.getText().toString().length()>0){
                    params.put("key", ed_name.getText().toString());
                }*/


                if(tag.getText().toString().equals("پر فروش ترین")){
                    params.put("order","sells_count");
                    params.put("order_type","desc");
                }

                if(tag.getText().toString().equals("جدید ترین")){
                    params.put("order","id");
                    params.put("order_type","desc");
                }

                if(tag.getText().toString().equals("ارزان ترین")){
                    params.put("order","price");
                    params.put("order_type","asc");
                }

                if(tag.getText().toString().equals("گران ترین")){
                    params.put("order","price");
                    params.put("order_type","desc");
                }

                params.put("offset","0");
                params.put("limit","30");




                return params;
            }

        };
        jsonArrayRequest.setTag("TAG");
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                400000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);



        //Volley End
    }


    public void submitLazy(){


       // SearchFrgment.progressLayout.setVisibility(View.VISIBLE);
        //Volley Start


        queue = Volley.newRequestQueue(contex);
        String url = "http://tondmarket.com/api/main/search";
        // Request a string response from the provided URL.

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("search",response);
                            JSONArray jsonArray =  new JSONArray(response);
                            for(int i= 0 ; i < jsonArray.length(); i++){

                                ArrayList<Feature> featuresArray = new ArrayList<>();
                                ArrayList<Image> imagesArray  = new ArrayList<>();
                                JSONObject c = jsonArray.getJSONObject(i);

                                JSONArray features = c.getJSONArray("features") ;
                                for(int fea = 0 ; fea < features.length() ; fea ++){
                                    try{
                                        JSONObject f = features.getJSONObject(fea);
                                        Feature feature = new Feature(
                                                f.getString("value"),f.getString("name"));
                                        featuresArray.add(fea,feature);
                                    }
                                    catch (JSONException ex){

                                    }
                                }

                                JSONArray images = c.getJSONArray("images") ; ;
                                for(int img = 0 ; img < images.length() ; img ++){

                                    try{
                                        JSONObject im = images.getJSONObject(img);
                                        Image image = new Image(
                                                im.getString("image_link"));
                                        imagesArray.add(img,image);
                                    }
                                    catch (JSONException ex){

                                    }
                                }

                                Product  product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                        c.getString("price"), c.getString("stock"),"",c.getString("discount_price"), imagesArray, featuresArray);

                                products.add(product);
                                adp.notifyDataSetChanged();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley",error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();

               /* Log.e("searchKey",ed_name.getText().toString());
                Log.e("tag",tag.getText().toString());
                if(ed_name.getText().toString().length()>0){
                    params.put("key", ed_name.getText().toString());
                }*/


                if(tag.getText().toString().equals("پر فروش ترین")){
                    params.put("order","sells_count");
                    params.put("order_type","desc");
                }

                if(tag.getText().toString().equals("جدید ترین")){
                    params.put("order","id");
                    params.put("order_type","desc");
                }

                if(tag.getText().toString().equals("ارزان ترین")){
                    params.put("order","price");
                    params.put("order_type","asc");
                }

                if(tag.getText().toString().equals("گران ترین")){
                    params.put("order","price");
                    params.put("order_type","desc");
                }

                params.put("offset",products.size()+"");
                params.put("limit","30");




                return params;
            }

        };
        jsonArrayRequest.setTag("TAG");
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                400000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);



        //Volley End
    }




}