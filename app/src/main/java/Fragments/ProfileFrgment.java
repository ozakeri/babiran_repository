package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

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

import Handlers.AdvertisingDatabaseHandler;
import Handlers.DatabaseHandler;
import Models.Advertising;
import Models.City;
import Models.Province;
import net.babiran.app.R;
import tools.AppConfig;



public class ProfileFrgment extends Fragment {

    View v;
    RequestQueue queue;
    public static final String TAG = "TAG";
    public ProfileFrgment() {

    }

    public City city;
    public Province province;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



            v = inflater.inflate(R.layout.basket_list_fragment, container, false);

            AppConfig.frag = ProfileFrgment.this;
            LinearLayout main = (LinearLayout) v.findViewById(R.id.main_layout);
            final Animation j = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
            j.setStartOffset(0);
            j.setDuration(300);
            j.setFillAfter(true);
            main.startAnimation(j);

            v.findViewById(R.id.favorites).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                        AdvertisingDatabaseHandler adb = new AdvertisingDatabaseHandler(getActivity());
                      //  AppConfig.fragmentManager.beginTransaction().replace(R.id.container, new AdvertisingListFrgment(adb.getAdvertisings(), "favorites")).commit();


                }
            });
            v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                      //  AppConfig.fragmentManager.beginTransaction().replace(R.id.container, new EditProfileActivity()).commit();



                }
            });
            v.findViewById(R.id.myCity).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                        ChooseProvinceFrgment getCat = new ChooseProvinceFrgment();
                        getCat.setTargetFragment(ProfileFrgment.this, 0);
                        getCat.show(AppConfig.fragmentManager, "getProvinceToFragmentProfile");
                    */
                }
            });

            v.findViewById(R.id.myAdsLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                        DatabaseHandler db = new DatabaseHandler(getActivity());
                        HashMap<String, String> user = db.getUserDetails();

                        String id = user.get("id").toString();

                      //  AppConfig.fragmentManager.beginTransaction().replace(R.id.container, new AdvertisingListFrgment("myAdvertisings", id, 1)).commit();

                }
            });


            v.findViewById(R.id.goosh).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                     //   AppConfig.fragmentManager.beginTransaction().replace(R.id.container, new GooshFrgment()).commit();

                }
            });

            v.findViewById(R.id.support).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                     //   AppConfig.fragmentManager.beginTransaction().replace(R.id.container, new TestFrgment()).commit();



                }
            });

            v.findViewById(R.id.rules).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                     //   AppConfig.fragmentManager.beginTransaction().replace(R.id.container, new RulesFrgment()).commit();



                }
            });

            v.findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                     //   AppConfig.fragmentManager.beginTransaction().replace(R.id.container, new AboutFrgment()).commit();


                }
            });

            v.findViewById(R.id.txt_return).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                        AppConfig.fragmentManager.beginTransaction().replace(R.id.Homecontainer, new HomeFragment()).commit();


                }
            });



            return v;

    }

    public void onUserSelectProvince(Province province) {
        // TODO add your implementation.
        //Toast.makeText(getActivity(),"oooops "+ province.name,Toast.LENGTH_SHORT).show();

        this.province = province;
        AppConfig.province_id = province.id;
        if( ! province.id.equals("32") && ! province.id.equals("35") && ! province.id.equals("36") && ! province.id.equals("37") && ! province.id.equals("38")) {


            /*
            ChooseCityFrgment getCat = new ChooseCityFrgment(AppConfig.province_id);
            getCat.setTargetFragment(ProfileFrgment.this, 0);
            getCat.show(AppConfig.fragmentManager, "getCityToFragmentProfile");
            */
        }else{
            AppConfig.province_id = province.id;
            searchProvince();

        }
        //txt_province.setText(province.name);
        //txt_delete_province.setVisibility(View.VISIBLE);
        //v.findViewById(R.id.city_layout).setVisibility(View.VISIBLE);

    }

    public void onUserSelectCity(City city) {
        // TODO add your implementation.
        //Toast.makeText(getActivity(),"oooops "+ city.name,Toast.LENGTH_SHORT).show();

        this.city = city;
        AppConfig.city_id = city.id;
      //  AppConfig.fragmentManager.beginTransaction().replace(R.id.container,new AdvertisingListFrgment("myCity",AppConfig.city_id,1)).commit();

        //txt_city.setText(city.name);
        //txt_delete_city.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        try {

            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener

                         //   AppConfig.fragmentManager.beginTransaction().replace(R.id.Homecontainer, new HomeFrgment()).commit();


                        return true;
                    }
                    return false;
                }
            });

        }

        catch (Exception ex)
        {

            AppConfig.error(ex);

        }
    }



    public void searchProvince(){
        //v.findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
        //Volley Start
        queue = Volley.newRequestQueue(getActivity());
        String url = AppConfig.BASE_URL+"api/advertising/search";
        // Request a string response from the provided URL.

       // Log.e("resss count",ed_product_count_from.getText().toString());

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("ressss",response.toString());
                        try {
                            JSONArray ca = new JSONArray(response);
                            Log.i("jsonarray",ca.toString());
                            ArrayList<Advertising> advertisings = new ArrayList<>();
                            for(int i=0;i<ca.length();i++){
                                JSONObject c = ca.getJSONObject(i);
                                advertisings.add( new Advertising(
                                        c.getString("id"),
                                        c.getString("company_id"),
                                        c.getString("category_id"),
                                        c.getString("name"),
                                        c.getString("visit"),
                                        c.getString("hoze"),
                                        c.getString("company_name"),
                                        c.getString("description"),
                                        c.getString("products"),
                                        c.getString("conditions"),
                                        c.getString("email"),
                                        c.getString("phone"),
                                        c.getString("email_public"),
                                        c.getString("phone_public"),
                                        c.getString("province_id"),
                                        c.getString("city_id"),
                                        c.getString("has_image"),
                                        c.getString("active"),
                                        c.getString("created_at"),
                                        c.getString("updated_at"),
                                        c.getString("img1"),
                                        c.getString("img2"),
                                        c.getString("img3"),
                                        c.getString("img4"),c.getString("address")
                                ));
                            }
                            if(advertisings.size()>0){
                                //v.findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);

                            //    AppConfig.fragmentManager.beginTransaction().replace(R.id.container,new AdvertisingSearchResultFrgment(advertisings,"profile")).commit();

                            }else{
                                //v.findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);
                                //Toast.makeText(getActivity(),"آگهی با ویژگی های مشخص شده پیدا نشد",Toast.LENGTH_SHORT).show();

                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                alertDialog.setTitle("آگهی با ویژگی های مشخص شده پیدا نشد");
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




                if(AppConfig.province_id !=null){
                    params.put("province_id", AppConfig.province_id);
                    Log.e("province_id",AppConfig.province_id);

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


    }




}
