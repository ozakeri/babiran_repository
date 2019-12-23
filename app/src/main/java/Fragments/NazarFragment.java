package Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tools.AppConfig;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mohammad on 6/13/2017.
 */


public class NazarFragment extends Fragment {

    View v;
    ImageView checkKheiliBad ,checkBad,checkKhoob,checkAwli ;
    LinearLayout LinAwli,LinKhoob,LinBad,LinKheilibad ;
    RelativeLayout send ;
    RequestQueue queue;

    String serviceQuality = "";
    String F_id = "" ;
    Boolean selected = false ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.nazarsanji, container, false);

        MainActivity.nazarsanji.setVisibility(View.VISIBLE);
        MainActivity.factorcontainer.setVisibility(View.INVISIBLE);

        checkKheiliBad = (ImageView) v.findViewById(R.id.kheilibad);
        checkBad = (ImageView) v.findViewById(R.id.bad);
        checkKhoob = (ImageView) v.findViewById(R.id.khoob);
        checkAwli = (ImageView) v.findViewById(R.id.awli);

        LinKheilibad = (LinearLayout) v.findViewById(R.id.LinearKheiliBad);
        LinBad = (LinearLayout) v.findViewById(R.id.LinearBad);
        LinKhoob = (LinearLayout) v.findViewById(R.id.LinearKhoob);
        LinAwli = (LinearLayout) v.findViewById(R.id.LinearAwli);

        SharedPreferences prefs = getActivity().getSharedPreferences("factor", MODE_PRIVATE);

        F_id = prefs.getString("factorID", "");


        send = (RelativeLayout) v.findViewById(R.id.ersal);

        LinKheilibad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkKheiliBad.setImageResource(R.drawable.select);
                checkBad.setImageResource(R.drawable.deselect);
                checkKhoob.setImageResource(R.drawable.deselect);
                checkAwli.setImageResource(R.drawable.deselect);
                selected = true ;
                serviceQuality = "0";
            }
        });

        LinBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkKheiliBad.setImageResource(R.drawable.deselect);
                checkBad.setImageResource(R.drawable.select);
                checkKhoob.setImageResource(R.drawable.deselect);
                checkAwli.setImageResource(R.drawable.deselect);
                selected = true ;
                serviceQuality = "1";
            }
        });

        LinKhoob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkKheiliBad.setImageResource(R.drawable.deselect);
                checkBad.setImageResource(R.drawable.deselect);
                checkKhoob.setImageResource(R.drawable.select);
                checkAwli.setImageResource(R.drawable.deselect);
                selected = true ;
                serviceQuality = "2";
            }
        });

        LinAwli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkKheiliBad.setImageResource(R.drawable.deselect);
                checkBad.setImageResource(R.drawable.deselect);
                checkKhoob.setImageResource(R.drawable.deselect);
                checkAwli.setImageResource(R.drawable.select);
                selected = true ;
                serviceQuality = "3";
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected){
                    Nazar();
                }
                else{
                    Toast.makeText(getActivity(),"لطفا نظر خود را نسبت به پیک اعلام نمایید ",Toast.LENGTH_LONG).show();
                }

            }
        });

        return v ;
    }

    public void Nazar(){

        queue = Volley.newRequestQueue(getActivity());
        final ProgressDialog d = new ProgressDialog(getActivity());
        d.setMessage("چند لحظه صبرکنید ...");
        d.setIndeterminate(true);
        d.setCancelable(false);
        d.show();

        String url = AppConfig.BASE_URL +"api/factor/nazarsanjiOfDevices";
        // Request a string response from the provided URL.

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        d.dismiss();
                        try {
                            JSONObject jsonObject =  new JSONObject(response);

                            if(jsonObject.getString("success").equals("1")){
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                alertDialog.setTitle("بابیران از حسن انتخاب شما کمال قدردانی را دارد");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "با تشکر",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                                MainActivity.nazarsanji.setVisibility(View.INVISIBLE);
                                                MainActivity.home.setVisibility(View.VISIBLE);
                                                BasketListFragment.needToRefrish = true ;
                                                AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();


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



                if(!serviceQuality.equals("")){
                    params.put("service_quality",serviceQuality);
                }
                if(!F_id.equals("")){
                    params.put("factor_id",F_id);
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
                    MainActivity.nazarsanji.setVisibility(View.INVISIBLE);


                    return true;
                }
                return false;
            }
        });
    }
}

