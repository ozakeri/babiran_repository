package net.babiran.app.Sefaresh;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import net.babiran.app.AppController;
import net.babiran.app.DownLoadImageTask;
import net.babiran.app.MainActivity;
import net.babiran.app.R;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import tools.AppConfig;

public class ShowActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    String idSender;
    private RelativeLayout relativeLayout;
    private TextView textView;
    String pathpic;
    ImageView imageViewl;
    private Toolbar mtoolbar;
    private String startTime = null;
    private String endTime = null;
    private SharedPreferences.Editor editor;
    private SharedPreferences pro_prefs;
    private boolean isPush = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);


        idSender = getIntent().getExtras().getString("IDDD");
        startTime = getIntent().getExtras().getString("startTime");
        endTime = getIntent().getExtras().getString("endTime");
        mtoolbar = (Toolbar) findViewById(R.id.app_toolbar_show);
        mtoolbar.setTitle("");
        MySetClickToolbar();
        setSupportActionBar(mtoolbar);
        pro_prefs = getSharedPreferences("productsArray", MODE_PRIVATE);

        Bundle pudhBndle = getIntent().getExtras();
        if (pudhBndle != null) {
            isPush = pudhBndle.getBoolean("isPush");
        }

        final boolean b = AppController.getInstance().getSharedPreferences().getBoolean("productFood", false);
        if (b) {
            getNewPro();
        } else {
            INIT();
        }
    }

    private void MySetClickToolbar() {
        ImageView textView = (ImageView) mtoolbar.findViewById(R.id.arrow_back_search);
        TextView textV = (TextView) mtoolbar.findViewById(R.id.txt_toolbar);
        textV.setText(this.getString(R.string.app_name));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPush) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    finish();
                }
            }
        });
    }

    private void INIT() {
        // Set up the ViewPager with the sections adapter.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.containerrr);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ////////////////////////////////////////////////////

        relativeLayout = (RelativeLayout) findViewById(R.id.headimage);
        // textView = (TextView) findViewById(R.id.id_txt_sssss);

        pathpic = getIntent().getExtras().getString("IsdfDDD");
        System.out.println("pathpic===" + pathpic);
        //text = getIntent().getExtras().getString("title");


        imageViewl = (ImageView) findViewById(R.id.img_show);
        new DownLoadImageTask(imageViewl, this).execute(pathpic);

        new DownLoadLaouytTask(relativeLayout, this).execute(pathpic);
        //  textView.setText(text);


    }


    /////////////////////////////////////////////////
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new Tab1();
                case 1:
                    return new Tab2();
                case 2:
                    return new Tab3();
            }


            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.pitza);
                case 1:
                    return getString(R.string.hamber);
                case 2:
                    return getString(R.string.drink);


            }
            return null;
        }
    }

    public void getNewPro() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("لطفا کمی صبر نمایید");
        progress.setMessage("در حال دریافت اطلاعات از سرور..");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = AppConfig.BASE_URL + "api/product/getNewProducts/" + 0 + "/" + "202";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Response", response.toString());
                        try {
                            AppConfig.NewPro = response;

                            progress.dismiss();
                            INIT();

                            editor = AppController.getInstance().getSharedPreferences().edit();
                            editor.putBoolean("productFood", false);
                            editor.apply();

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
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();
                params.put("offset", String.valueOf(0));
                params.put("limit", String.valueOf("202"));

                return params;
            }

        };

        queue.add(getRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isPush) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
