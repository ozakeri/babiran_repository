package Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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

import java.util.HashMap;
import java.util.Map;

import Handlers.DatabaseHandler;
import tools.AppConfig;


public class EditProfileFrgment extends Fragment {

    private View v;
    private RequestQueue queue;
    public static final String TAG = "TAG";

    private boolean ok = true;
    private boolean ok_site = true;
    private EditText ed_name, ed_email, ed_address, ed_address_two, ed_phone, ed_phone_two;
    private TextView txt_email_error;
    private String phone;
    public static String prev_edit = "";


    public EditProfileFrgment() {

    }

    public EditProfileFrgment(String prev) {
        this.prev_edit = prev;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_editprofile_frgment, container, false);

        MainActivity.edit.setVisibility(View.VISIBLE);
        txt_email_error = (TextView) v.findViewById(R.id.email_error);
        ed_name = (EditText) v.findViewById(R.id.name);
        ed_email = (EditText) v.findViewById(R.id.email);
        ed_address = (EditText) v.findViewById(R.id.address);
        ed_address_two = (EditText) v.findViewById(R.id.addressTwo);
        ed_phone = (EditText) v.findViewById(R.id.phone);
        ed_phone_two = (EditText) v.findViewById(R.id.phoneTwo);


        LinearLayout main = (LinearLayout) v.findViewById(R.id.main_layout);
        final Animation j = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        j.setStartOffset(0);
        j.setDuration(300);
        j.setFillAfter(true);
        main.startAnimation(j);

        SharedPreferences load = getActivity().getSharedPreferences("NUMBER", 0);
        String Phone = load.getString("TKN", "");

        Log.e("Phone", Phone);
        //get user details from local db

        DatabaseHandler db = new DatabaseHandler(getActivity());
        if (db.getRowCount() > 0) {
            HashMap<String, String> userDetailsHashMap = db.getUserDetails();


            String id = userDetailsHashMap.get("id");
            phone = userDetailsHashMap.get("phone1");
            String phoneTwo = userDetailsHashMap.get("phone2");
            String name = userDetailsHashMap.get("name");
            String email = userDetailsHashMap.get("email");
            String address = userDetailsHashMap.get("address");
            String addressTwo = userDetailsHashMap.get("address2");

            if (!Phone.equals("")) {
                ed_phone.setText(Phone);
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


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        ed_email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                String email = ed_email.getText().toString().trim();
                if (email.matches(emailPattern) && s.length() > 0) {
                    //Toast.makeText(getActivity(),"valid email address",Toast.LENGTH_SHORT).show();
                    // or
                    txt_email_error.setVisibility(View.INVISIBLE);
                    ok = true;


                    //textView.setText("valid email");
                } else if (!email.matches(emailPattern) && s.length() > 0) {
                    //Toast.makeText(getActivity(),"Invalid email address",Toast.LENGTH_SHORT).show();
                    //or
                    txt_email_error.setVisibility(View.VISIBLE);
                    ok = false;

                    //textView.setText("invalid email");
                } else {
                    txt_email_error.setVisibility(View.INVISIBLE);
                    ok = true;

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });


        v.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   submit();
                if (ok) {
                    Log.d("here", "email ok");
                    submit();

                }

            }
        });


        return v;


    }


    public void submit() {
        //Volley Start
        queue = Volley.newRequestQueue(getActivity());

        final DatabaseHandler db = new DatabaseHandler(getActivity());
        HashMap<String, String> user = db.getUserDetails();
        final String id = user.get("id").toString();

        final ProgressDialog d = new ProgressDialog(getActivity());
        d.setMessage("در حال ثبت اطلاعات ...");
        d.setIndeterminate(true);
        d.setCancelable(false);
        d.show();

        Log.e("id", id);
        String url = AppConfig.BASE_URL + "api/user/updateAUser/" + id;
        // Request a string response from the provided URL.
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


                                DatabaseHandler db = new DatabaseHandler(getActivity());
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


                                if (!(phone.equals("null")) && !(phone.equals(""))) {
                                    ed_phone.setText(phone);
                                }
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
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                alertDialog.setTitle("پروفایل با موفقیت ثبت شد");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "باشه",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                MainActivity.edit.setVisibility(View.INVISIBLE);
                                                MainActivity.home.setVisibility(View.VISIBLE);
                                                BasketListFragment.needToRefrish = true;
                                                AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();
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
                        //Toast.makeText(getActivity(), error.getMessage()+"", Toast.LENGTH_SHORT).show();
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
                if (ed_phone.getText().length() > 0) {

                    params.put("phone1", ed_phone.getText().toString() + "");
                } else {
                    params.put("phone1", "KmD3487f83nDFrm448Fp03Az4wl4F1sPPwkm38dGdek5km");
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
    public void onResume() {
        super.onResume();

        if (queue != null) {
            queue.cancelAll(TAG);
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    MainActivity.edit.setVisibility(View.INVISIBLE);

                    // AppConfig.fragmentManager.beginTransaction().replace(R.id.container,new HomeFrgment()).commit();

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

}
