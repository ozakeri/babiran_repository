package Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
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

import net.babiran.app.FactorList;
import net.babiran.app.MainActivity;
import net.babiran.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Handlers.DatabaseHandler;
import Models.Product;
import tools.AppConfig;
import tools.AudioRecorder;
import ui_elements.MyTextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mohammad on 7/26/2017.
 */

@SuppressLint("ValidFragment")
public class DescriptionDialog extends DialogFragment {

    View v;
    RequestQueue queue;
    AppCompatEditText edt_description;
    RelativeLayout progressbar;
    RelativeLayout submit;
    String id, address, productArray, selected_Pay;
    CardView fori, yeksaat, dosaat, yekrooz, sayer;
    CheckBox forichck, yeksaatchck, dosaatchck, yekroozchck, sayerchck;
    SharedPreferences.Editor editor;
    Context context;
    String descriptionFactor = "";
    private int credit;
    private DatabaseHandler db;
    private String timeId;

    AudioRecorder audioRecorder;
    public static final int REQUEST_CODE_PAY = 1001;

    public DescriptionDialog(Context context, String id, String address, String productsArray, String selected_pay, int credit, SharedPreferences.Editor editor, String timeId) {
        this.id = id;
        this.address = address;
        this.productArray = productsArray;
        this.selected_Pay = selected_pay;
        this.editor = editor;
        this.context = context;
        this.credit = credit;
        this.timeId = timeId;
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
        edt_description = v.findViewById(R.id.edt_description);
        edt_description.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));

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
                Log.e("selected_Pay==", selected_Pay);
                completeBUY(id, address, productArray, selected_Pay, credit, edt_description.getText().toString(), timeId);
                //DescriptionDialog descriptionDialog = new DescriptionDialog();
                //descriptionDialog.dismiss();
            }
        });

        return v;

    }

    public void completeBUY(final String user_id, final String address, final String productArray, final String selected_Pay, final int credit, final String description, String timeId) {
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

                            if (jsonObject.getString("success").equals("1")) {
                                if (!jsonObject.isNull("url")) {
                                    String urlStr = jsonObject.getString("url");
                                    getDialog().dismiss();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlStr));
                                    startActivityForResult(browserIntent, REQUEST_CODE_PAY);
                                    NullBasket();

                                } else if (!jsonObject.isNull("result")) {
                                    showGuideDialog();
                                }
                            } else if (jsonObject.getString("success").equals("2")) {
                                d.dismiss();
                                if (!jsonObject.isNull("message")) {
                                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                }

                            } else if (jsonObject.getString("success").equals("3")) {
                                d.dismiss();
                                Toast.makeText(getActivity(), "در خواست شما با موفقیت ثبت شد", Toast.LENGTH_LONG).show();
                                getFactorId();
                                getDialog().dismiss();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error != null && !TextUtils.isEmpty(error.getMessage()) ? error.getMessage() : "خطای سرور رخ داده است. لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG)
                        .show();
                d.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", user_id);
                params.put("description", description);
                params.put("address", address);
                params.put("selected_pay", selected_Pay);
                params.put("credit", String.valueOf(credit));
                params.put("productsArray", productArray);
                //params.put("productsArray", "[{\"count\":\"10\",\"product_id\":\"2826\"},{\"count\":\"2\",\"product_id\":\"1336\"},{\"count\":\"1\",\"product_id\":\"4586\"},{\"count\":\"1\",\"product_id\":\"4663\"}]");
                params.put("timeId", timeId);

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


            } else {

                NullBasket();

            }
        }
    }

    public void NullBasket() {

        MainActivity.basketlist.setVisibility(View.GONE);
        SharedPreferences pro_prefs;
        pro_prefs = getActivity().getSharedPreferences("productsArray", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pro_prefs.getString("products", "");
        ArrayList<Product> arrayList = gson.fromJson(json, new TypeToken<List<Product>>() {
        }.getType());
        SharedPreferences.Editor editor = context.getSharedPreferences("productsArray", MODE_PRIVATE).edit();
        arrayList.removeAll(arrayList);
        AppConfig.products = arrayList;

        try {
            Gson gson1 = new Gson();
            String proObj = gson1.toJson(AppConfig.products);

            editor.putString("products", proObj);
            editor.commit();

            AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();
        } catch (Exception e) {
            e.getMessage();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getFactorId() {

        NullBasket();


        db = new DatabaseHandler(context);
        if (db.getRowCount() > 0) {
            HashMap<String, String> userDetailsHashMap = db.getUserDetails();
            String id = userDetailsHashMap.get("id");
            Intent intent1 = new Intent(context, FactorList.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("id", id);
            startActivity(intent1);
        }
    }

    public void showGuideDialog() {
        final Dialog alert = new Dialog(getActivity());
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.custom_dialog_after_pay);
        TextView buttonTextView = alert.findViewById(R.id.txt_action);
        MyTextView txt_status = alert.findViewById(R.id.txt_status);
        AppCompatImageView statusIcon = alert.findViewById(R.id.statusIcon);

        txt_status.setText("در خواست شما با موفقیت ثبت شد");

        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                alert.dismiss();
                getFactorId();
                getDialog().dismiss();

            }
        });
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}
