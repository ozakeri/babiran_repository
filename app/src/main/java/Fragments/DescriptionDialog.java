package Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.babiran.app.BlankAcct;
import net.babiran.app.R;
import net.babiran.app.commnets.UNIQ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Product;
import tools.AppConfig;
import tools.AudioRecorder;
import ui_elements.MyEditText;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mohammad on 7/26/2017.
 */

@SuppressLint("ValidFragment")
public class DescriptionDialog extends DialogFragment {

    View v;
    RequestQueue queue;

    MyEditText description;
    RelativeLayout progressbar;

    RelativeLayout submit;
    String id, address, productArray, selected_Pay;

    CardView fori, yeksaat, dosaat, yekrooz, sayer;

    CheckBox forichck, yeksaatchck, dosaatchck, yekroozchck, sayerchck;

    SharedPreferences.Editor editor;
    Context context;

    String descriptionFactor = "";

    AudioRecorder audioRecorder;
    public static final int REQUEST_CODE_PAY = 1001;

    public DescriptionDialog(Context context, String id, String address, String productsArray, String selected_pay, SharedPreferences.Editor editor) {
        this.id = id;
        this.address = address;
        this.productArray = productsArray;
        this.selected_Pay = selected_pay;
        this.editor = editor;
        this.context = context;
    }

    public DescriptionDialog() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        v = inflater.inflate(R.layout.description_dialog, container, false);
        getDialog().setTitle("DescriptionDialog");
        // description = (MyEditText) v.findViewById(R.id.description_main);

        fori = (CardView) v.findViewById(R.id.fori);
        yekrooz = (CardView) v.findViewById(R.id.yekrooz);
        dosaat = (CardView) v.findViewById(R.id.dosaat);
        yeksaat = (CardView) v.findViewById(R.id.yeksaat);
        sayer = (CardView) v.findViewById(R.id.sayer);

        forichck = (CheckBox) v.findViewById(R.id.foricheck);
        yekroozchck = (CheckBox) v.findViewById(R.id.yekroozcheck);
        yeksaatchck = (CheckBox) v.findViewById(R.id.yeksaatcheck);
        dosaatchck = (CheckBox) v.findViewById(R.id.dosaatcheck);
        sayerchck = (CheckBox) v.findViewById(R.id.sayercheck);

        fori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forichck.isChecked()) {
                    forichck.setChecked(false);
                } else if (!forichck.isChecked()) {
                    forichck.setChecked(true);
                    yeksaatchck.setChecked(false);
                    yekroozchck.setChecked(false);
                    dosaatchck.setChecked(false);
                    sayerchck.setChecked(false);
                }
            }
        });

        yekrooz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yekroozchck.isChecked()) {
                    yekroozchck.setChecked(false);

                } else if (!yekroozchck.isChecked()) {
                    yekroozchck.setChecked(true);
                    forichck.setChecked(false);
                    yeksaatchck.setChecked(false);
                    dosaatchck.setChecked(false);
                    sayerchck.setChecked(false);
                }
            }
        });

        yeksaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yeksaatchck.isChecked()) {
                    yeksaatchck.setChecked(false);

                } else if (!yeksaatchck.isChecked()) {
                    yeksaatchck.setChecked(true);
                    forichck.setChecked(false);
                    yekroozchck.setChecked(false);
                    dosaatchck.setChecked(false);
                    sayerchck.setChecked(false);
                }
            }
        });

        dosaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dosaatchck.isChecked()) {
                    dosaatchck.setChecked(false);
                } else if (!dosaatchck.isChecked()) {
                    dosaatchck.setChecked(true);
                    yekroozchck.setChecked(false);
                    yeksaatchck.setChecked(false);
                    sayerchck.setChecked(false);
                    forichck.setChecked(false);
                }
            }
        });

        sayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sayerchck.isChecked()) {
                    sayerchck.setChecked(false);
                } else if (!sayerchck.isChecked()) {
                    sayerchck.setChecked(true);
                    forichck.setChecked(false);
                    yekroozchck.setChecked(false);
                    yeksaatchck.setChecked(false);
                    dosaatchck.setChecked(false);
                }

            }
        });


        submit = (RelativeLayout) v.findViewById(R.id.submitDes);
        progressbar = (RelativeLayout) v.findViewById(R.id.progressLayout);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forichck.isChecked()) {
                    descriptionFactor = "فوری";
                } else if (yekroozchck.isChecked()) {
                    descriptionFactor = "یک روز";
                } else if (yeksaatchck.isChecked()) {
                    descriptionFactor = "یک ساعت";
                } else if (dosaatchck.isChecked()) {
                    descriptionFactor = "دو ساعت";
                } else if (sayerchck.isChecked()) {
                    descriptionFactor = "سایر";
                }
                Log.e("descript", descriptionFactor);
                completeBUY(id, address, productArray, selected_Pay, "");
                //DescriptionDialog descriptionDialog = new DescriptionDialog();
                //descriptionDialog.dismiss();
                //getDialog().dismiss();

            }
        });

        return v;

    }

    public void completeBUY(final String user_id, final String address, final String productArray, final String selected_Pay, final String description) {
        queue = Volley.newRequestQueue(getActivity());
//        getDialog().dismiss();
        final ProgressDialog d = new ProgressDialog(getActivity());
        d.setMessage("چند لحظه صبرکنید ...");
        d.setIndeterminate(true);
        d.setCancelable(false);
        d.show();

        // progressbar.setVisibility(View.VISIBLE);
        final String url = "http://babiran.net/api/order/addToBasketMain";
        // Request a string response from the provided URL.

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        d.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("~!@~!@", jsonObject.toString());
                            if (jsonObject.getString("success").equals("1")) {

//                                ArrayList<Product> productArrayList = new ArrayList<>();
//                                Gson gson = new Gson();
//                                String proObj = gson.toJson(productArrayList);
//                                editor.putString("products", proObj);
//                                editor.commit();

                                //Intent intent = new Intent(context, ActivityPay.class);
                                //intent.putExtra("url", jsonObject.getString("url"));
                                // startActivityForResult(intent, REQUEST_CODE_PAY);
//                                AppConfig.products.clear();
//                                MainActivity.basketlist.setVisibility(View.INVISIBLE);
//                                SharedPreferences.Editor edit = context.getSharedPreferences("factor", MODE_PRIVATE).edit();
//                                edit.putString("factor_id", jsonObject.getString("factor_id"));
//                                edit.putString("motor", "active");
//                                long time = System.currentTimeMillis();
//                                edit.putLong("currentTime", time);
//                                edit.commit();
//                                MainActivity.factorcontainer.setVisibility(View.VISIBLE);
//                                AppConfig.fragmentManager.beginTransaction().replace(R.id.Factorcontainer, new FactorFragment()).commit();

                                System.out.println("url-=-=-=-=-=-" + url);
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(jsonObject.getString("url")));
                                startActivity(browserIntent);
                                getDialog().dismiss();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error=====" + error);
                Log.e("Volley", error.toString());
                Toast.makeText(context, error != null && !TextUtils.isEmpty(error.getMessage()) ? error.getMessage() : "خطای سرور رخ داده است. لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG)
                        .show();
                d.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();

               /* params.put("price",String.valueOf(TotalPrice));
                params.put("user_id",id);
                params.put("payment_id",selectedPay);
                params.put("address",selectedAdd);
                params.put("price_under_discount",String.valueOf(rawPrice));*/
                //  Log.e("factorJSON:",factorJson);
                params.put("user_id", user_id);
                params.put("description", description);
                params.put("address", address);
                params.put("selected_pay", selected_Pay);
                params.put("productsArray", productArray);

                System.out.println("user_id==" + user_id);
                System.out.println("description==" + description);
                System.out.println("address==" + address);
                System.out.println("selected_Pay==" + selected_Pay);
                System.out.println("productArray==" + productArray);

                return params;
            }


            /* @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 Map<String, String>  params = new HashMap<String, String>();
                 params.put("Content-Type", "application/json");

                 return params;
             }*/
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                params.put("Content-Type", "application/json");
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAY) {
            if (resultCode == Activity.RESULT_OK) {

                NullBasket();
                getDialog().dismiss();
                AppConfig.NULLBASKET = UNIQ.BASKET_NULL;
                Intent intent = new Intent(getActivity(), BlankAcct.class);
                startActivity(intent);

            } else {

                NullBasket();
                getDialog().dismiss();
                AppConfig.NULLBASKET = UNIQ.BASKET_NULL;
                Intent intent = new Intent(getActivity(), BlankAcct.class);
                startActivity(intent);


            }
        }
    }

    public void NullBasket() {

        final SharedPreferences.Editor editor = getActivity().getSharedPreferences("productsArray", MODE_PRIVATE).edit();
        editor.putString("products", "");
        editor.apply();


        SharedPreferences pro_prefs;
        pro_prefs = getActivity().getSharedPreferences("productsArray", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = pro_prefs.getString("products", "");
        ArrayList<Product> obj = gson.fromJson(json, new TypeToken<List<Product>>() {
        }.getType());
        Log.e("objGson", obj + "");

        AppConfig.products = obj;

        //  getSupportFragmentManager().beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("====dialog onResume====");
    }
}
