package net.babiran.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

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

import java.util.HashMap;
import java.util.Map;

import Handlers.DatabaseHandler;
import tools.AppConfig;


public class EditProfileActivity extends AppCompatActivity {

    private RequestQueue queue;
    public static final String TAG = "TAG";

    private boolean ok = true;
    private boolean ok_site = true;
    private EditText ed_name, ed_email, ed_address, ed_address_two, ed_phone_two,ed_phone;
    private TextView txt_email_error;
    private String phone;
    public static String prev_edit = "";
    private AppCompatImageView btn_back;

    public EditProfileActivity() {

    }

    public EditProfileActivity(String prev) {
        this.prev_edit = prev;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_editprofile_frgment);

        ed_name = (EditText) findViewById(R.id.name);
        ed_email = (EditText) findViewById(R.id.email);
        ed_address = (EditText) findViewById(R.id.address);
        ed_address_two = (EditText) findViewById(R.id.addressTwo);
        ed_phone_two = (EditText) findViewById(R.id.phoneTwo);
        ed_phone = (EditText) findViewById(R.id.phone);
        btn_back = findViewById(R.id.btn_back);


        LinearLayout main = (LinearLayout) findViewById(R.id.main_layout);
        final Animation j = AnimationUtils.loadAnimation(EditProfileActivity.this, R.anim.fadein);
        j.setStartOffset(0);
        j.setDuration(300);
        j.setFillAfter(true);
        main.startAnimation(j);

        SharedPreferences load = EditProfileActivity.this.getSharedPreferences("NUMBER", 0);
        String Phone = load.getString("TKN", "");

        Log.e("Phone", Phone);
        //get user details from local db

        DatabaseHandler db = new DatabaseHandler(EditProfileActivity.this);
        if (db.getRowCount() > 0) {
            HashMap<String, String> userDetailsHashMap = db.getUserDetails();


            String id = userDetailsHashMap.get("id");
            phone = userDetailsHashMap.get("phone1");
            String phoneTwo = userDetailsHashMap.get("phone2");
            String name = userDetailsHashMap.get("name");
            String email = userDetailsHashMap.get("email");
            String address = userDetailsHashMap.get("address");
            String addressTwo = userDetailsHashMap.get("address2");

            if (phone != null && phone.length() > 0 && !(phone.equals("null"))) {
                ed_phone.setText(phone);
            }
            if (phoneTwo != null && phoneTwo.length() > 0 && !(phoneTwo.equals("null"))) {
                ed_phone_two.setText(phoneTwo);
            }
            if (name != null && name.length() > 0 && !(name.equals("null"))) {
                ed_name.setText(name);
            }
            if (email != null && email.length() > 0 && !(email.equals("null"))) {
                ed_email.setText(email);
            }
            if (address != null && address.length() > 0 && !(address.equals("null"))) {
                ed_address.setText(address);
            }
            if (addressTwo != null && addressTwo.length() > 0 && !(addressTwo.equals("null"))) {
                ed_address_two.setText(addressTwo);
            }

        }

        
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   submit();
                if (ok) {
                    Log.d("here", "email ok");
                    submit();

                }

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MainActivity.btnBack.setVisibility(View.INVISIBLE);
                MainActivity.viewLogo.setVisibility(View.VISIBLE);
                MainActivity.layout_search.setVisibility(View.VISIBLE);
                MainActivity.home.setVisibility(View.VISIBLE);
            }
        });

    }


    public void submit() {
        //Volley Start
        queue = Volley.newRequestQueue(EditProfileActivity.this);

        final DatabaseHandler db = new DatabaseHandler(EditProfileActivity.this);
        HashMap<String, String> user = db.getUserDetails();
        final String id = user.get("id").toString();

        final ProgressDialog d = new ProgressDialog(EditProfileActivity.this);
        d.setMessage("در حال ثبت اطلاعات ...");
        d.setIndeterminate(true);
        d.setCancelable(false);
        d.show();

        Log.e("id", id);
        String url = AppConfig.BASE_URL + "api/user/updateAUser/" + id;
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        d.dismiss();
                        Log.i("response", response.toString());
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getString("success").equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("user");
                                JSONObject edit = jsonArray.getJSONObject(0);


                                Log.d("jsonarray", edit + "");


                                DatabaseHandler db = new DatabaseHandler(EditProfileActivity.this);
                                db.update(id, edit.getString("name"), edit.getString("email"), edit.getString("address"), edit.getString("address2"), "", edit.getString("phone1"),
                                        edit.getString("phone2"));

                                HashMap<String, String> userDetailsHashMap = db.getUserDetails();

                                String id = userDetailsHashMap.get("id");
                                String phone = userDetailsHashMap.get("phone1");
                                String phoneTwo = userDetailsHashMap.get("phone2");
                                String name = userDetailsHashMap.get("name");
                                String email = userDetailsHashMap.get("email");
                                String address = userDetailsHashMap.get("address");
                                String addressTwo = userDetailsHashMap.get("address2");


                                if (!(phoneTwo.equals("null")) && !(phoneTwo.equals(""))) {
                                    ed_phone_two.setText(phoneTwo);
                                }

                                if (!(name.equals("null")) && !(name.equals(""))) {
                                    ed_name.setText(name);
                                    MainActivity.nameHeaderTxt.setText(name);
                                } else {
                                    MainActivity.nameHeaderTxt.setText("");
                                }

                                if (!(email.equals("null")) && !(email.equals(""))) {
                                    ed_email.setText(email);
                                }
                                if (!(address.equals("null")) && !(address.equals(""))) {
                                    ed_address.setText(address);
                                }
                                if (!(addressTwo.equals("null")) && !(addressTwo.equals(""))) {
                                    ed_address_two.setText(addressTwo);
                                }
                                AlertDialog alertDialog = new AlertDialog.Builder(EditProfileActivity.this).create();
                                alertDialog.setTitle("پروفایل با موفقیت ثبت شد");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "باشه",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                //MainActivity.home.setVisibility(View.VISIBLE);
                                                //AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();      BasketListFragment.needToRefrish = true;

                                            }
                                        });
                                alertDialog.show();

                            }
                            //Toast.makeText(getApplicationContext(), json.getString("id"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            d.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        d.dismiss();
                        //Toast.makeText(EditProfileActivity.this, error.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (ed_name.getText().length() > 0) {
                    params.put("name", ed_name.getText().toString());
                } else {
                    params.put("name", "KmD3487f83nDFrm448Fp03Az4wl4F1sPPwkm38dGdek5km");

                }
                if (ed_email.getText().length() > 0) {

                    params.put("email", ed_email.getText().toString() + "");
                } else {
                    params.put("email", "KmD3487f83nDFrm448Fp03Az4wl4F1sPPwkm38dGdek5km");

                }

                if (ed_address.getText().length() > 0) {

                    params.put("address", ed_address.getText().toString() + "");
                } else {
                    params.put("address", "KmD3487f83nDFrm448Fp03Az4wl4F1sPPwkm38dGdek5km");

                }
                if (ed_address_two.getText().length() > 0) {

                    params.put("address2", ed_address_two.getText().toString() + "");
                } else {
                    params.put("address2", "KmD3487f83nDFrm448Fp03Az4wl4F1sPPwkm38dGdek5km");

                }

                if (ed_phone_two.getText().length() > 0) {

                    params.put("phone2", ed_phone_two.getText().toString() + "");
                } else {
                    params.put("phone2", "KmD3487f83nDFrm448Fp03Az4wl4F1sPPwkm38dGdek5km");
                }
                params.put("address3", "KmD3487f83nDFrm448Fp03Az4wl4F1sPPwkm38dGdek5km");

                return params;
            }
        };

        strRequest.setTag(TAG);
        strRequest.setRetryPolicy(new DefaultRetryPolicy(
                400000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(strRequest);
        //Volley End
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        MainActivity.btnBack.setVisibility(View.INVISIBLE);
        MainActivity.viewLogo.setVisibility(View.VISIBLE);
        MainActivity.layout_search.setVisibility(View.VISIBLE);
        MainActivity.home.setVisibility(View.VISIBLE);
    }
}
