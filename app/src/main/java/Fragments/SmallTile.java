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
import net.babiran.app.MainActivity;
import net.babiran.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Models.Feature;
import Models.Image;
import Models.Product;
import tools.AppConfig;

/**
 * Created by Mohammad on 6/28/2017.
 */

@SuppressWarnings("ALL")
public class SmallTile extends Fragment {

    ListView smalltile ;

    String id ;
    View v ;
    String prev = "";
    RequestQueue queue ;

    public SmallTile(String id , String prev){

        this.id = id ;
        this.prev = prev ;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.small_tile, container, false);


        smalltile = (ListView) v.findViewById(R.id.SmallTileList);


        if (!id.equals("") && !id.equals("null") && id != null) {
            MainActivity.smalltile_banner.setVisibility(View.VISIBLE);
            if(prev.equals("first")){
                /*SmallTileAdapter smallTileAdapter = new SmallTileAdapter(getActivity(), products,"first");
                smalltile.setAdapter(smallTileAdapter);
                smallTileAdapter.notifyDataSetChanged();*/
                submit();
            }
            else if(prev.equals("second")){
               /* SmallTileAdapter  smallTileAdapter = new SmallTileAdapter(getActivity(), products,"second");
                smalltile.setAdapter(smallTileAdapter);
                smallTileAdapter.notifyDataSetChanged();*/
                submit();
            }
            else if(prev.equals("third")){
               /* SmallTileAdapter  smallTileAdapter = new SmallTileAdapter(getActivity(), products,"third");
                smalltile.setAdapter(smallTileAdapter);
                smallTileAdapter.notifyDataSetChanged();*/
                submit();
            }
            else if(prev.equals("fourth")){
                /*SmallTileAdapter  smallTileAdapter = new SmallTileAdapter(getActivity(), products,"fourth");
                smalltile.setAdapter(smallTileAdapter);
                smallTileAdapter.notifyDataSetChanged();
            */
                submit();
            }
        }


        return v ;
    }

    public void submit(){


        //  getActivity().findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
        //Volley Start


        final ProgressDialog d = new ProgressDialog(getActivity());
        d.setMessage("چند لحظه صبرکنید ...");
        d.setIndeterminate(true);
        d.setCancelable(false);
        d.show();

        queue = Volley.newRequestQueue(getActivity());
        String url = AppConfig.BASE_URL + "api/main/search";

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressWarnings("MagicConstant")
                    @Override
                    public void onResponse(String response) {
                        d.dismiss();
                        try {
                            Log.e("response",response);
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

                                //noinspection MagicConstant
                                @SuppressWarnings("MagicConstant") Product  product = new Product(c.getString("category_id1"),c.getString("id"), c.getString("name"), c.getString("description"),
                                        c.getString("price"), c.getString("stock"),"",c.getString("discount_price"), imagesArray, featuresArray);

                                products.add(product);
                            }
                            if(products.size()>0){
                                //   getActivity().findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);
                                MainActivity.smalltile_banner.setVisibility(View.INVISIBLE);
                                AppConfig.fragmentManager.beginTransaction().replace(R.id.ProductListcontainer,new ProductListFragment(products,"notcat")).commit();

                            }else{
                                //   getActivity().findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);


                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                alertDialog.setTitle("محصولِی در این دسته بندی موجود نیست ");
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
                Log.e("Volley",error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();

                if (!id.equals("") && !id.equals("null") && id != null) {
                    params.put("category_id", id);
                }


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

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    Log.e("Resume","smalltile");

                    MainActivity.smalltile_banner.setVisibility(View.INVISIBLE);


                    return true;
                }
                return false;
            }
        });
    }



}
