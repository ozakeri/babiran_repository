package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.babiran.app.AppController;
import net.babiran.app.FullBanner;
import net.babiran.app.MainActivity;
import net.babiran.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.BigTileAdapter;
import Adapters.DisProListAdapter;
import Adapters.FooterAdapter;
import Adapters.NewProListAdapter;
import Adapters.SmallTileAdapter;
import Adapters.TopProListAdapter;
import Handlers.DatabaseHandler;
import Models.Category;
import Models.Color;
import Models.Feature;
import Models.Image;
import Models.Moshakhasat;
import Models.Product;
import me.relex.circleindicator.CircleIndicator;
import tools.AppConfig;
import tools.CustomPagerAdapterByUrlMain;
import tools.GlobalValues;
import tools.Util;
import ui_elements.Card;
import ui_elements.MyTextView;

import static tools.AppConfig.fragmentManager;


public class HomeFragment extends Fragment {
    ViewPager viewPager, viewPager2;
    CircleIndicator customIndicator, customIndicator2;
    CardView firstBanner, secondbanner, FullfirstBanner, FullSecondBanner, specialLayout;
    RelativeLayout specialTondMarketRelative, special, discount, newpro, selltop;
    public LinearLayout horizontal1, horizontal2, secondHorizontal;
    public ArrayList<Card> cards;
    public List<Category> footerCategories = new ArrayList<>();
    public List<Category> smallTileCategories = new ArrayList<>();
    public List<Category> bigTileCategories = new ArrayList<>();
    public CustomPagerAdapterByUrlMain mCustomPagerAdapterByUrlMain, mCustomPagerAdapterByUrlMain2;
    NewProListAdapter adp;
    TopProListAdapter adpTop;
    DisProListAdapter adpDis;
    MyTextView cardTextHome, HourTxt, MinTxt, SecTxt;
    RelativeLayout specialRelative;
    LinearLayout layspecial, laynew, laydiscount, laytopsell;
    ImageView firstFullBannerImg, secondFullBannerImg, firstCardBannerImage, secondCardBannerImage;
    public ArrayList<Product> Newproducts = new ArrayList<>();
    public ArrayList<Product> Topproducts = new ArrayList<>();
    public ArrayList<Product> Disproducts = new ArrayList<>();
    String firstslideImage = "";
    String secondslideImage = "";
    String firstCardImage = "";
    String secondCardImage = "";
    ListView NewProList, TopProList, DisProList;
    RequestQueue queue;
    public static final String TAG = "TAG";
    private RecyclerView recycler_view, recycler_view_smallTile, recycler_view_bigTile;
    private GlobalValues globalValues = new GlobalValues();


    DatabaseHandler db;

    private SwipeRefreshLayout disSwipeRefreshLayout, proSwipeRefreshLayout, topSwipeRefreshLayout;


    public HomeFragment() {

    }

    View v;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Timer timer, timer2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_home_frgment, container, false);


        AppConfig.frag = HomeFragment.this;
        MainActivity.home.setVisibility(View.VISIBLE);

        viewPager = (ViewPager) v.findViewById(R.id.home_viewpager);
        //viewPager2 = (ViewPager) v.findViewById(R.id.home_viewpager2);

        specialTondMarketRelative = (RelativeLayout) v.findViewById(R.id.specialTondMarketRelative);
        special = v.findViewById(R.id.special);
        discount = v.findViewById(R.id.discount);
        newpro = v.findViewById(R.id.newpro);
        selltop = v.findViewById(R.id.selltop);

        firstBanner = (CardView) v.findViewById(R.id.firstbanner);
        secondbanner = (CardView) v.findViewById(R.id.secondbanner);

        specialLayout = (CardView) v.findViewById(R.id.specialLayout);

        firstFullBannerImg = (ImageView) v.findViewById(R.id.imgfirstFullban1);
        secondFullBannerImg = (ImageView) v.findViewById(R.id.imgsecondFullban1);
        firstCardBannerImage = (ImageView) v.findViewById(R.id.imgfirstban1);
        secondCardBannerImage = (ImageView) v.findViewById(R.id.imgsecondban2);

        FullfirstBanner = (CardView) v.findViewById(R.id.Fullfirstbanner);
        FullSecondBanner = (CardView) v.findViewById(R.id.Fullsecondbanner);

        specialRelative = (RelativeLayout) v.findViewById(R.id.specialRelative);

        layspecial = (LinearLayout) v.findViewById(R.id.layspecial);
        laydiscount = (LinearLayout) v.findViewById(R.id.laydiscount);
        laynew = (LinearLayout) v.findViewById(R.id.laynew);
        laytopsell = (LinearLayout) v.findViewById(R.id.laytopsell);

        NewProList = (ListView) v.findViewById(R.id.proListHome);
        TopProList = (ListView) v.findViewById(R.id.TopProListHome);
        DisProList = (ListView) v.findViewById(R.id.DisProListHome);

        horizontal1 = (LinearLayout) v.findViewById(R.id.horizontal1);
        secondHorizontal = (LinearLayout) v.findViewById(R.id.secondhorizontal);


        cardTextHome = (MyTextView) v.findViewById(R.id.cardtextHome);
        HourTxt = (MyTextView) v.findViewById(R.id.hourTxt);
        MinTxt = (MyTextView) v.findViewById(R.id.minTxt);
        SecTxt = (MyTextView) v.findViewById(R.id.secTxt);
        recycler_view = v.findViewById(R.id.recycler_view);
        recycler_view_smallTile = v.findViewById(R.id.recycler_view_smallTile);
        recycler_view_bigTile = v.findViewById(R.id.recycler_view_bigTile);
        disSwipeRefreshLayout = v.findViewById(R.id.disSwipeRefreshLayout);
        proSwipeRefreshLayout = v.findViewById(R.id.proSwipeRefreshLayout);
        topSwipeRefreshLayout = v.findViewById(R.id.topSwipeRefreshLayout);

        recycler_view.setNestedScrollingEnabled(false);
        recycler_view_smallTile.setNestedScrollingEnabled(false);
        recycler_view_bigTile.setNestedScrollingEnabled(false);

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        viewPager.getLayoutParams().height = width / 2;
        //  viewPager2.getLayoutParams().height = width / 2;

        firstBanner.getLayoutParams().height = width / 2;
        // firstBanner.getLayoutParams().width = (int) (width * 0.92);
        secondbanner.getLayoutParams().height = width / 2;
        FullfirstBanner.getLayoutParams().height = width / 2;

        FullSecondBanner.getLayoutParams().height = width / 2;
        //FullSecondBanner.getLayoutParams().width = (int) (width * 0.92);
        // FullSecondBanner.getLayoutParams().height = width / 2;
        //FullSecondBanner.getLayoutParams().height = width / 2;


        final String fullbannerList = getFirstFullBanner();
        if (firstslideImage != null && !firstslideImage.equals("") && !firstslideImage.equals("null")) {
            Glide.with(getActivity()).load(firstslideImage).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(firstFullBannerImg);
        }

        FullfirstBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("fullListener", "here");
                fragmentManager.beginTransaction().replace(R.id.FullbannerContainer, new FullBanner(fullbannerList, "first")).commit();
            }
        });

        final String fullbannerList_second = getSecondFullBanner();
        if (secondslideImage != null && !secondslideImage.equals("") && !secondslideImage.equals("null")) {
            Glide.with(getActivity()).load(secondslideImage).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(secondFullBannerImg);
        }

        FullSecondBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.FullbannerContainer, new FullBanner(fullbannerList_second, "second")).commit();
            }
        });

        final String cardbannerList = getFirstCardBanner();
        if (firstCardImage != null && !firstCardImage.equals("") && !firstCardImage.equals("null")) {
            Glide.with(getActivity()).load(firstCardImage).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(firstCardBannerImage);
        }


        firstBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.fragmentManager.beginTransaction().replace(R.id.CardbannerContainer, new CardBanner(cardbannerList, "first")).commit();
            }
        });

        final String cardbannerList_second = getSecondCardBanner();
        if (secondCardImage != null && !secondCardImage.equals("") && !secondCardImage.equals("null")) {
            Glide.with(getActivity()).load(secondCardImage).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(secondCardBannerImage);
        }

        secondbanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.fragmentManager.beginTransaction().replace(R.id.CardbannerContainer, new CardBanner(cardbannerList_second, "second")).commit();
            }
        });

        customIndicator = (CircleIndicator) v.findViewById(R.id.product_indicator_custom);
        //customIndicator2 = (CircleIndicator) v.findViewById(R.id.product_indicator_custom2);


        Log.e("vCode", AppConfig.vCode + "");
        Log.e("currentCode", AppConfig.current_version + "");

        if (AppConfig.vCode > AppConfig.current_version) {

            AlertDialog alertDialog = new AlertDialog.Builder(AppConfig.act).create();
            alertDialog.setTitle("بابیران به روز رسانی شده است . لطفا نسخه ی جدید را از کافه بازار دریافت نمایید");
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "باشه",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafebazaar.ir/app/net.babiran.app/?l=fa"));
                            startActivity(browserIntent);

                        }
                    });
            alertDialog.show();
        }

        specialRelative.setVisibility(View.VISIBLE);
        proSwipeRefreshLayout.setVisibility(View.GONE);
        topSwipeRefreshLayout.setVisibility(View.GONE);
        disSwipeRefreshLayout.setVisibility(View.GONE);
        TopProList.setVisibility(View.INVISIBLE);
        NewProList.setVisibility(View.INVISIBLE);
        // laytopsell.setVisibility(View.INVISIBLE);
        selltop.setBackgroundResource(R.drawable.background_button_main);
        //layspecial.setVisibility(View.VISIBLE);
        special.setBackgroundResource(R.drawable.background_button_main_selected);
        //laynew.setVisibility(View.INVISIBLE);
        newpro.setBackgroundResource(R.drawable.background_button_main);
        //laydiscount.setVisibility(View.INVISIBLE);
        discount.setBackgroundResource(R.drawable.background_button_main);


        getSlide1();


        getCardsNewPro();
        getCardsTopSell();
        getCardsTopSeen();
        getFooterCards();
        getSmallTileCards();
        getBigTileCards();
        getCardsSpecialPro();
        getCardsDiscountPro();


        v.findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);


        special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specialRelative.setVisibility(View.VISIBLE);
                FullfirstBanner.setVisibility(View.VISIBLE);
                firstBanner.setVisibility(View.VISIBLE);
                secondbanner.setVisibility(View.VISIBLE);
                FullSecondBanner.setVisibility(View.VISIBLE);
                specialLayout.setVisibility(View.VISIBLE);
                specialTondMarketRelative.setVisibility(View.VISIBLE);

                cardTextHome.setText("شاید جالب باشد");
                //laytopsell.setVisibility(View.INVISIBLE);
                selltop.setBackgroundResource(R.drawable.background_button_main);
                //layspecial.setVisibility(View.VISIBLE);
                special.setBackgroundResource(R.drawable.background_button_main_selected);
                //laynew.setVisibility(View.INVISIBLE);
                newpro.setBackgroundResource(R.drawable.background_button_main);
                //laydiscount.setVisibility(View.INVISIBLE);
                discount.setBackgroundResource(R.drawable.background_button_main);

                DisProList.setVisibility(View.INVISIBLE);
                NewProList.setVisibility(View.INVISIBLE);
                TopProList.setVisibility(View.INVISIBLE);
                proSwipeRefreshLayout.setVisibility(View.GONE);
                topSwipeRefreshLayout.setVisibility(View.GONE);
                disSwipeRefreshLayout.setVisibility(View.GONE);

            }
        });

        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // laytopsell.setVisibility(View.INVISIBLE);
                selltop.setBackgroundResource(R.drawable.background_button_main);
                //layspecial.setVisibility(View.INVISIBLE);
                special.setBackgroundResource(R.drawable.background_button_main);
                // laynew.setVisibility(View.INVISIBLE);
                newpro.setBackgroundResource(R.drawable.background_button_main);
                //laydiscount.setVisibility(View.VISIBLE);
                discount.setBackgroundResource(R.drawable.background_button_main_selected);

                proSwipeRefreshLayout.setVisibility(View.GONE);
                topSwipeRefreshLayout.setVisibility(View.GONE);
                disSwipeRefreshLayout.setVisibility(View.VISIBLE);
                NewProList.setVisibility(View.INVISIBLE);
                TopProList.setVisibility(View.INVISIBLE);
                DisProList.setVisibility(View.VISIBLE);

                if (Disproducts.size() > 0) {

                    adpDis = new DisProListAdapter(getActivity(), Disproducts);
                    DisProList.setAdapter(adpDis);
                    DisProList.setOnScrollListener(new AbsListView.OnScrollListener() {

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                                    && (DisProList.getLastVisiblePosition() - DisProList.getHeaderViewsCount() -
                                    DisProList.getFooterViewsCount()) >= (adpDis.getCount() - 1)) {
                                getDisPro();
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        }
                    });
                }

            }
        });


        final boolean b = AppController.getInstance().getSharedPreferences().getBoolean("newPush", false);
        if (b) {

            v.findViewById(R.id.newpro).post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("b====" + b);
                    v.findViewById(R.id.newpro).performClick();

                    SharedPreferences.Editor editor = AppController.getInstance().getSharedPreferences().edit();
                    editor.putBoolean("newPush", false);
                    editor.apply();
                }
            });
        }


        newpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //laytopsell.setVisibility(View.INVISIBLE);
                selltop.setBackgroundResource(R.drawable.background_button_main);
                //layspecial.setVisibility(View.INVISIBLE);
                special.setBackgroundResource(R.drawable.background_button_main);
                //laynew.setVisibility(View.VISIBLE);
                newpro.setBackgroundResource(R.drawable.background_button_main_selected);
                //laydiscount.setVisibility(View.INVISIBLE);
                discount.setBackgroundResource(R.drawable.background_button_main);

                proSwipeRefreshLayout.setVisibility(View.VISIBLE);
                topSwipeRefreshLayout.setVisibility(View.GONE);
                disSwipeRefreshLayout.setVisibility(View.GONE);
                DisProList.setVisibility(View.INVISIBLE);
                TopProList.setVisibility(View.INVISIBLE);
                NewProList.setVisibility(View.VISIBLE);

                if (Newproducts.size() > 0) {
                    adp = new NewProListAdapter(getActivity(), Newproducts);
                    NewProList.setAdapter(adp);
                    //   adp.notifyDataSetChanged();
                    NewProList.setOnScrollListener(new AbsListView.OnScrollListener() {

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                                    && (NewProList.getLastVisiblePosition() - NewProList.getHeaderViewsCount() -
                                    NewProList.getFooterViewsCount()) >= (adp.getCount() - 1)) {

                                getNewPro();
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        }
                    });
                }


            }
        });

        selltop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /*
                specialRelative.setVisibility(View.INVISIBLE);
                topsellRelative.setVisibility(View.VISIBLE);
                seenproRelative.setVisibility(View.INVISIBLE);
                newproRelative.setVisibility(View.INVISIBLE);
                discountRelative.setVisibility(View.INVISIBLE);
                cardTextHome.setText("پر فروش ترین ها");

                */
                //laytopsell.setVisibility(View.VISIBLE);
                selltop.setBackgroundResource(R.drawable.background_button_main_selected);
                //layspecial.setVisibility(View.INVISIBLE);
                special.setBackgroundResource(R.drawable.background_button_main);
                //laynew.setVisibility(View.INVISIBLE);
                newpro.setBackgroundResource(R.drawable.background_button_main);
                // laydiscount.setVisibility(View.INVISIBLE);
                discount.setBackgroundResource(R.drawable.background_button_main);

                proSwipeRefreshLayout.setVisibility(View.GONE);
                topSwipeRefreshLayout.setVisibility(View.VISIBLE);
                disSwipeRefreshLayout.setVisibility(View.GONE);
                DisProList.setVisibility(View.INVISIBLE);
                NewProList.setVisibility(View.INVISIBLE);
                TopProList.setVisibility(View.VISIBLE);

                if (Topproducts.size() > 0) {

                    adpTop = new TopProListAdapter(getActivity(), Topproducts);
                    TopProList.setAdapter(adpTop);
                    TopProList.setOnScrollListener(new AbsListView.OnScrollListener() {

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                                    && (TopProList.getLastVisiblePosition() - TopProList.getHeaderViewsCount() -
                                    TopProList.getFooterViewsCount()) >= (adpTop.getCount() - 1)) {
                                getTopPro();
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        }
                    });
                }

            }
        });

        disSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDisPro();
            }
        });

        proSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewPro();
            }
        });

        topSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTopPro();
            }
        });

        MainActivity.wait.setVisibility(View.INVISIBLE);
        return v;

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    public void getNewPro() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        final String url = AppConfig.BASE_URL + "api/product/getNewProducts/" + Newproducts.size() + "/" + "20";
        System.out.println("getNewPro====" + url);

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("`1`1`1`", response.toString());
                        try {
                            AppConfig.newPro = response;

                            ArrayList<Feature> featuresArray;
                            ArrayList<Image> imagesArray;
                            ArrayList<Moshakhasat> moshakhasatArrayList;
                            ArrayList<Color> colorArrayList;

                            for (int i = 0; i < AppConfig.newPro.length(); i++) {
                                featuresArray = new ArrayList<>();
                                imagesArray = new ArrayList<>();
                                moshakhasatArrayList = new ArrayList<>();
                                colorArrayList = new ArrayList<>();
                                try {
                                    JSONObject c = AppConfig.newPro.getJSONObject(i);

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

                                    if (!c.isNull("moshakhasat")) {
                                        JSONArray jsonArray1 = c.getJSONArray("moshakhasat");

                                        for (int mo = 0; mo < jsonArray1.length(); mo++) {

                                            try {
                                                JSONObject im = jsonArray1.getJSONObject(mo);
                                                Moshakhasat moshakhasat = new Moshakhasat(im.getString("name"), im.getString("val"));
                                                moshakhasatArrayList.add(mo, moshakhasat);
                                            } catch (JSONException ex) {

                                            }
                                        }
                                    }

                                    if (!c.isNull("rang")) {
                                        JSONArray colorJSONArray = c.getJSONArray("rang");
                                        for (int iColor = 0; iColor < colorJSONArray.length(); iColor++) {

                                            try {
                                                JSONObject im = colorJSONArray.getJSONObject(iColor);
                                                Color color = new Color(Util.createTransactionID(), im.getString("name"), im.getString("val"));
                                                colorArrayList.add(iColor, color);
                                            } catch (JSONException ex) {

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    Product product = new Product(c.getString("category_id1"), c.getString("id"), c.getString("name"), c.getString("description"),
                                            c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, moshakhasatArrayList, colorArrayList, c.getString("provider_name"));

                                    Newproducts.add(product);
                                    adp.notifyDataSetChanged();


                                } catch (JSONException e) {

                                    AppConfig.error(e);

                                }
                            }

                            proSwipeRefreshLayout.setRefreshing(false);
                            disSwipeRefreshLayout.setRefreshing(false);
                            topSwipeRefreshLayout.setRefreshing(false);
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
                params.put("offset", String.valueOf(Newproducts.size()));
                params.put("limit", String.valueOf("20"));

                return params;
            }

        };

        queue.add(getRequest);

    }

    public void getTopPro() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        final String url = AppConfig.BASE_URL + "api/product/getTopSells/" + Topproducts.size() + "/" + "20";
        System.out.println("getTopPro====" + url);

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            AppConfig.topsellpro = response;

                            ArrayList<Feature> featuresArray;
                            ArrayList<Image> imagesArray;
                            ArrayList<Moshakhasat> moshakhasatArrayList;
                            ArrayList<Color> colorArrayList;

                            for (int i = 0; i < AppConfig.topsellpro.length(); i++) {
                                featuresArray = new ArrayList<>();
                                imagesArray = new ArrayList<>();
                                moshakhasatArrayList = new ArrayList<>();
                                colorArrayList = new ArrayList<>();
                                try {
                                    JSONObject c = AppConfig.topsellpro.getJSONObject(i);

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


                                    if (!c.isNull("moshakhasat")) {
                                        JSONArray jsonArray1 = c.getJSONArray("moshakhasat");

                                        for (int mo = 0; mo < jsonArray1.length(); mo++) {

                                            try {
                                                JSONObject im = jsonArray1.getJSONObject(mo);
                                                Moshakhasat moshakhasat = new Moshakhasat(im.getString("name"), im.getString("val"));
                                                moshakhasatArrayList.add(mo, moshakhasat);
                                            } catch (JSONException ex) {

                                            }
                                        }
                                    }

                                    if (!c.isNull("rang")) {
                                        JSONArray colorJSONArray = c.getJSONArray("rang");
                                        for (int iColor = 0; iColor < colorJSONArray.length(); iColor++) {

                                            try {
                                                JSONObject im = colorJSONArray.getJSONObject(iColor);
                                                Color color = new Color(Util.createTransactionID(), im.getString("name"), im.getString("val"));
                                                colorArrayList.add(iColor, color);
                                            } catch (JSONException ex) {

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    Product product = new Product(c.getString("category_id1"), c.getString("id"), c.getString("name"), c.getString("description"),
                                            c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, moshakhasatArrayList, colorArrayList, c.getString("provider_name"));

                                    Topproducts.add(product);
                                    adpTop.notifyDataSetChanged();


                                } catch (JSONException e) {

                                    AppConfig.error(e);

                                }
                            }

                            proSwipeRefreshLayout.setRefreshing(false);
                            disSwipeRefreshLayout.setRefreshing(false);
                            topSwipeRefreshLayout.setRefreshing(false);

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
                params.put("offset", String.valueOf(Topproducts.size()));
                params.put("limit", String.valueOf("20"));

                return params;
            }

        };

        queue.add(getRequest);
    }

    public void getDisPro() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        final String url = AppConfig.BASE_URL + "api/product/getTopSeenLazy/20/" + Disproducts.size();
        System.out.println("getDisPro====" + url);

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            AppConfig.topseenPro = response;

                            ArrayList<Feature> featuresArray;
                            ArrayList<Image> imagesArray;
                            ArrayList<Moshakhasat> moshakhasatArrayList;
                            ArrayList<Color> colorArrayList;

                            for (int i = 0; i < AppConfig.topseenPro.length(); i++) {
                                featuresArray = new ArrayList<>();
                                imagesArray = new ArrayList<>();
                                moshakhasatArrayList = new ArrayList<>();
                                colorArrayList = new ArrayList<>();
                                try {
                                    JSONObject c = AppConfig.topseenPro.getJSONObject(i);

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


                                    if (!c.isNull("moshakhasat")) {
                                        JSONArray jsonArray1 = c.getJSONArray("moshakhasat");

                                        for (int mo = 0; mo < jsonArray1.length(); mo++) {

                                            try {
                                                JSONObject im = jsonArray1.getJSONObject(mo);
                                                Moshakhasat moshakhasat = new Moshakhasat(im.getString("name"), im.getString("val"));
                                                moshakhasatArrayList.add(mo, moshakhasat);
                                            } catch (JSONException ex) {

                                            }
                                        }
                                    }

                                    if (!c.isNull("rang")) {
                                        JSONArray colorJSONArray = c.getJSONArray("rang");
                                        for (int iColor = 0; iColor < colorJSONArray.length(); iColor++) {

                                            try {
                                                JSONObject im = colorJSONArray.getJSONObject(iColor);
                                                Color color = new Color(Util.createTransactionID(), im.getString("name"), im.getString("val"));
                                                colorArrayList.add(iColor, color);
                                            } catch (JSONException ex) {

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    Product product = new Product(c.getString("category_id1"), c.getString("id"), c.getString("name"), c.getString("description"),
                                            c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, moshakhasatArrayList, colorArrayList, c.getString("provider_name"));

                                    Disproducts.add(product);
                                    adpDis.notifyDataSetChanged();


                                } catch (JSONException e) {

                                    AppConfig.error(e);

                                }
                            }

                            disSwipeRefreshLayout.setRefreshing(false);
                            proSwipeRefreshLayout.setRefreshing(false);
                            topSwipeRefreshLayout.setRefreshing(false);

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
                params.put("offset", String.valueOf(Disproducts.size()));
                params.put("limit", String.valueOf("20"));

                return params;
            }

        };

        queue.add(getRequest);
    }

    public void getSlide1() {

        try {
            mCustomPagerAdapterByUrlMain = new CustomPagerAdapterByUrlMain(getActivity());
            for (int i = 0; i < AppConfig.slides1.length(); i++) {
                try {
                    JSONObject c = AppConfig.slides1.getJSONObject(i);
                    mCustomPagerAdapterByUrlMain.ids.add(c.getString("id"));
                    mCustomPagerAdapterByUrlMain.subject.add(c.getString("subject"));
                    mCustomPagerAdapterByUrlMain.description.add(c.getString("description"));
                    mCustomPagerAdapterByUrlMain.links.add(c.getString("link"));
                    mCustomPagerAdapterByUrlMain.imageLink.add(c.getString("image_link"));
                    mCustomPagerAdapterByUrlMain.whichSlide.add(c.getString("which_slide"));
                    mCustomPagerAdapterByUrlMain.isActive.add(c.getString("is_active"));
                    mCustomPagerAdapterByUrlMain.createdAtJalali.add(c.getString("created_at_jalali"));
                    mCustomPagerAdapterByUrlMain.updatedAtJalali.add(c.getString("updated_at_jalali"));
                    mCustomPagerAdapterByUrlMain.createdAt.add(c.getString("created_at"));
                    mCustomPagerAdapterByUrlMain.updateAt.add(c.getString("updated_at"));


                    //Log.e("jsonObject", c.toString());

                } catch (JSONException e) {

                    Log.e("jsonObjectEroor", e.toString());
                    AppConfig.error(e);

                }
            }

            viewPager.setAdapter(mCustomPagerAdapterByUrlMain);
            customIndicator.setViewPager(viewPager);


            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (first) {
                        first = false;
                    } else if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1) {
                        viewPager.setCurrentItem(0);
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                    }

                }
            };

            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 100, 5000);


        } catch (Exception ex) {
            Log.e("slides fucking ... ", ex.getMessage());
            AppConfig.error(ex);
        }

    }

    public String getFirstFullBanner() {

        String id = "";
        Category category = null;

        try {

            if (AppConfig.fullbanner.getJSONObject(0) != null) {
                JSONObject c = AppConfig.fullbanner.getJSONObject(0);
                firstslideImage = c.getString("slide_image");


                category = new Category(c.getString("id"), c.getString("name"), c.getString("parent_id")
                        , c.getString("icon"), c.getString("slide_image"));
            }

        } catch (JSONException e) {
            AppConfig.error(e);
        }
        if (category != null) {
            id = category.id;
        }

        return id;
    }

    public String getSecondFullBanner() {


        String id = "";
        Category category = null;

        try {
            JSONObject c = AppConfig.fullbanner.getJSONObject(1);
            secondslideImage = c.getString("slide_image");

            category = new Category(c.getString("id"), c.getString("name"), c.getString("parent_id")
                    , c.getString("icon"), c.getString("slide_image"));
        } catch (JSONException e) {
            AppConfig.error(e);
        }

        if (category != null) {
            id = category.id;
        }
        return id;
    }

    public String getFirstCardBanner() {

        String id = "";
        Category category = null;

        try {
            JSONObject c = AppConfig.cardbanner.getJSONObject(0);
            firstCardImage = c.getString("slide_image");

            category = new Category(c.getString("id"), c.getString("name"), c.getString("parent_id")
                    , c.getString("icon"), c.getString("slide_image"));
        } catch (JSONException e) {
            AppConfig.error(e);
        }

        if (category != null) {
            id = category.id;
        }

        return id;
    }

    public String getSecondCardBanner() {


        String id = "";
        Category category = null;

        try {
            JSONObject c = AppConfig.cardbanner.getJSONObject(1);
            secondCardImage = c.getString("slide_image");

            category = new Category(c.getString("id"), c.getString("name"), c.getString("parent_id")
                    , c.getString("icon"), c.getString("slide_image"));
        } catch (JSONException e) {
            AppConfig.error(e);
        }

        if (category != null) {
            id = category.id;
        }

        return id;
    }

    public boolean first = true;

    public void getCardsTopSell() {

        ArrayList<Feature> featuresArray;
        ArrayList<Image> imagesArray;
        ArrayList<Moshakhasat> moshakhasatArrayList;
        ArrayList<Color> colorArrayList;

        cards = new ArrayList<>();
        for (int i = 0; i < AppConfig.topsellpro.length(); i++) {

            featuresArray = new ArrayList<>();
            imagesArray = new ArrayList<>();
            moshakhasatArrayList = new ArrayList<>();
            colorArrayList = new ArrayList<>();
            try {
                JSONObject c = AppConfig.topsellpro.getJSONObject(i);
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

                if (!c.isNull("moshakhasat")) {
                    JSONArray jsonArray1 = c.getJSONArray("moshakhasat");

                    for (int mo = 0; mo < jsonArray1.length(); mo++) {

                        try {
                            JSONObject im = jsonArray1.getJSONObject(mo);
                            Moshakhasat moshakhasat = new Moshakhasat(im.getString("name"), im.getString("val"));
                            moshakhasatArrayList.add(mo, moshakhasat);
                        } catch (JSONException ex) {

                        }
                    }
                }

                if (!c.isNull("rang")) {
                    JSONArray colorJSONArray = c.getJSONArray("rang");
                    for (int iColor = 0; iColor < colorJSONArray.length(); iColor++) {

                        try {
                            JSONObject im = colorJSONArray.getJSONObject(iColor);
                            Color color = new Color(Util.createTransactionID(), im.getString("name"), im.getString("val"));
                            colorArrayList.add(iColor, color);
                        } catch (JSONException ex) {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                Product product = new Product(c.getString("category_id1"), c.getString("id"), c.getString("name"), c.getString("description"),
                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, moshakhasatArrayList, colorArrayList, c.getString("provider_name"));


                Topproducts.add(product);


            } catch (JSONException e) {

                AppConfig.error(e);

            }
        }

        v.findViewById(R.id.progressBarCircularIndeterminate1).setVisibility(View.GONE);


    }

    public void getCardsTopSeen() {

        ArrayList<Feature> featuresArray;
        ArrayList<Image> imagesArray;
        ArrayList<Moshakhasat> moshakhasatArrayList;
        ArrayList<Color> colorArrayList;

        for (int i = 0; i < AppConfig.topseenPro.length(); i++) {
            featuresArray = new ArrayList<>();
            imagesArray = new ArrayList<>();
            moshakhasatArrayList = new ArrayList<>();
            colorArrayList = new ArrayList<>();
            try {
                JSONObject c = AppConfig.topseenPro.getJSONObject(i);

                System.out.println("ccccccccccccccc====" + c.toString());

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


                if (!c.isNull("moshakhasat")) {
                    JSONArray jsonArray = c.getJSONArray("moshakhasat");

                    for (int mo = 0; mo < jsonArray.length(); mo++) {

                        JSONObject im = jsonArray.getJSONObject(mo);
                        Moshakhasat moshakhasat = new Moshakhasat();
                        moshakhasat.setName(im.getString("name"));
                        moshakhasat.setValue(im.getString("val"));
                        moshakhasatArrayList.add(mo, moshakhasat);
                    }
                }


                if (!c.isNull("rang")) {
                    JSONArray colorJSONArray = c.getJSONArray("rang");
                    for (int iColor = 0; iColor < colorJSONArray.length(); iColor++) {

                        try {
                            JSONObject im = colorJSONArray.getJSONObject(iColor);
                            Color color = new Color(Util.createTransactionID(), im.getString("name"), im.getString("val"));
                            colorArrayList.add(iColor, color);
                        } catch (JSONException ex) {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println();
                Product product = new Product(c.getString("category_id1"), c.getString("id"), c.getString("name"), c.getString("description"),
                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, moshakhasatArrayList, colorArrayList, c.getString("provider_name"));
                System.out.println("product====" + product.getName());
                System.out.println("product====" + product.getDis_price());
                System.out.println("product====" + product.getProviderName());
                System.out.println("product====" + product.getStock());
                Card card = new Card(AppConfig.act, product);
                cards.add(card);
                secondHorizontal.addView(card);

            } catch (JSONException e) {

                AppConfig.error(e);

            }
        }

        v.findViewById(R.id.SecondprogressBarCircularIndeterminate).setVisibility(View.GONE);
    }

    public void getCardsNewPro() {

        ArrayList<Feature> featuresArray;
        ArrayList<Image> imagesArray;
        ArrayList<Moshakhasat> moshakhasatArrayList;
        ArrayList<Color> colorArrayList;

        //  cards = new ArrayList<>();
        for (int i = 0; i < AppConfig.newPro.length(); i++) {

            featuresArray = new ArrayList<>();
            imagesArray = new ArrayList<>();
            moshakhasatArrayList = new ArrayList<>();
            colorArrayList = new ArrayList<>();
            try {
                JSONObject c = AppConfig.newPro.getJSONObject(i);
                System.out.println("ccccccc===" + c);
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

                if (!c.isNull("moshakhasat")) {
                    JSONArray jsonArray1 = c.getJSONArray("moshakhasat");

                    for (int mo = 0; mo < jsonArray1.length(); mo++) {

                        try {
                            JSONObject im = jsonArray1.getJSONObject(mo);
                            Moshakhasat moshakhasat = new Moshakhasat(im.getString("name"), im.getString("val"));
                            moshakhasatArrayList.add(mo, moshakhasat);
                        } catch (JSONException ex) {

                        }
                    }
                }

                if (!c.isNull("rang")) {
                    JSONArray colorJSONArray = c.getJSONArray("rang");
                    for (int iColor = 0; iColor < colorJSONArray.length(); iColor++) {

                        try {
                            JSONObject im = colorJSONArray.getJSONObject(iColor);
                            Color color = new Color(Util.createTransactionID(), im.getString("name"), im.getString("val"));
                            colorArrayList.add(iColor, color);
                        } catch (JSONException ex) {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                Product product = new Product(c.getString("category_id1"), c.getString("id"), c.getString("name"), c.getString("description"),
                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, moshakhasatArrayList, colorArrayList, c.getString("provider_name"));

                Newproducts.add(product);

            } catch (JSONException e) {

                AppConfig.error(e);

            }
        }


    }

    public void getCardsSpecialPro() {
        ArrayList<Feature> featuresArray;
        ArrayList<Image> imagesArray;
        ArrayList<Moshakhasat> moshakhasatArrayList;
        ArrayList<Color> colorArrayList;

        for (int i = 0; i < AppConfig.specialPro.length(); i++) {
            featuresArray = new ArrayList<>();
            imagesArray = new ArrayList<>();
            moshakhasatArrayList = new ArrayList<>();
            colorArrayList = new ArrayList<>();
            try {
                JSONObject c = AppConfig.specialPro.getJSONObject(i);
                System.out.println("Special=====" + c.toString());

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

                if (!c.isNull("moshakhasat")) {
                    JSONArray jsonArray = c.getJSONArray("moshakhasat");

                    for (int mo = 0; mo < jsonArray.length(); mo++) {

                        JSONObject im = jsonArray.getJSONObject(mo);
                        Moshakhasat moshakhasat = new Moshakhasat();
                        moshakhasat.setName(im.getString("name"));
                        moshakhasat.setValue(im.getString("val"));
                        moshakhasatArrayList.add(mo, moshakhasat);
                    }
                }


                if (!c.isNull("rang")) {
                    JSONArray colorJSONArray = c.getJSONArray("rang");
                    for (int iColor = 0; iColor < colorJSONArray.length(); iColor++) {

                        try {
                            JSONObject im = colorJSONArray.getJSONObject(iColor);
                            Color color = new Color(Util.createTransactionID(), im.getString("name"), im.getString("val"));
                            colorArrayList.add(iColor, color);
                        } catch (JSONException ex) {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


                //System.out.println("homecategory_id1==" + c.getString("category_id1"));
                Product product = new Product(c.getString("category_id1"), c.getString("id"), c.getString("name"), c.getString("description"),
                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, moshakhasatArrayList, colorArrayList, c.getString("provider_name"));


                Card card = new Card(AppConfig.act, product);
                cards.add(card);
                horizontal1.addView(card);

            } catch (JSONException e) {

                e.printStackTrace();
                AppConfig.error(e);

            }
        }

        v.findViewById(R.id.progressBarCircularIndeterminate1).setVisibility(View.GONE);
        //  v.findViewById(R.id.progressLayout).setVisibility(View.GONE);
    }

    public void getCardsDiscountPro() {
        ArrayList<Feature> featuresArray;
        ArrayList<Image> imagesArray;
        ArrayList<Moshakhasat> moshakhasatArrayList;
        ArrayList<Color> colorArrayList;

        for (int i = 0; i < AppConfig.topseenPro.length(); i++) {
            featuresArray = new ArrayList<>();
            imagesArray = new ArrayList<>();
            moshakhasatArrayList = new ArrayList<>();
            colorArrayList = new ArrayList<>();
            try {
                JSONObject c = AppConfig.topseenPro.getJSONObject(i);

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

                if (!c.isNull("moshakhasat")) {
                    JSONArray jsonArray1 = c.getJSONArray("moshakhasat");

                    for (int mo = 0; mo < jsonArray1.length(); mo++) {

                        try {
                            JSONObject im = jsonArray1.getJSONObject(mo);
                            Moshakhasat moshakhasat = new Moshakhasat(im.getString("name"), im.getString("val"));
                            moshakhasatArrayList.add(mo, moshakhasat);
                        } catch (JSONException ex) {

                        }
                    }
                }

                if (!c.isNull("rang")) {
                    JSONArray colorJSONArray = c.getJSONArray("rang");
                    for (int iColor = 0; iColor < colorJSONArray.length(); iColor++) {

                        try {
                            JSONObject im = colorJSONArray.getJSONObject(iColor);
                            Color color = new Color(Util.createTransactionID(), im.getString("name"), im.getString("val"));
                            colorArrayList.add(iColor, color);
                        } catch (JSONException ex) {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                Product product = new Product(c.getString("category_id1"), c.getString("id"), c.getString("name"), c.getString("description"),
                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, moshakhasatArrayList, colorArrayList, c.getString("provider_name"));

                Disproducts.add(product);

            } catch (JSONException e) {

                AppConfig.error(e);

            }
        }

    }

    public void getFooterCards() {

        String id = "";
        Category category = null;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(mLayoutManager);

        for (int i = 0; i < AppConfig.footerBanner.length(); i++) {
            try {
                JSONObject c = AppConfig.footerBanner.getJSONObject(i);
                category = new Category(c.getString("id"), c.getString("name"), c.getString("parent_id")
                        , c.getString("icon"), c.getString("slide_image"));

                footerCategories.add(category);

            } catch (JSONException e) {

                AppConfig.error(e);
            }
        }

        recycler_view.setAdapter(new FooterAdapter(getActivity(), footerCategories));

    }

    public void getSmallTileCards() {

        String id = "";
        Category category = null;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        recycler_view_smallTile.setLayoutManager(mLayoutManager);

        for (int i = 0; i < AppConfig.smallTile.length(); i++) {
            try {
                JSONObject c = AppConfig.smallTile.getJSONObject(i);
                category = new Category(c.getString("id"), c.getString("name"), c.getString("parent_id")
                        , c.getString("icon"), c.getString("slide_image"));

                smallTileCategories.add(category);

            } catch (JSONException e) {

                AppConfig.error(e);
            }
        }

        recycler_view_smallTile.setAdapter(new SmallTileAdapter(getActivity(), smallTileCategories));
    }

    public void getBigTileCards() {

        Category category = null;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        recycler_view_bigTile.setLayoutManager(mLayoutManager);

        for (int i = 0; i < AppConfig.bigTile.length(); i++) {
            try {
                JSONObject c = AppConfig.bigTile.getJSONObject(i);
                category = new Category(c.getString("id"), c.getString("name"), c.getString("parent_id")
                        , c.getString("icon"), c.getString("slide_image"));

                bigTileCategories.add(category);

            } catch (JSONException e) {

                AppConfig.error(e);
            }
        }

        recycler_view_bigTile.setAdapter(new BigTileAdapter(getActivity(), bigTileCategories));
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.layout_search.setVisibility(View.VISIBLE);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (MainActivity.productlist.getVisibility() == View.VISIBLE) {
                        MainActivity.productlist.setVisibility(View.INVISIBLE);

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AppConfig.act);
                        builder.setTitle("می خواهید خارج شوید؟");
                        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (AppConfig.checkReciveSms == true) {
                                    AppConfig.checkReciveSms = false;
                                }
                                if (AppConfig.btnSubmitOk == true) {
                                    AppConfig.btnSubmitOk = false;
                                }

                                AppConfig.act.finish();


                                dialog.dismiss();
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
                    }

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
