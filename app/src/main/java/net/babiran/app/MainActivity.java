package net.babiran.app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.babiran.app.Rss.FavListActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Adapters.MenuAdapter;
import Adapters.WhatsAppNumberListAdapter;
import Fragments.BasketListFragment;
import Fragments.BlogFrgment;
import Fragments.CategoryFragment;
import Fragments.EditProfileFrgment;
import Fragments.FactorFragment;
import Fragments.HomeFragment;
import Fragments.ProductFragment;
import Fragments.ProductListFragment;
import Fragments.SearchFrgment;
import Fragments.ShajeFrgment;
import Handlers.DatabaseHandler;
import Models.EventbusModel;
import Models.Feature;
import Models.Image;
import Models.Menu;
import Models.Product;
import co.ronash.pushe.Pushe;
import tools.AppConfig;
import tools.GlobalValues;
import tools.NotificationUtils;
import tools.Util;
import ui_elements.MyTextView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public static RelativeLayout layout_search;
    //    private GoogleCloudMessaging gcmObj;
//    private String RegId,TOKEN;
    private String catId;
    private String proId;
    private boolean getProduct = false;
    public static FrameLayout product, secondcategory, sharje, search, blogContainer, category, basketlist, productlist, about, factorcontainer, home, nazarsanji, setting, mostfactor, fullbanner, card_banner, bigtile_banner, smalltile_banner, support;
    public static RelativeLayout wait;
    public static View viewLogo, btnBack;
    MyTextView voiceText, label;
    public static RelativeLayout deliver;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    RequestQueue queue;
    SharedPreferences pro_prefs;
    public String phone = "";
    RelativeLayout notif_Relative;
    MyTextView notif_title, notif_body;
    ImageView notif_img, img_wait;
    MyTextView phone_txt;
    ArrayList<Menu> menu;
    ListView listView;
    private static MenuAdapter adapter;
    public String id = "";
    public String nameHeader = "";
    String tab_situation = "home";
    public static MyTextView nameHeaderTxt, txt_credit;
    String category_id_notif = "";
    String image_from_notif = "";
    DatabaseHandler db;
    private RelativeLayout layout_favorite;
    public static final String TAG = "TAG";
    GlobalValues globalValues = new GlobalValues();
    private int android_version;
    private boolean android_ver_is_critical = false;
    private int versionCode;
    private JSONArray jsonArray = null;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        Pushe.initialize(this, true);

        //  registerInBackground();
//        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
//        status.getSubscriptionStatus().getUserId();
//        Log.e("Dsdssssss",status.getSubscriptionStatus().getUserId()+"");


        /*SharedPreferences.Editor editor1 = AppController.getInstance().getSharedPreferences().edit();
        editor1.putBoolean("getProduct", false);
        editor1.apply();*/

        txt_credit = findViewById(R.id.txt_credit);
        txt_credit.setTypeface(Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));

        AppConfig.fragmentManager = this.getSupportFragmentManager();
        final boolean b = AppController.getInstance().getSharedPreferences().getBoolean("getProduct", false);
        if (b) {
            //notif_Relative = (RelativeLayout) findViewById(R.id.notif_main_relative);
            //notif_Relative.setVisibility(View.GONE);
            try {
                getProduct = true;
                proId = getIntent().getExtras().getString("pro_id");
                catId = getIntent().getExtras().getString("cat_id");
                getCompaniesByID(catId, proId);
            } catch (Exception e) {
                e.getMessage();
            }
        }

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        versionCode = BuildConfig.VERSION_CODE;
        layout_search = findViewById(R.id.layout_search);
        layout_search.setVisibility(View.GONE);
        MainActivity.layout_search.setVisibility(View.VISIBLE);


        if (TextUtils.isEmpty(getIntent().getStringExtra("AA"))) {
            viewLogo = findViewById(R.id.btn_logo);
            layout_favorite = findViewById(R.id.layout_favorite);
            btnBack = findViewById(R.id.btn_back);


            layout_favorite.setVisibility(View.GONE);
            layout_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, FavListActivity.class));
                }
            });

            layout_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // startActivity(new Intent(MainActivity.this, SearchActivity.class));
                    getSupportFragmentManager().beginTransaction().replace(R.id.searchContainer, new SearchFrgment()).commit();
                    MainActivity.search.setVisibility(View.VISIBLE);
                    MainActivity.sharje.setVisibility(View.INVISIBLE);
                    MainActivity.setting.setVisibility(View.INVISIBLE);
                    MainActivity.about.setVisibility(View.INVISIBLE);
                    MainActivity.support.setVisibility(View.INVISIBLE);
                    MainActivity.home.setVisibility(View.INVISIBLE);
                    MainActivity.category.setVisibility(View.INVISIBLE);
                    MainActivity.basketlist.setVisibility(View.INVISIBLE);
                    MainActivity.blogContainer.setVisibility(View.INVISIBLE);
                    MainActivity.product.setVisibility(View.INVISIBLE);
                    MainActivity.productlist.setVisibility(View.INVISIBLE);
                }
            });

            if (TextUtils.isEmpty(AppConfig.NULLBASKET)) {
                if (isOnline()) {
                    PremissionCheck();
                    AppConfig.act = MainActivity.this;
                    db = new DatabaseHandler(getApplicationContext());

                    pro_prefs = getSharedPreferences("productsArray", MODE_PRIVATE);

                    notif_Relative = (RelativeLayout) findViewById(R.id.notif_main_relative);
                    notif_title = (MyTextView) findViewById(R.id.title_value);
                    notif_body = (MyTextView) findViewById(R.id.body_value);
                    notif_img = (ImageView) findViewById(R.id.notif_img);
                    phone_txt = (MyTextView) findViewById(R.id.phonenumber);

                    if (db.getRowCount() > 0) {

                        HashMap<String, String> userDetailsHashMap = db.getUserDetails();

                        id = userDetailsHashMap.get("id");

                        nameHeader = userDetailsHashMap.get("name");
                        phone = userDetailsHashMap.get("phone1");

                    }


/////////////////////////////////////////////////////////
                    Bundle bundle = getIntent().getExtras();
                    if (bundle != null) {

                        //bundle must contain all info sent in "data" field of the notification
                        Log.e("bundle", bundle.getString("image") + "");

                        String body = bundle.getString("body");
                        String title = bundle.getString("title");
                        String tag = bundle.getString("has_image");
                        if (tag != null) {
                            if (tag.equals("1")) {
                                image_from_notif = bundle.getString("image");
                                category_id_notif = bundle.getString("category_id");

                                if (image_from_notif != null && !image_from_notif.equals("") && !image_from_notif.equals("null")) {
                                    Glide.with(MainActivity.this).load(image_from_notif).diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(notif_img);
                                }
                            }
                        }


                        //Toast.makeText(getApplicationContext(), "Push notification: " + body, Toast.LENGTH_LONG).show();

                        Log.e("dataInRece", body + " " + title + " " + category_id_notif + " " + image_from_notif);

                        notif_Relative.setVisibility(View.VISIBLE);


                        if (title != null && !title.equals("") && !title.equals("null")) {
                            notif_title.setText(title);
                        }
                        if (body != null && !body.equals("") && !body.equals("null")) {
                            notif_body.setText(body);
                        }

                        findViewById(R.id.deny_notif).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                notif_Relative.setVisibility(View.INVISIBLE);
                            }
                        });

                        notif_Relative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (image_from_notif != null && !image_from_notif.equals("") && !image_from_notif.equals("null") && category_id_notif != null &&
                                        !category_id_notif.equals("") && !category_id_notif.equals("null")) {
                                    submit(category_id_notif);
                                }
                            }
                        });
                    }


                    mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            Log.e("intent", intent.getAction());
                            // checking for type intent filter
                            if (intent.getAction().equals(AppConfig.REGISTRATION_COMPLETE)) {
                                // gcm successfully registered
                                // now subscribe to `global` topic to receive app wide notifications
                                //FirebaseMessaging.getInstance().subscribeToTopic(AppConfig.TOPIC_GLOBAL);

                                //displayFirebaseRegId();

                            } else if (intent.getAction().equals(AppConfig.PUSH_NOTIFICATION)) {
                                // new push notification is received

                                String body = intent.getStringExtra("body");
                                String title = intent.getStringExtra("title");
                                final String category_id = intent.getStringExtra("cat_id");
                                final String image = intent.getStringExtra("image");
                                Toast.makeText(getApplicationContext(), "Push notification: " + body, Toast.LENGTH_LONG).show();

                                Log.e("dataInRece", body + " " + title + " " + category_id + " " + image);

                                notif_Relative.setVisibility(View.VISIBLE);

                                if (image != null && !image.equals("") && !image.equals("null")) {
                                    Glide.with(MainActivity.this).load(image).diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(notif_img);
                                }
                                if (title != null && !title.equals("") && !title.equals("null")) {
                                    notif_title.setText(title);
                                }
                                if (body != null && !body.equals("") && !body.equals("null")) {
                                    notif_body.setText(body);
                                }

                                findViewById(R.id.deny_notif).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        notif_Relative.setVisibility(View.INVISIBLE);
                                    }
                                });

                                notif_Relative.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (image != null && !image.equals("") && !image.equals("null") && category_id != null &&
                                                !category_id.equals("") && !category_id.equals("null")) {
                                            submit(category_id);
                                        }
                                    }
                                });
                                //txtMessage.setText(message);
                            }
                        }
                    };
                    //displayFirebaseRegId();
                    initNavigationDrawer(id);

                    deliver = (RelativeLayout) findViewById(R.id.motor);
                    SharedPreferences prefs = getSharedPreferences("factor", MODE_PRIVATE);

                    if (prefs.getString("motor", "deactive").equals("active")) {
                        //deliver.setVisibility(View.VISIBLE);
                    } else {
                        // deliver.setVisibility(View.INVISIBLE);
                    }

                    home = (FrameLayout) findViewById(R.id.Homecontainer);
                    product = (FrameLayout) findViewById(R.id.Productcontainer);
                    secondcategory = (FrameLayout) findViewById(R.id.SecondCategorycontainer);
                    productlist = (FrameLayout) findViewById(R.id.ProductListcontainer);
                    sharje = (FrameLayout) findViewById(R.id.shajeContainer);
                    search = (FrameLayout) findViewById(R.id.searchContainer);
                    blogContainer = (FrameLayout) findViewById(R.id.blogContainer);
                    category = (FrameLayout) findViewById(R.id.Categorycontainer);
                    basketlist = (FrameLayout) findViewById(R.id.BasketListcontainer);
                    factorcontainer = (FrameLayout) findViewById(R.id.Factorcontainer);
                    fullbanner = (FrameLayout) findViewById(R.id.FullbannerContainer);
                    card_banner = (FrameLayout) findViewById(R.id.CardbannerContainer);
                    nazarsanji = (FrameLayout) findViewById(R.id.Nazarcontainer);
                    setting = (FrameLayout) findViewById(R.id.SettingContainer);
                    about = (FrameLayout) findViewById(R.id.AboutContainer);
                    wait = (RelativeLayout) findViewById(R.id.WaitContainer);
                    img_wait = findViewById(R.id.img_wait);
                    bigtile_banner = (FrameLayout) findViewById(R.id.BigTileContainer);
                    smalltile_banner = (FrameLayout) findViewById(R.id.SmallTileContainer);
                    support = (FrameLayout) findViewById(R.id.SupportContainer);

                    Glide.with(this).load(R.drawable.loading).into(img_wait);
                    ///// Rss is set

                    Typeface type = Typeface.createFromAsset(getAssets(), "IRANSansMobile_Bold.ttf");

                    label = (MyTextView) findViewById(R.id.label);


                    //  label.setTypeface(null, Typeface.BOLD);
                    label.setTypeface(type);


                    deliver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity.factorcontainer.setVisibility(View.VISIBLE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.Factorcontainer, new FactorFragment()).commit();
                        }
                    });


                    AppConfig.fragmentManager = this.getSupportFragmentManager();
                    homeGetRequest();
                    getCreditRequest();
                    getWhatsAppNumbers();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("لطفا اتصال خود به اینترنت را بررسی نمایید");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "باشه",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    alertDialog.show();
                }

            } else   // Refresh Bascket Frahment
            {
                db = new DatabaseHandler(getApplicationContext());

                pro_prefs = getSharedPreferences("productsArray", MODE_PRIVATE);

                notif_Relative = (RelativeLayout) findViewById(R.id.notif_main_relative);
                notif_title = (MyTextView) findViewById(R.id.title_value);
                notif_body = (MyTextView) findViewById(R.id.body_value);
                notif_img = (ImageView) findViewById(R.id.notif_img);
                phone_txt = (MyTextView) findViewById(R.id.phonenumber);

                if (db.getRowCount() > 0) {

                    HashMap<String, String> userDetailsHashMap = db.getUserDetails();

                    id = userDetailsHashMap.get("id");
                    nameHeader = userDetailsHashMap.get("name");
                    phone = userDetailsHashMap.get("phone1");

                }


                Intent intent = new Intent(this, FactorList.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();


            }
        } else {
            wait.setVisibility(View.INVISIBLE);
            //getSupportFragmentManager().beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();
        }

        if (b) {
            notif_Relative.setVisibility(View.GONE);
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.search.getVisibility() == View.VISIBLE) {
                    MainActivity.search.setVisibility(View.INVISIBLE);
                    MainActivity.btnBack.setVisibility(View.GONE);
                    MainActivity.viewLogo.setVisibility(View.VISIBLE);
                    MainActivity.layout_search.setVisibility(View.VISIBLE);
                    MainActivity.home.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////Token $ RegId
//    private void registerInBackground()
//    {
//        new AsyncTask<Void, Void, String>()
//        {
//            @Override
//            protected String doInBackground(Void... params) {
//                String msg = "";
//                try {
//                    if (gcmObj == null) {
//                        gcmObj = GoogleCloudMessaging
//                                .getInstance(getApplicationContext());
//                    }
//                    RegId = gcmObj
//                            .register("1045357595996");
//                    msg = "Registration ID :" + RegId;
//
//                } catch (IOException ex) {
//                    msg = "Error :" + ex.getMessage();
//                }
//                return msg;
//            }
//
//            @Override
//            protected void onPostExecute(String msg)
//            {
//                if (!TextUtils.isEmpty(RegId))
//                {
//                    // Store RegId created by GCM Server in SharedPref
//
//                    Log.e("REG", msg);
//                } else {
//                    Log.e("REG", msg);  }
//            }
//        }.execute(null, null, null);
//
//        TOKEN = FirebaseInstanceId.getInstance().getToken();
//
//        Log.d("TOKEN", TOKEN);
//
//    }

    public void submit(final String category_id) {


        //  getActivity().findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
        //Volley Start

        final ProgressDialog d = new ProgressDialog(MainActivity.this);
        d.setMessage("چند لحظه صبرکنید ...");
        d.setIndeterminate(true);
        d.setCancelable(false);
        d.show();

        queue = Volley.newRequestQueue(MainActivity.this);
        String url = AppConfig.BASE_URL + "api/main/search";

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            d.dismiss();
                            notif_Relative.setVisibility(View.INVISIBLE);
                            Log.e("response", response);
                            ArrayList<Product> products = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                ArrayList<Feature> featuresArray = new ArrayList<>();
                                ArrayList<Image> imagesArray = new ArrayList<>();
                                JSONObject c = jsonArray.getJSONObject(i);

                                JSONArray features = c.getJSONArray("features");
                                for (int fea = 0; fea < features.length(); fea++) {
                                    try {
                                        JSONObject f = features.getJSONObject(fea);
                                        Feature feature = new Feature(
                                                f.getString("value"), f.getString("name"));
                                        featuresArray.add(fea, feature);
                                    } catch (JSONException ex) {

                                    }
                                }

                                JSONArray images = c.getJSONArray("images");
                                ;
                                for (int img = 0; img < images.length(); img++) {

                                    try {
                                        JSONObject im = images.getJSONObject(img);
                                        Image image = new Image(
                                                im.getString("image_link"));
                                        imagesArray.add(img, image);
                                    } catch (JSONException ex) {

                                    }
                                }

                                Product product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, c.getString("provider_name"));

                                products.add(product);
                            }
                            if (products.size() > 0) {
                                //   getActivity().findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);
                                MainActivity.smalltile_banner.setVisibility(View.INVISIBLE);
                                AppConfig.fragmentManager.beginTransaction().replace(R.id.ProductListcontainer, new ProductListFragment(products, "notcat")).commit();

                            } else {
                                //   getActivity().findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);


                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("محصولِی در این دسته بندی موجود نیست ");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "باشه",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

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
                Log.e("Volley", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();

                if (category_id != null && !category_id.equals("") && !category_id.equals("null")) {
                    params.put("category_id", category_id);
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


        //Volley End
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e("heloo", "sendRegistrationToServer: " + token);

        queue = Volley.newRequestQueue(this);


        String url = AppConfig.BASE_URL + "api/user/updateAUser/" + id;
        // Request a string response from the provided URL.

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();


                if (token != null && token.length() > 0) {
                    params.put("code", token);
                } else {
                    params.put("code", "KmD3487f83nDFrm448Fp03Az4wl4F1sPPwkm38dGdek5km");
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

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        sendRegistrationToServer(regId);
        Log.e("tag", "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {
            // txtRegId.setText("Firebase Reg Id: " + regId);
            Log.e("Firebase Reg Id:", regId);
        } else
            // txtRegId.setText("Firebase Reg Id is not received yet!");
            Log.e("Firebase Reg Id:", "Firebase Reg Id is not received yet!");

    }

    private void startSound(String filename) {
        try {
            AssetFileDescriptor afd = getBaseContext().getAssets().openFd(filename);
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public void homeGetRequest() {

        //sendToken();

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        if (id.equals("")) {
            id = "-1";
        }
        Log.d("idd", id);

        final String url = AppConfig.BASE_URL + "api/main/getHome2/" + id;
        System.out.println("getHome2=====" + url);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (!response.isNull("android_version") && !response.isNull("android_ver_is_critical")) {
                                android_ver_is_critical = response.getBoolean("android_ver_is_critical");
                                android_version = response.getInt("android_version");

                               /* if (android_ver_is_critical) {
                                    if (versionCode < android_version) {
                                        showDialog();
                                        return;
                                    }
                                }*/
                            }

                            AppConfig.slides1 = response.getJSONArray("slides1");
                            AppConfig.topsellpro = response.getJSONArray("topSellsProducts");
                            AppConfig.topseenPro = response.getJSONArray("topSeenProducts");
                            AppConfig.newPro = response.getJSONArray("NewProducts");
                            AppConfig.specialPro = response.getJSONArray("specialProducts");
                            AppConfig.categories = response.getJSONArray("categories");
                            AppConfig.discountpro = response.getJSONArray("discountProducts");
                            AppConfig.mostorder = response.getJSONArray("mostOrders");
                            AppConfig.fullbanner = response.getJSONArray("fullBannerCategory");
                            AppConfig.footerBanner = response.getJSONArray("footerBannerCategory");
                            AppConfig.getproduct = response.getJSONArray("product");
                            AppConfig.restaurants_info = response.getJSONArray("restaurants_info");
                            AppConfig.cardbanner = response.getJSONArray("cardBannerCategory");
                            AppConfig.smallTile = response.getJSONArray("smallTileCategory");
                            AppConfig.bigTile = response.getJSONArray("bigTileCategory");

                            if (pro_prefs != null) {
                                Gson gson = new Gson();
                                String json = pro_prefs.getString("products", "");
                                ArrayList<Product> obj = gson.fromJson(json, new TypeToken<List<Product>>() {
                                }.getType());
                                Log.e("objGson", obj + "");

                                if (obj != null) {
                                    AppConfig.products = obj;
                                }

                            }

                            try {
                                loadfragments();
                            } catch (Exception e) {
                                e.getMessage();
                            }
                            tabListeners();


                        } catch (JSONException e) {

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

    public void getCreditRequest() {

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        if (id.equals("")) {
            id = "-1";
        }

        final String url = AppConfig.BASE_URL + "api/main/getCredit/" + id;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                String credit = response.getString("credit");

                                if (response.getString("credit") == null) {
                                    credit = "0";
                                }
                                EventBus.getDefault().post(new EventbusModel(credit));
                                Pattern p = Pattern.compile("\\d+");
                                Matcher m = p.matcher(credit);
                                while (m.find()) {
                                    //txt_credit.setText(Util.convertToFormalString((m.group())));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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

    public void getWhatsAppNumbers() {

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        if (id.equals("")) {
            id = "-1";
        }

        final String url = "http://babiran.net/api/whatsappnumbers";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            jsonArray = response;
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

    private void loadfragments() {
        getSupportFragmentManager().beginTransaction().replace(R.id.Homecontainer, new HomeFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.Categorycontainer, new CategoryFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.shajeContainer, new ShajeFrgment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.blogContainer, new BlogFrgment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();

        findViewById(R.id.Homecontainer).setVisibility(View.VISIBLE);
        findViewById(R.id.shajeContainer).setVisibility(View.INVISIBLE);
        findViewById(R.id.blogContainer).setVisibility(View.INVISIBLE);
        findViewById(R.id.Categorycontainer).setVisibility(View.INVISIBLE);
        findViewById(R.id.BasketListcontainer).setVisibility(View.INVISIBLE);
        findViewById(R.id.searchContainer).setVisibility(View.INVISIBLE);

        toolbar.setVisibility(View.VISIBLE);
        layout_search.setVisibility(View.VISIBLE);

        if (findViewById(R.id.blogContainer).getVisibility() == View.VISIBLE) {
            layout_search.setVisibility(View.GONE);
        }

    }

    private void tabListeners() {

        findViewById(R.id.tab_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventbusModel(false));
                viewLogo.setVisibility(View.VISIBLE);
                layout_favorite.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
                findViewById(R.id.Homecontainer).setVisibility(View.VISIBLE);
                findViewById(R.id.shajeContainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Categorycontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Productcontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.BasketListcontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.blogContainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.SecondCategorycontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.ProductListcontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.searchContainer).setVisibility(View.INVISIBLE);
                MainActivity.layout_search.setVisibility(View.VISIBLE);

                if (tab_situation.equals("category")) {

                    findViewById(R.id.tab_category).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_home).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "home";
                } else if (tab_situation.equals("profile")) {

                    findViewById(R.id.tab_profile).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_home).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "home";
                } else if (tab_situation.equals("search")) {

                    findViewById(R.id.tab_sharje).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_home).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "home";
                }


            }
        });

        findViewById(R.id.tab_sharje).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewLogo.setVisibility(View.VISIBLE);
                layout_favorite.setVisibility(View.GONE);
                EventBus.getDefault().post(new EventbusModel(false));
                btnBack.setVisibility(View.GONE);

                findViewById(R.id.Homecontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.shajeContainer).setVisibility(View.VISIBLE);
                findViewById(R.id.Categorycontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.BasketListcontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.blogContainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.SecondCategorycontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Productcontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.ProductListcontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.searchContainer).setVisibility(View.INVISIBLE);
                MainActivity.layout_search.setVisibility(View.VISIBLE);


                if (tab_situation.equals("category")) {

                    findViewById(R.id.tab_category).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_sharje).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "search";
                } else if (tab_situation.equals("profile")) {

                    findViewById(R.id.tab_profile).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_sharje).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "search";
                } else if (tab_situation.equals("home")) {

                    findViewById(R.id.tab_home).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_sharje).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "search";
                } else if (tab_situation.equals("blog")) {

                    findViewById(R.id.tab_blog).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_sharje).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "search";
                }


            }
        });

        findViewById(R.id.tab_blog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewLogo.setVisibility(View.VISIBLE);
                layout_favorite.setVisibility(View.GONE);
                EventBus.getDefault().post(new EventbusModel(false));
                btnBack.setVisibility(View.GONE);

                findViewById(R.id.Homecontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.blogContainer).setVisibility(View.VISIBLE);
                findViewById(R.id.shajeContainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Categorycontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.BasketListcontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.searchContainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.SecondCategorycontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Productcontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.ProductListcontainer).setVisibility(View.INVISIBLE);
                MainActivity.layout_search.setVisibility(View.GONE);

                if (tab_situation.equals("category")) {

                    findViewById(R.id.tab_category).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_blog).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "blog";
                } else if (tab_situation.equals("profile")) {

                    findViewById(R.id.tab_profile).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_blog).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "blog";
                } else if (tab_situation.equals("home")) {

                    findViewById(R.id.tab_home).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_blog).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "blog";
                } else if (tab_situation.equals("search")) {
                    findViewById(R.id.tab_sharje).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_blog).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "blog";
                }


            }
        });

        findViewById(R.id.tab_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLogo.setVisibility(View.VISIBLE);
                layout_favorite.setVisibility(View.GONE);
                EventBus.getDefault().post(new EventbusModel(false));
                btnBack.setVisibility(View.GONE);

                findViewById(R.id.Homecontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.shajeContainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.blogContainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Categorycontainer).setVisibility(View.VISIBLE);
                findViewById(R.id.BasketListcontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.SecondCategorycontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.Productcontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.ProductListcontainer).setVisibility(View.INVISIBLE);
                findViewById(R.id.searchContainer).setVisibility(View.INVISIBLE);
                MainActivity.layout_search.setVisibility(View.VISIBLE);

                if (tab_situation.equals("home")) {
                    findViewById(R.id.tab_home).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_category).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "category";
                } else if (tab_situation.equals("profile")) {
                    findViewById(R.id.tab_profile).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_category).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "category";
                } else if (tab_situation.equals("search")) {
                    findViewById(R.id.tab_sharje).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_category).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "category";
                } else if (tab_situation.equals("blog")) {
                    findViewById(R.id.tab_blog).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                    findViewById(R.id.tab_category).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                    tab_situation = "category";
                }
            }
        });

        findViewById(R.id.tab_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLogo.setVisibility(View.VISIBLE);
                layout_favorite.setVisibility(View.GONE);

                btnBack.setVisibility(View.GONE);

                if (db.getRowCount() == 0) {


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("ورود کاربران");

                    alertDialog.setMessage("آیا مایلید ثبت نام کنید ؟");
                    alertDialog.setIcon(R.drawable.prof);

                    alertDialog.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(MainActivity.this, Sms_Register.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    alertDialog.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                } else {

                    AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();
                    findViewById(R.id.Homecontainer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.shajeContainer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.blogContainer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.Categorycontainer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.BasketListcontainer).setVisibility(View.VISIBLE);
                    findViewById(R.id.SecondCategorycontainer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.Productcontainer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.ProductListcontainer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.searchContainer).setVisibility(View.INVISIBLE);
                    MainActivity.layout_search.setVisibility(View.VISIBLE);

                    if (tab_situation.equals("home")) {

                        findViewById(R.id.tab_home).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                        findViewById(R.id.tab_profile).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                        tab_situation = "profile";
                    } else if (tab_situation.equals("category")) {

                        findViewById(R.id.tab_category).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                        findViewById(R.id.tab_profile).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                        tab_situation = "profile";
                    } else if (tab_situation.equals("search")) {

                        findViewById(R.id.tab_sharje).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                        findViewById(R.id.tab_profile).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                        tab_situation = "profile";
                    } else if (tab_situation.equals("blog")) {
                        findViewById(R.id.tab_blog).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                        findViewById(R.id.tab_profile).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                        tab_situation = "profile";
                    }


                }


            }
        });
    }

    public void initNavigationDrawer(final String userID) {

        listView = (ListView) findViewById(R.id.lst_menu_items);
        menu = new ArrayList<>();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //   menu.add(new Menu("افزایش اعتبار کیف پول", R.drawable.kif));
        menu.add(new Menu("پروفایل من", R.drawable.prof));
        menu.add(new Menu("کیف پول", R.drawable.credit));
        menu.add(new Menu("سوابق و پیگیری سفارشات", R.drawable.sefaresh));
        //menu.add(new Menu("خرید بلیط", R.drawable.ic_ticket));
        //menu.add(new Menu("شارژ سیمکارت اعتباری", R.drawable.shaje_icon));
        // menu.add(new Menu("دسته بندی", R.drawable.cats));
        // menu.add(new Menu("سبد خرید", R.drawable.sabad2));
        //menu.add(new Menu("آخرین اخبار", R.drawable.ic_news));
        //menu.add(new Menu("رویداد ها و مطالب گوناگون", R.drawable.ic_news));
        menu.add(new Menu("پشتیبانی", R.drawable.ic_suport));
        menu.add(new Menu("اشتراک گذاری", R.drawable.share));
        menu.add(new Menu("سوالات متداول", R.drawable.ic_ansqus));
        menu.add(new Menu("راهنمای استفاده", R.drawable.help_icon));
        //menu.add(new Menu("درباره ما", R.drawable.babiran));
        // menu.add(new Menu("تنظیمات", R.drawable.ic_info_outline_black_24dp));
        menu.add(new Menu("خروج از حساب کاربری", R.drawable.ic_exit___));
        adapter = new MenuAdapter(menu, getApplicationContext());


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //  Menu dataModel = menu.get(position);
                switch (position) {
                   /* case 0 :
                        Toast.makeText(getBaseContext(),"این بخش در دست ساخت می باشد",Toast.LENGTH_LONG).show();
                        break;*/
                    case 0:
                        if (!userID.equals("")) {
                            startActivity(new Intent(MainActivity.this, EditProfileFrgment.class));
                            drawerLayout.closeDrawer(Gravity.RIGHT);
                            MainActivity.setting.setVisibility(View.INVISIBLE);
                            MainActivity.about.setVisibility(View.INVISIBLE);
                            MainActivity.support.setVisibility(View.INVISIBLE);
                            MainActivity.category.setVisibility(View.INVISIBLE);
                            MainActivity.basketlist.setVisibility(View.INVISIBLE);
                            break;

                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                            alertDialog.setTitle("ورود کاربران");

                            alertDialog.setMessage("آیا مایلید ثبت نام کنید ؟");
                            alertDialog.setIcon(R.drawable.prof);

                            alertDialog.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(MainActivity.this, Sms_Register.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            alertDialog.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        }


                        break;

                    case 1:
                        startActivity(new Intent(MainActivity.this, CreditActivity.class));
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        MainActivity.setting.setVisibility(View.INVISIBLE);
                        MainActivity.about.setVisibility(View.INVISIBLE);
                        MainActivity.support.setVisibility(View.INVISIBLE);
                        MainActivity.category.setVisibility(View.INVISIBLE);
                        MainActivity.basketlist.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        Intent intent = new Intent(MainActivity.this, FactorList.class);
                        intent.putExtra("id", userID);

                        startActivity(intent);
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        MainActivity.setting.setVisibility(View.INVISIBLE);
                        MainActivity.about.setVisibility(View.INVISIBLE);
                        MainActivity.support.setVisibility(View.INVISIBLE);
                        MainActivity.category.setVisibility(View.INVISIBLE);
                        MainActivity.basketlist.setVisibility(View.INVISIBLE);


                        break;
                    /*case 2:
                        //  startActivity(new Intent(MainActivity.this,MainTabs.class));
                        Toast.makeText(MainActivity.this, "بزودی در نسخه بعدی...", Toast.LENGTH_SHORT).show();
//
                        //ticket
                        break;*/
                    /*case 3:

                        startActivity(new Intent(MainActivity.this, SharjActivity.class));
//                        getSupportFragmentManager().beginTransaction().replace(R.id.SupportContainer, new SupportFragment()).commit();
//                        drawerLayout.closeDrawer(Gravity.RIGHT);
//                        MainActivity.setting.setVisibility(View.INVISIBLE);
//                        MainActivity.about.setVisibility(View.INVISIBLE);
//                        MainActivity.support.setVisibility(View.VISIBLE);
//                        MainActivity.category.setVisibility(View.INVISIBLE);
//                        MainActivity.basketlist.setVisibility(View.INVISIBLE);


                        break;*/
                 /*   case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.Categorycontainer, new CategoryFragment()).commit();
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        MainActivity.setting.setVisibility(View.INVISIBLE);
                        MainActivity.about.setVisibility(View.INVISIBLE);
                        MainActivity.support.setVisibility(View.INVISIBLE);
                        MainActivity.home.setVisibility(View.INVISIBLE);
                        MainActivity.category.setVisibility(View.VISIBLE);
                        MainActivity.basketlist.setVisibility(View.INVISIBLE);

                        break;*/

                   /* case 4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        MainActivity.setting.setVisibility(View.INVISIBLE);
                        MainActivity.about.setVisibility(View.INVISIBLE);
                        MainActivity.support.setVisibility(View.INVISIBLE);
                        MainActivity.home.setVisibility(View.INVISIBLE);
                        MainActivity.category.setVisibility(View.INVISIBLE);
                        MainActivity.basketlist.setVisibility(View.VISIBLE);

                        break;*/
                  /*  case 6:
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        startActivity(new Intent(MainActivity.this, MainListActivity.class));

                        break;*/
                    case 3:
                        try {
                            Di();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //kif pool
                        break;
                    case 4:

                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://cafebazaar.ir/app/net.babiran.app"));
                        startActivity(intent);

                        break;
                    case 5:
                        // solat motedavel
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        startActivity(new Intent(MainActivity.this, CommonQuestionActivity.class));


                        break;

                    case 6:

                        //rahnama
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        startActivity(new Intent(MainActivity.this, GuideUsageActivity.class));
                        MainActivity.setting.setVisibility(View.INVISIBLE);
                        MainActivity.about.setVisibility(View.INVISIBLE);
                        MainActivity.support.setVisibility(View.INVISIBLE);
                        MainActivity.category.setVisibility(View.INVISIBLE);
                        MainActivity.basketlist.setVisibility(View.INVISIBLE);

                        break;
                    /*case 11:
                        //about

                        getSupportFragmentManager().beginTransaction().replace(R.id.AboutContainer, new AboutFragment()).commit();
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        MainActivity.edit.setVisibility(View.INVISIBLE);
                        MainActivity.setting.setVisibility(View.INVISIBLE);
                        MainActivity.about.setVisibility(View.VISIBLE);
                        MainActivity.support.setVisibility(View.INVISIBLE);
                        MainActivity.category.setVisibility(View.INVISIBLE);
                        MainActivity.basketlist.setVisibility(View.INVISIBLE);


                        break;*/
                   /* case 12:
                      //  startActivity(new Intent(MainActivity.this, SettingActivity.class));

                        break;*/
                    case 7:

                        //khoroj
                        AlertDialog.Builder builder = new AlertDialog.Builder(AppConfig.act);
                        builder.setTitle("می خواهید از حساب خود خارج شوید؟");
                        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                db.resetTables();
                                dialog.dismiss();
                                startActivity(new Intent(MainActivity.this, BlankAcct.class));
                                MainActivity.this.finish();
                            }
                        });
                        builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;

                }

            }
        });

        nameHeaderTxt = (MyTextView) findViewById(R.id.name_header);
        if (!nameHeader.equals("null")) {
            nameHeaderTxt.setText(nameHeader);
            Log.d("header name :", nameHeaderTxt.getText().toString());
        }
        if (!phone.equals("null") && !phone.equals("")) {
            phone_txt.setText(ConvertEnToPe(phone));
        }


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == android.R.id.home) {
                    txt_credit.setText(globalValues.getCreditValue());
                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }
                return false;
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public String ConvertEnToPe(String value) {
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i))) {
                builder.append(arabicChars[(int) (value.charAt(i)) - 48]);
            } else {
                builder.append(value.charAt(i));
            }
        }
        return builder.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return;
            }

        }
    }


    private void PremissionCheck() {


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Subscribe
    public void getEvent(EventbusModel model) {

        if (model.getCredit() != null) {
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(model.getCredit());
            while (m.find()) {
                txt_credit.setText(Util.convertToFormalString((m.group())));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnBack.setVisibility(View.INVISIBLE);
        layout_search.setVisibility(View.VISIBLE);
        viewLogo.setVisibility(View.VISIBLE);
        getCreditRequest();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

        if (!TextUtils.isEmpty(AppConfig.REFRESH)) {
            AppConfig.REFRESH = "";
            AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();

        }
        if (AppConfig.BACK_TO_LIOST.equals("1")) {
            AppConfig.BACK_TO_LIOST = "0";
            Log.e("RESUME", "dkfjghskdfgh");
            viewLogo.setVisibility(View.VISIBLE);
            layout_favorite.setVisibility(View.GONE);

            findViewById(R.id.Homecontainer).setVisibility(View.INVISIBLE);
            findViewById(R.id.shajeContainer).setVisibility(View.INVISIBLE);
            findViewById(R.id.blogContainer).setVisibility(View.INVISIBLE);
            findViewById(R.id.Categorycontainer).setVisibility(View.VISIBLE);
            findViewById(R.id.BasketListcontainer).setVisibility(View.INVISIBLE);
            findViewById(R.id.SecondCategorycontainer).setVisibility(View.INVISIBLE);
            findViewById(R.id.Productcontainer).setVisibility(View.INVISIBLE);
            findViewById(R.id.ProductListcontainer).setVisibility(View.INVISIBLE);

            if (tab_situation.equals("home")) {

                findViewById(R.id.tab_home).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                findViewById(R.id.tab_category).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                tab_situation = "category";
            } else if (tab_situation.equals("profile")) {

                findViewById(R.id.tab_profile).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                findViewById(R.id.tab_category).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                tab_situation = "category";
            } else if (tab_situation.equals("search")) {

                findViewById(R.id.tab_sharje).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out_tab));
                findViewById(R.id.tab_category).startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in_tab));
                tab_situation = "category";
            }


        }


        Handler handler = new Handler();
        Thread someThread = new Thread() {

            @Override
            public void run() {

                //some actions
                handler.post(new Runnable() {
                    public void run() {
                        if (findViewById(R.id.blogContainer).getVisibility() == View.VISIBLE) {
                            MainActivity.layout_search.setVisibility(View.GONE);
                        }
                    }
                });
            }
        };

        someThread.start();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private void Di() throws JSONException {
        Dialog dialog = new Dialog(this);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.coustom_dilog_sup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();


        ImageView telegram = (ImageView) dialog.findViewById(R.id.kjdnfgbkjn1);

        ImageView sourush = (ImageView) dialog.findViewById(R.id.kjdnfgbkjn3);

        ImageView whats_up = (ImageView) dialog.findViewById(R.id.whats_up);

        LinearLayout linearLayout = dialog.findViewById(R.id.linearLayout);
        TextView txt_close = dialog.findViewById(R.id.txt_close);
        RelativeLayout relativeLayout = dialog.findViewById(R.id.relativeLayout);
        RecyclerView recycler_view = dialog.findViewById(R.id.recycler_view);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            list.add(String.valueOf(jsonArray.get(i)));

        }
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(new WhatsAppNumberListAdapter(this,list));

        relativeLayout.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        whats_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);


             /*   String url = "https://api.whatsapp.com/send?phone=" + "+989143185242";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/
            }
        });


        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://t.me/babiran2"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

//        whats.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:+989143185242"));
//                startActivity(intent);
//            }
//        });

        sourush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://sapp.ir/babiran2"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public void getCompaniesByID(final String catId, final String proId) {
        queue = Volley.newRequestQueue(getApplicationContext());

    /*    final ProgressDialog d = new ProgressDialog(getApplicationContext());
        d.setMessage("چند لحظه صبرکنید ...");
        d.setIndeterminate(true);
        d.setCancelable(false);
        d.show();*/

        String url = AppConfig.BASE_URL + "api/main/search";
        // Request a string response from the provided URL.

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //d.dismiss();
                        Log.e("responseINCat", response);

                        try {

                            ArrayList<Product> products = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                ArrayList<Feature> featuresArray = new ArrayList<>();
                                ArrayList<Image> imagesArray = new ArrayList<>();
                                JSONObject c = jsonArray.getJSONObject(i);

                                JSONArray features = c.getJSONArray("features");
                                for (int fea = 0; fea < features.length(); fea++) {
                                    try {
                                        JSONObject f = features.getJSONObject(fea);
                                        Feature feature = new Feature(
                                                f.getString("value"), f.getString("name"));
                                        featuresArray.add(fea, feature);
                                    } catch (JSONException ex) {

                                    }
                                }

                                JSONArray images = c.getJSONArray("images");
                                for (int img = 0; img < images.length(); img++) {

                                    try {
                                        JSONObject im = images.getJSONObject(img);
                                        Image image = new Image(
                                                im.getString("image_link"));
                                        imagesArray.add(img, image);
                                    } catch (JSONException ex) {

                                    }
                                }

                                Product product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray);


                                products.add(product);
                            }
                            Log.e("compSize", products.size() + "");

                            for (int i = 0; i < products.size(); i++) {
                                if (products.get(i).id.equals(proId)) {
                                    Product product = products.get(i);
                                    AppConfig.fragmentManager.beginTransaction().replace(R.id.Productcontainer, new ProductFragment(product, getProduct)).commit();
                                }
                            }

                            //Product product = products.get(Integer.parseInt(proId));


                            //getSupportFragmentManager().beginTransaction().replace(R.id.Productcontainer, new ProductFragment(proId, catId, getProduct)).commit();

                            SharedPreferences.Editor editor = AppController.getInstance().getSharedPreferences().edit();
                            editor.putBoolean("getProduct", false);
                            editor.apply();

                            /*if(products.size() > 0){
                             *//* Gson gson = new Gson();
                                String compObj = gson.toJson(companies);
                                Intent intent = new Intent(SecondCategory.this,CompanyList.class);
                                intent.putExtra("companies",compObj);
                                startActivity(intent);
                                SecondCategory.secondCategory.finish();*//*

                                //  MainActivity.secondcategory.setVisibility(View.VISIBLE);

                                AppConfig.fragmentManager.beginTransaction().replace(R.id.ProductListcontainer,new ProductListFragment(products,id,"second")).commit();



                            }
                            else{
                                v.findViewById(R.id.no_comp).setVisibility(View.VISIBLE);
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<String, String>();


                params.put("category_id", catId);

                return params;
            }

        };
        jsonArrayRequest.setTag("tag");
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                400000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeDrawer();
    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);

        } else {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    public void showDialog() {
        final Dialog dialog_wait = new Dialog(this, R.style.Theme_Dialog);
        dialog_wait.setContentView(R.layout.dialog_alert_update);
        dialog_wait.show();
        dialog_wait.setCancelable(false);
        Window window = dialog_wait.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        TextView ok = dialog_wait.findViewById(R.id.ok);
        TextView cancel = dialog_wait.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://cafebazaar.ir/app/net.babiran.app"));
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
