package Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.CategoryAdapter;
import Adapters.CategoryListAdapter;
import Models.Category;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import net.babiran.app.MainActivity;
import net.babiran.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Models.CategoryInfo;
import Models.Feature;
import Models.Image;
import Models.Product;
import tools.AppConfig;

import static tools.AppConfig.categories;
import static tools.AppConfig.restaurants_info;


public class SecondCategoryFragment extends Fragment {

    public String id = "0";

    View v;
    RequestQueue queue;
    //ListView listView;
    String prev = "" ;
    String Mainprev = "" ;
    Category category,parent_category;
    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter ;
    ArrayList<Category> categoryArrayList = null ;

    ArrayList<CategoryInfo> categoriesInfos = null ;
    public static final String TAG = "TAG";


    public SecondCategoryFragment(String id) {
        this.id = id;
    }
    public SecondCategoryFragment(String id,String prev) {
        this.id = id;
        this.prev = prev ;
    }
    public SecondCategoryFragment(ArrayList<Category> categories , String prev){
        this.categoryArrayList = categories ;
        this.Mainprev = prev ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e("category","calcul");

        v = inflater.inflate(R.layout.second_category_fragment, container, false);

        MainActivity.secondcategory.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) v.findViewById(R.id.other_category_list_grid);
        //listView = (ListView) v.findViewById(R.id.category_listView);


        //    AppConfig.frag = SecondCategoryFragment.this;
        // calcute();

        if(Mainprev.equals("backInSecond")) {
            if(categoryArrayList!= null){
                CategoryListAdapter adp = new CategoryListAdapter(getActivity(),categoryArrayList);
                //listView.setAdapter(adp);
                adp.notifyDataSetChanged();
            }

        }
        else{
            if(prev.equals("backToSecond")){
                if(getCategory(getCategoryID(id)).size()>0){
                    CategoryListAdapter adp = new CategoryListAdapter(getActivity(),getCategory(getCategoryID(id)));
                    //listView.setAdapter(adp);
                    adp.notifyDataSetChanged();
                }
                else{
                    getCompaniesByID(getCategoryID(getCategoryID(id)));
                }
            }
            else
            {


                if(id.equals("1197"))
                {
//                    if(getCategory(id).size() > 0)
//                    {
//                        Log.e("111","here");
//                        CategoryListAdapterNew adp = new CategoryListAdapterNew(getActivity(),getCategoryZS(id));
//                        listView.setAdapter(adp);
//                        adp.notifyDataSetChanged();
//                    }
                }
                else if(id.equals("1196"))
                {
//                    if(getCategory(id).size() > 0)
//                    {
//                        Log.e("111","here");
//
//                        CategoryListAdapterNew adp = new CategoryListAdapterNew(getActivity(),getCategoryZS(id));
//                        listView.setAdapter(adp);
//                        adp.notifyDataSetChanged();
//                    }
                }
                else
                {
                    if(getCategory(id).size() > 0)
                    {
                        Log.e("111","here");
            categoryAdapter = new CategoryAdapter(getCategory(id),getActivity());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity() , 2 , LinearLayoutManager.VERTICAL , false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(categoryAdapter);
            categoryAdapter.notifyDataSetChanged();

                        CategoryListAdapter adp = new CategoryListAdapter(getActivity(),getCategory(id));
                        //listView.setAdapter(adp);
                        adp.notifyDataSetChanged();
                    }
                    else{

                        Log.e("222","here");
                        getCompaniesByID(id);
                    }
              }




            }

        }



        return v;

    }

    public ArrayList<Category> getCategory(String parentID)
    {

        ArrayList<Category> categoryArrayList = new ArrayList<>();

        for (int i = 0; i < categories.length(); i++) {
            try {
                JSONObject c = categories.getJSONObject(i);

                Category category =  new Category(c.getString("id") ,c.getString("name"), c.getString("parent_id")
                        ,c.getString("icon"),c.getString("slide_image"));

                if(category.parent_id.equals(parentID)){
                    categoryArrayList.add(category);
                }

            } catch (JSONException e) {

                e.printStackTrace();
                AppConfig.error(e);

            }
        }
        return categoryArrayList ;
    }
    public ArrayList<CategoryInfo> getCategoryZS(String parentID)
    {

        ArrayList<CategoryInfo> categoryArrayList = new ArrayList<>();

        for (int i = 0; i < restaurants_info.length(); i++) {
            try {
                JSONObject c = restaurants_info.getJSONObject(i);

                CategoryInfo category =  new CategoryInfo("1197",c.getString("category_id"),c.getString("work_time") ,c.getString("service_area"),c.getString("minimum_order"),c.getString("image"));
              //  CategoryInfo category =  new CategoryInfo(c.getString("id"),c.getString("category_id"),c.getString("work_time") ,c.getString("service_area"),c.getString("minimum_order"),c.getString("image"));

                if(category.getId().equals(parentID)){
                    categoryArrayList.add(category);
                }

            } catch (JSONException e) {

                e.printStackTrace();
                AppConfig.error(e);

            }
        }
        return categoryArrayList ;
    }
    public String getCategoryID(String ID){

        String parent_id = "0";

        for (int i = 0; i < categories.length(); i++) {
            try {
                JSONObject c = categories.getJSONObject(i);

                Category category =  new Category(c.getString("id") ,c.getString("name"), c.getString("parent_id")
                        ,c.getString("icon"),c.getString("slide_image"));

                if(category.id.equals(ID)){
                    parent_id = category.parent_id ;
                }

            } catch (JSONException e) {

                e.printStackTrace();
                AppConfig.error(e);

            }
        }
        return parent_id ;
    }

    public void getCompaniesByID(final String id){

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
                        Log.e("responseINCat",response);


                        try {

                            ArrayList<Product> products = new ArrayList<>();
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
                            Log.e("compSize",products.size()+"");

                            if(products.size() > 0){
                               /* Gson gson = new Gson();
                                String compObj = gson.toJson(companies);
                                Intent intent = new Intent(SecondCategory.this,CompanyList.class);
                                intent.putExtra("companies",compObj);
                                startActivity(intent);
                                SecondCategory.secondCategory.finish();*/

                                //  MainActivity.secondcategory.setVisibility(View.VISIBLE);

                                AppConfig.fragmentManager.beginTransaction().replace(R.id.ProductListcontainer,new ProductListFragment(products,id,"second")).commit();

                                System.out.println("---id---" + id);

                            }
                            else{
                                v.findViewById(R.id.no_comp).setVisibility(View.VISIBLE);
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


                params.put("category_id",id);

                return params;
            }

        };
        jsonArrayRequest.setTag("tag");
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                400000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (queue != null) {
            queue.cancelAll(TAG);
        }
        MainActivity.btnBack.setVisibility(View.VISIBLE);
        MainActivity.viewLogo.setVisibility(View.GONE);
        MainActivity.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("previous",prev);

                // handle back button's click listener


                Log.e("parentID",getCategoryID(id));
                if(!getCategoryID(id).equals("0") && !getCategoryID(id).equals("-1") && !getCategoryID(id).equals("null")
                        && !getCategoryID(id).equals("") && getCategoryID(id) != null){
                    if(getCategoryID(getCategoryID(id)).equals("0")|| getCategoryID(getCategoryID(id)).equals("-1")){
                        MainActivity.secondcategory.setVisibility(View.INVISIBLE);
                    }
                    else{
                        AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer,new SecondCategoryFragment(getCategory(getCategoryID(id)),"backInSecond")).commit();
                    }

                }
                else{
                    MainActivity.secondcategory.setVisibility(View.INVISIBLE);

                }
                MainActivity.btnBack.setVisibility(View.GONE);
                MainActivity.viewLogo.setVisibility(View.VISIBLE);
            }
        });
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    Log.e("previous",prev);

                    // handle back button's click listener


                    Log.e("parentID",getCategoryID(id));
                    if(!getCategoryID(id).equals("0") && !getCategoryID(id).equals("-1") && !getCategoryID(id).equals("null")
                            && !getCategoryID(id).equals("") && getCategoryID(id) != null){
                        if(getCategoryID(getCategoryID(id)).equals("0")|| getCategoryID(getCategoryID(id)).equals("-1")){
                            MainActivity.secondcategory.setVisibility(View.INVISIBLE);
                        }
                        else{
                            AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer,new SecondCategoryFragment(getCategory(getCategoryID(id)),"backInSecond")).commit();
                        }

                    }
                    else{
                        MainActivity.secondcategory.setVisibility(View.INVISIBLE);

                    }
                    MainActivity.btnBack.setVisibility(View.GONE);
                    MainActivity.viewLogo.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });

    }



}
