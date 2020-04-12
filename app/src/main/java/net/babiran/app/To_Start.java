package net.babiran.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Handlers.DatabaseHandler;
import co.ronash.pushe.Pushe;
import tools.AppConfig;

import static tools.AppConfig.id;

public class To_Start extends AppCompatActivity {

    private RelativeLayout NotValidButton;
    private RelativeLayout btnNotFalid;
    private GoogleCloudMessaging gcmObj;

    private String RegId, TOKEN;

    EditText random_end;

    RelativeLayout btnTo;
    RequestQueue queue;
    EditText ed1;
    public static final String TAG = "TAG";
    private String random_Datm;
    // OSPermissionSubscriptionState status;
    String message, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to__start);


//         status = OneSignal.getPermissionSubscriptionState();
//        status.getSubscriptionStatus().getUserId();
        NotValidButton = (RelativeLayout) findViewById(R.id.not_valid);
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                if (AppConfig.checkReciveSms == false) {

                    Log.e("Timer", "fin");
                    NotValidButton.setVisibility(View.VISIBLE);
                    //return;
                }

            }
        }.start();

        btnTo = (RelativeLayout) findViewById(R.id.button);
        btnNotFalid = (RelativeLayout) findViewById(R.id.BTNNotValidation);


        if (AppConfig.phone != null) {

            phone = AppConfig.phone;

        }

        //btnTo.setVisibility(View.INVISIBLE);
        ed1 = (EditText) findViewById(R.id.edt_start);
        final Handler handler = new Handler();

        Thread someThread = new Thread() {

            @Override
            public void run() {

                //some actions
                while (true) {
                    //        Log.d("app config code:", AppConfig.code);
                    if (AppConfig.code.length() > 3) {

                        handler.post(new Runnable() {
                            public void run() {

                                Log.i("codeeeee", AppConfig.code);
                                ed1.setText(AppConfig.code);
                                //submit();
                                Sumbit2();
                                AppConfig.checkReciveSms = true;

                                //btnTo.setVisibility(View.VISIBLE);
                                //findViewById(R.id.btt).setVisibility(View.VISIBLE);
                            }
                        });

                        break;

                    } //wait for condition
                    //some actions
                }

            }


        };

        someThread.start();
        Log.d("sms in to start ", ed1.getText().toString());


        btnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //btnNotFalid.setEnabled(false);
                submit();

            }
        });


        btnNotFalid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //findViewById(R.id.btt).setVisibility(View.GONE);
                if (AppConfig.isEnter) {
                    finish();
                } else {
                    findViewById(R.id.btt).setEnabled(false);
                    startActivity(new Intent(To_Start.this, MainActivity.class));
                    finish();
                }
            }
        });

    }


    public void submit() {

        Log.d("check receive :", AppConfig.checkReciveSms + "");
        Log.d("sent :", AppConfig.sent + "");
        //Toast.makeText(getApplicationContext(), AppConfig.token, Toast.LENGTH_SHORT).show();
        if (ed1.getText().toString().equals(AppConfig.sent) && AppConfig.sent.toString() != null) {

            try {

                findViewById(R.id.not_valid).setVisibility(View.GONE);
                findViewById(R.id.progressLayout).setVisibility(View.GONE);
                AppConfig.checkReciveSms = true;
                //Volley Start
                queue = Volley.newRequestQueue(getBaseContext());
                final ProgressDialog d = new ProgressDialog(To_Start.this);
                d.setMessage("چند لحظه صبرکنید ...");
                d.setIndeterminate(true);
                d.setCancelable(false);
                d.show();
                String url = AppConfig.BASE_URL + "api/user/insertNewUser";
                // Request a string response from the provided URL.
                Log.e("request", "start");
                StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                d.dismiss();
                                try {

                                    Log.d("response:", response);
                                    JSONObject json = new JSONObject(response);
                                    Log.d("success", json.getString("success"));
                                    if (json.getString("success").equals("1")) {

                                        Log.d("here", "iM here");
                                        try {
                                            JSONObject user = json.getJSONObject("user");
                                            Log.d("user data:", user + "");

                                            Log.e("request", "finish");

                                            DatabaseHandler db = new DatabaseHandler(getBaseContext());

                                            db.addUser(user.getString("id"),
                                                    user.getString("name"),
                                                    user.getString("email"),
                                                    user.getString("address"),
                                                    user.getString("address2"),
                                                    user.getString("address3"),
                                                    user.getString("phone1"),
                                                    user.getInt("balance"), " ");

                                            //findViewById(R.id.progressLayout).setVisibility(View.GONE);
                                            AppConfig.btnSubmitOk = true;

                                            Log.e("tt1", db.getUserDetails().get("id"));
                                            if (AppConfig.isEnter) {
                                                finish();
                                            } else {
                                                startActivity(new Intent(To_Start.this, MainActivity.class));
                                                finish();
                                            }

                                        } catch (Exception ex) {
                                            DatabaseHandler db = new DatabaseHandler(getBaseContext());
                                            db.addUser(id, phone, AppConfig.token);
                                            Log.e("tt1", db.getUserDetails().get("id"));

                                            AppConfig.error(ex);

                                            if (AppConfig.isEnter) {
                                                finish();
                                            } else {
                                                startActivity(new Intent(To_Start.this, MainActivity.class));
                                                finish();
                                            }
                                            //AppConfig.end(To_Start.this);
                                        }


                                        //Toast.makeText(getApplicationContext(), sharedpreferences.getString("city_id","999"), Toast.LENGTH_SHORT).show();
                                    }
                                    //Toast.makeText(getApplicationContext(), json.getString("id"), Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    AppConfig.error(e);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(getApplicationContext(), error.toString() + "", Toast.LENGTH_SHORT).show();
                                AppConfig.error(error);
                                queue.cancelAll(this);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("phone1", phone);
                        Log.e("toooooook", AppConfig.token);
                        params.put("code", " ");

//                       params.put("reg_id", status.getSubscriptionStatus().getUserId());
                        //   params.put("reg_id",RegId);
                        params.put("reg_id", Pushe.getPusheId(To_Start.this));
                        return params;
                    }
                };
                strRequest.setRetryPolicy(new DefaultRetryPolicy(
                        400000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                strRequest.setTag(TAG);
                // Add the request to the RequestQueue.
                queue.add(strRequest);
                //Volley End
            } catch (Exception ex) {

                AppConfig.error(ex);

            }
        } else {
            Toast.makeText(getBaseContext(), "کد صحیح نمی باشد", Toast.LENGTH_SHORT).show();
        }
    }

    private void Sumbit2() {

        try {

            findViewById(R.id.not_valid).setVisibility(View.GONE);
            findViewById(R.id.progressLayout).setVisibility(View.GONE);
            AppConfig.checkReciveSms = true;
            //Volley Start
            queue = Volley.newRequestQueue(getBaseContext());
            final ProgressDialog d = new ProgressDialog(To_Start.this);
            d.setMessage("چند لحظه صبرکنید ...");
            d.setIndeterminate(true);
            d.setCancelable(false);
            d.show();
            String url = AppConfig.BASE_URL + "api/user/insertNewUser";
            // Request a string response from the provided URL.
            Log.e("request", "start");
            StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            d.dismiss();
                            try {

                                Log.d("response:", response);
                                JSONObject json = new JSONObject(response);
                                Log.d("success", json.getString("success"));
                                if (json.getString("success").equals("1")) {

                                    Log.d("here", "iM here");
                                    try {
                                        JSONObject user = json.getJSONObject("user");
                                        Log.d("user data:", user + "");

                                        Log.e("request", "finish");

                                        DatabaseHandler db = new DatabaseHandler(getBaseContext());

                                        db.addUser(user.getString("id"),
                                                user.getString("name"),
                                                user.getString("email"),
                                                user.getString("address"),
                                                user.getString("address2"),
                                                user.getString("address3"),
                                                user.getString("phone1"),
                                                user.getInt("balance"), " ");

                                        //findViewById(R.id.progressLayout).setVisibility(View.GONE);
                                        AppConfig.btnSubmitOk = true;

                                        Log.e("tt1", db.getUserDetails().get("id"));
                                        if (AppConfig.isEnter) {
                                            finish();
                                        } else {
                                            startActivity(new Intent(To_Start.this, MainActivity.class));
                                            finish();
                                        }

                                    } catch (Exception ex) {
                                        DatabaseHandler db = new DatabaseHandler(getBaseContext());
                                        db.addUser(id, phone, AppConfig.token);
                                        Log.e("tt1", db.getUserDetails().get("id"));

                                        AppConfig.error(ex);

                                        if (AppConfig.isEnter) {
                                            finish();
                                        } else {
                                            startActivity(new Intent(To_Start.this, MainActivity.class));
                                            finish();
                                        }
                                        //AppConfig.end(To_Start.this);
                                    }


                                    //Toast.makeText(getApplicationContext(), sharedpreferences.getString("city_id","999"), Toast.LENGTH_SHORT).show();
                                }
                                //Toast.makeText(getApplicationContext(), json.getString("id"), Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                AppConfig.error(e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getApplicationContext(), error.toString() + "", Toast.LENGTH_SHORT).show();
                            AppConfig.error(error);
                            queue.cancelAll(this);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("phone1", phone);
                    Log.e("toooooook", AppConfig.token);
                    params.put("code", " ");
                    params.put("reg_id", Pushe.getPusheId(To_Start.this));
                    // params.put("reg_id",status.getSubscriptionStatus().getUserId());
                    return params;
                }
            };
            strRequest.setRetryPolicy(new DefaultRetryPolicy(
                    400000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            strRequest.setTag(TAG);
            // Add the request to the RequestQueue.
            queue.add(strRequest);
            //Volley End
        } catch (Exception ex) {

            AppConfig.error(ex);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

}
