package net.babiran.app;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Handlers.DatabaseHandler;
import tools.AppConfig;

import static tools.AppConfig.id;

public class SMSReceiver extends BroadcastReceiver {
    private static String random_From_Main;

    String phone;


    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        //   Toast.makeText(context.getApplicationContext(), "xcvxfgnjdtghj", Toast.LENGTH_LONG).show();

        if (action.equals("my.action.string")) {
            final String mm = intent.getExtras().getString("extra");
            phone = intent.getExtras().getString("phone");
            //Toast.makeText(context.getApplicationContext(), mm +".........", Toast.LENGTH_LONG).show();
            random_From_Main = mm;

        }

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    if (senderNum.contains("98300077")) {
                        //Toast.makeText(context,"OK",Toast.LENGTH_SHORT).show();
                        // TEst(context);
                        // TESTvR(context);
                        AppConfig.code = "codeok";
                    }

                    // Show Alert


                } // end for loop

            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }


    private void TEst(final Context context) {


        final RequestQueue queue;
        AppConfig.checkReciveSms = true;
        //Volley Start
        queue = Volley.newRequestQueue(context);
        final ProgressDialog d = new ProgressDialog(context);
        d.setMessage("چند لحظه صبرکنید ...");
        d.setIndeterminate(true);
        d.setCancelable(false);
        d.show();
        String url = AppConfig.BASE_URL + "api/user/insertNewUser";
        // Request a string response from the provided URL.
        Log.e("request", "start");
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {

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

                                    DatabaseHandler db = new DatabaseHandler(context);

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
                                    context.startActivity(new Intent(context, MainActivity.class));


                                } catch (Exception ex) {
                                    DatabaseHandler db = new DatabaseHandler(context);
                                    db.addUser(id, phone, AppConfig.token);
                                    Log.e("tt1", db.getUserDetails().get("id"));

                                    AppConfig.error(ex);
                                    context.startActivity(new Intent(context, MainActivity.class));
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
                new com.android.volley.Response.ErrorListener() {
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

                return params;
            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(
                400000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        strRequest.setTag("TAG");
        // Add the request to the RequestQueue.
        queue.add(strRequest);
        //Volley End

    }


}