package net.babiran.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import net.babiran.app.AppController;
import net.babiran.app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Fragments.dynamicfragment.TabAdapter;
import Models.DeliveryTime;
import Models.TimeList;
import saman.zamani.persiandate.PersianDate;
import tools.AppConfig;
import tools.GlobalValues;

/**
 * Created by Mohammad on 7/26/2017.
 */

@SuppressLint("ValidFragment")
public class DateTimeDialog extends AppCompatActivity {

    View v;
    public ArrayList<TimeList> timeLists = new ArrayList<>();
    public ArrayList<DeliveryTime> dateTimes = new ArrayList<>();
    public RecyclerView recycler_view;
    private Context context;
    private TextView txt_date;
    private TabAdapter adapter;
    private TabLayout tab;
    private ViewPager viewPager;
    private GlobalValues globalValues = new GlobalValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.date_time_dialog);

        txt_date = findViewById(R.id.txt_date);
        recycler_view = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(layoutManager);
        getTime();

        PersianDate persianDate = new PersianDate();
        txt_date.setText("امروز " + persianDate.dayName() + " " + persianDate.getShYear() + "/" + persianDate.getShMonth() + "/" + persianDate.getShDay());

        viewPager = findViewById(R.id.viewPager);
        tab = findViewById(R.id.tabLayout);
    }

    public void getTime() {

        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = AppConfig.BASE_URL + "api/main/delivery_time";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response != null) {
                            globalValues.setJsonArray(response);
                            SharedPreferences.Editor editor = AppController.getInstance().getSharedPreferences().edit();
                            editor.putString("delivery_time", String.valueOf(response));
                            editor.apply();
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    timeLists.clear();
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    tab.addTab(tab.newTab().setText(jsonObject.getString("date")));
                                }

                                adapter = new TabAdapter(getSupportFragmentManager(), tab.getTabCount());
                                viewPager.setAdapter(adapter);
                                viewPager.setOffscreenPageLimit(1);
                                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
                                tab.setOnTabSelectedListener(onTabSelectedListener(viewPager));

                            } catch (Exception e) {

                                AppConfig.error(e);
                            }
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

    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager pager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }
}
