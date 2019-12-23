package Fragments;

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
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

import Handlers.DatabaseHandler;
import tools.AppConfig;
import ui_elements.MyEditText;
import ui_elements.TicketItem;


public class SupportFragment extends Fragment {


    View v;
    RequestQueue queue;

    ArrayList<TicketItem> ticketItemArrayList = new ArrayList<>();
    public boolean ok = true;

    MyEditText txt_message;
    LinearLayout main_layout;
    ScrollView scrollView;

    DatabaseHandler db ;
    public static final String TAG = "TAG";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.support, container, false);


        scrollView = (ScrollView)v.findViewById(R.id.sscroll);

        MainActivity.support.setVisibility(View.VISIBLE);


        v.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfig.fragmentManager.beginTransaction().replace(R.id.SupportContainer,new SupportFragment()).commit();

            }
        });



        main_layout = (LinearLayout)v.findViewById(R.id.main_layout_support);

        txt_message = (MyEditText) v.findViewById(R.id.message);

        LinearLayout main = (LinearLayout)v.findViewById(R.id.main_layout_support);
        final Animation j = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        j.setStartOffset(0);
        j.setDuration(300);
        j.setFillAfter(true);
        main.startAnimation(j);

        ticketItemArrayList = new ArrayList<>();


        TicketItem tt = new TicketItem(getActivity(),"تیکت های پشتیبانی سوالات و پیام های شما به ما و پاسخ های بابیران می باشند. تیکت ها به صورت لحظه ای نبوده و پشتیبانی بابیران بعد از مشاهده پاسخ خواهد داد. برای مشاهده پاسخ باید صفحه بروز رسانی شود.",0);
        main_layout.addView(tt);

        getTicket();

        scrollView.post(new Runnable() {
            @Override
            public void run() {

                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        v.findViewById(R.id.progressLayout).setVisibility(View.GONE);



        v.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submit();

            }
        });

        return v;
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

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener

                    MainActivity.support.setVisibility(View.INVISIBLE);


                    return true;
                }
                return false;
            }
        });
    }


    public void getTicket(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        db = new DatabaseHandler(getActivity());

        String id = "";
        if (db.getRowCount() > 0) {
            HashMap<String, String> userDetailsHashMap = db.getUserDetails();

            id = userDetailsHashMap.get("id");
        }
        String url =AppConfig.BASE_URL + "api/ticket/getAUsersTicketsLazy/"+ id + "/5/0";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            Log.e("response :",response+"");
                            for(int i=0;i<response.length();i++){
                                try {
                                    JSONObject c = response.getJSONObject(i);
                                    Log.i("oooooo",c.getString("body"));
                                    TicketItem ticketItem = new TicketItem(getActivity(),c.getString("body"),c.getInt("is_question"));
                                    main_layout.addView(ticketItem);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                     }
                        } catch (Exception e) {

                            AppConfig.error(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppConfig.error(error);
                    }
                }
        );
        queue.add(getRequest);

    }


    public void submit(){
        queue = Volley.newRequestQueue(getActivity());
        v.findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
        String url = AppConfig.BASE_URL+"api/ticket/insertNewTicket";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.i("response",response);
                        txt_message.setText("");

                        //Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject json = new JSONObject(response);
                            if(json.getString("success").equals("1")){

                                JSONArray tickets = json.getJSONArray("ticket");
                                v.findViewById(R.id.progressLayout).setVisibility(View.GONE);

                                for(int i=0;i<tickets.length();i++){
                                    try {
                                        JSONObject c = tickets.getJSONObject(i);
                                        Log.i("oooooo",c.getString("body"));
                                        TicketItem ticketItem = new TicketItem(getActivity(),c.getString("body"),c.getInt("is_question"));
                                        main_layout.addView(ticketItem);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                scrollView.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });
                              //  AppConfig.fragmentManager.beginTransaction().replace(R.id.SupportContainer,new SupportFragment()).commit();


                            }
                            //Toast.makeText(getApplicationContext(), json.getString("id"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        //Toast.makeText(getActivity(), error.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();


                DatabaseHandler db = new DatabaseHandler(getActivity());
                HashMap<String,String> user = db.getUserDetails();


                params.put("user_id", user.get("id"));
                params.put("body", txt_message.getText().toString());
                params.put("is_question", "1");
                params.put("employee_id", "1");

                return params;
            }
        };

        strRequest.setTag(TAG);
        // Add the request to the RequestQueue.
        queue.add(strRequest);
        //Volley End
    }


}
