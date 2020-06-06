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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.DisProListAdapter;
import Adapters.NewProListAdapter;
import Adapters.TopProListAdapter;
import Handlers.DatabaseHandler;
import Models.Category;
import Models.Feature;
import Models.Image;
import Models.Product;
import me.relex.circleindicator.CircleIndicator;
import tools.AppConfig;
import tools.CustomPagerAdapterByUrlMain;
import ui_elements.Card;
import ui_elements.FooterCard;
import ui_elements.MyTextView;

import static tools.AppConfig.fragmentManager;
import static tools.AppConfig.smallTile;


public class HomeFragment extends Fragment {
    ViewPager viewPager, viewPager2;
    CircleIndicator customIndicator, customIndicator2;
    CardView firstBanner, secondbanner, firstcardban, secondcardban, thirdcardban, forthcardban, FullfirstBanner, FullSecondBanner, dualfirstcard, dualsecondcard, specialLayout;
    RelativeLayout relativebanner, secondrelative, dualcardrelative, specialTondMarketRelative, footer_relativeLayout,special,discount,newpro,selltop;

    public LinearLayout horizontal1, horizontal2, secondHorizontal, footer_linearLayout;


    public ArrayList<Card> cards;
    public ArrayList<FooterCard> footerCards;
    public CustomPagerAdapterByUrlMain mCustomPagerAdapterByUrlMain, mCustomPagerAdapterByUrlMain2;

    NewProListAdapter adp;
    TopProListAdapter adpTop;
    DisProListAdapter adpDis;

    MyTextView cardTextHome, HourTxt, MinTxt, SecTxt;
    RelativeLayout specialRelative;
    LinearLayout layspecial, laynew, laydiscount, laytopsell;
    ImageView firstFullBannerImg, secondFullBannerImg, firstCardBannerImage, secondCardBannerImage, firstBigTile, secondBigTile, firstSmallTile, secondSmallTile, thirdSmallTile, fourthSmallTile;

    public ArrayList<Product> Newproducts = new ArrayList<>();
    public ArrayList<Product> Topproducts = new ArrayList<>();
    public ArrayList<Product> Disproducts = new ArrayList<>();


    String firstslideImage = "";
    String secondslideImage = "";
    String firstCardImage = "";
    String secondCardImage = "";
    String FirstBigTile = "";
    String SecondBigTile = "";
    String FirstSmallTile = "";
    String SecondSmallTile = "";
    String ThirdSmallTile = "";
    String FourthSmallTile = "";


    ListView NewProList, TopProList, DisProList;
    RequestQueue queue;
    public static final String TAG = "TAG";


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

        relativebanner = (RelativeLayout) v.findViewById(R.id.relativebanner);
        secondrelative = (RelativeLayout) v.findViewById(R.id.secondrelativebanner);
        dualcardrelative = (RelativeLayout) v.findViewById(R.id.DualCard);

        specialTondMarketRelative = (RelativeLayout) v.findViewById(R.id.specialTondMarketRelative);
        footer_relativeLayout = (RelativeLayout) v.findViewById(R.id.footer_relativeLayout);
        special = v.findViewById(R.id.special);
        discount = v.findViewById(R.id.discount);
        newpro = v.findViewById(R.id.newpro);
        selltop = v.findViewById(R.id.selltop);

        firstBanner = (CardView) v.findViewById(R.id.firstbanner);
        secondbanner = (CardView) v.findViewById(R.id.secondbanner);

        firstcardban = (CardView) v.findViewById(R.id.ban1);
        secondcardban = (CardView) v.findViewById(R.id.ban2);
        thirdcardban = (CardView) v.findViewById(R.id.ban3);
        forthcardban = (CardView) v.findViewById(R.id.ban4);


        dualfirstcard = (CardView) v.findViewById(R.id.Dualban1);
        dualsecondcard = (CardView) v.findViewById(R.id.Dualban2);

        specialLayout = (CardView) v.findViewById(R.id.specialLayout);

        firstFullBannerImg = (ImageView) v.findViewById(R.id.imgfirstFullban1);
        secondFullBannerImg = (ImageView) v.findViewById(R.id.imgsecondFullban1);
        firstCardBannerImage = (ImageView) v.findViewById(R.id.imgfirstban1);
        secondCardBannerImage = (ImageView) v.findViewById(R.id.imgsecondban2);

        firstBigTile = (ImageView) v.findViewById(R.id.imgdualban1);
        secondBigTile = (ImageView) v.findViewById(R.id.imgdualban2);

        firstSmallTile = (ImageView) v.findViewById(R.id.imgban1);
        secondSmallTile = (ImageView) v.findViewById(R.id.imgban2);
        thirdSmallTile = (ImageView) v.findViewById(R.id.imgban3);
        fourthSmallTile = (ImageView) v.findViewById(R.id.imgban4);

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
        footer_linearLayout = (LinearLayout) v.findViewById(R.id.footer_linearLayout);


        cardTextHome = (MyTextView) v.findViewById(R.id.cardtextHome);
        HourTxt = (MyTextView) v.findViewById(R.id.hourTxt);
        MinTxt = (MyTextView) v.findViewById(R.id.minTxt);
        SecTxt = (MyTextView) v.findViewById(R.id.secTxt);

        disSwipeRefreshLayout = v.findViewById(R.id.disSwipeRefreshLayout);
        proSwipeRefreshLayout = v.findViewById(R.id.proSwipeRefreshLayout);
        topSwipeRefreshLayout = v.findViewById(R.id.topSwipeRefreshLayout);

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        viewPager.getLayoutParams().height = width / 2;
        //  viewPager2.getLayoutParams().height = width / 2;

        relativebanner.getLayoutParams().height = width / 3;
        secondrelative.getLayoutParams().height = width / 3;
        dualcardrelative.getLayoutParams().height = (int) (width * 0.50);

        firstBanner.getLayoutParams().height = width / 2;
        // firstBanner.getLayoutParams().width = (int) (width * 0.92);
        secondbanner.getLayoutParams().height = width / 2;
        secondbanner.getLayoutParams().width = (int) (width * 0.92);


        FullfirstBanner.getLayoutParams().height = width / 2;

        FullSecondBanner.getLayoutParams().height = width / 2;
        FullSecondBanner.getLayoutParams().width = (int) (width * 0.92);
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


        firstcardban.getLayoutParams().width = (int) (width * 0.45);
        secondcardban.getLayoutParams().width = (int) (width * 0.45);

        thirdcardban.getLayoutParams().width = (int) (width * 0.45);
        forthcardban.getLayoutParams().width = (int) (width * 0.45);

        dualfirstcard.getLayoutParams().width = (int) (width * 0.45);
        dualsecondcard.getLayoutParams().width = (int) (width * 0.45);


        final String DualcardList = getFirstBigTile();
        if (FirstBigTile != null && !FirstBigTile.equals("") && !FirstBigTile.equals("null")) {
            Glide.with(getActivity()).load(FirstBigTile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(firstBigTile);
        }
        dualfirstcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.BigTileContainer, new BigTile(DualcardList, "first")).commit();
            }
        });

        final String DualcardList_second = getSecondBigTile();
        if (SecondBigTile != null && !SecondBigTile.equals("") && !SecondBigTile.equals("null")) {
            Glide.with(getActivity()).load(SecondBigTile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(secondBigTile);
        }
        dualsecondcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.BigTileContainer, new BigTile(DualcardList_second, "second")).commit();
            }
        });


        final String smallTile_first = getFirstSmallTile();
        if (FirstSmallTile != null && !FirstSmallTile.equals("") && !FirstSmallTile.equals("null")) {
            Glide.with(getActivity()).load(FirstSmallTile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(firstSmallTile);
        }
        firstcardban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.SmallTileContainer, new SmallTile(smallTile_first, "first")).commit();
            }
        });

        final String smallTile_second = getSecondSmallTile();
        if (SecondSmallTile != null && !SecondSmallTile.equals("") && !SecondSmallTile.equals("null")) {
            Glide.with(getActivity()).load(SecondSmallTile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(secondSmallTile);
        }
        secondcardban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.SmallTileContainer, new SmallTile(smallTile_second, "second")).commit();
            }
        });

        final String smallTile_third = getThirdSmallTile();
        if (ThirdSmallTile != null && !ThirdSmallTile.equals("") && !ThirdSmallTile.equals("null")) {
            Glide.with(getActivity()).load(ThirdSmallTile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(thirdSmallTile);
        }
        thirdcardban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.SmallTileContainer, new SmallTile(smallTile_third, "third")).commit();
            }
        });

        final String smallTile_fourth = getFourthSmallTile();
        if (FourthSmallTile != null && !FourthSmallTile.equals("") && !FourthSmallTile.equals("null")) {
            Glide.with(getActivity()).load(FourthSmallTile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(fourthSmallTile);
        }
        forthcardban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.SmallTileContainer, new SmallTile(smallTile_fourth, "fourth")).commit();
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
        selltop.setBackgroundResource(R.drawable.tab_background);
        //layspecial.setVisibility(View.VISIBLE);
        special.setBackgroundResource(R.drawable.tab_background_selected);
        //laynew.setVisibility(View.INVISIBLE);
        newpro.setBackgroundResource(R.drawable.tab_background);
        //laydiscount.setVisibility(View.INVISIBLE);
        discount.setBackgroundResource(R.drawable.tab_background);


        getSlide1();


        getCardsNewPro();
        getCardsTopSell();
        getCardsTopSeen();
        getFooterCards();
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
                relativebanner.setVisibility(View.VISIBLE);
                secondrelative.setVisibility(View.VISIBLE);
                dualcardrelative.setVisibility(View.VISIBLE);
                FullSecondBanner.setVisibility(View.VISIBLE);
                specialLayout.setVisibility(View.VISIBLE);
                specialTondMarketRelative.setVisibility(View.VISIBLE);
                footer_relativeLayout.setVisibility(View.VISIBLE);

                cardTextHome.setText("شاید جالب باشد");
                //laytopsell.setVisibility(View.INVISIBLE);
                selltop.setBackgroundResource(R.drawable.tab_background);
                //layspecial.setVisibility(View.VISIBLE);
                special.setBackgroundResource(R.drawable.tab_background_selected);
                //laynew.setVisibility(View.INVISIBLE);
                newpro.setBackgroundResource(R.drawable.tab_background);
                //laydiscount.setVisibility(View.INVISIBLE);
                discount.setBackgroundResource(R.drawable.tab_background);

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
                selltop.setBackgroundResource(R.drawable.tab_background);
                //layspecial.setVisibility(View.INVISIBLE);
                special.setBackgroundResource(R.drawable.tab_background);
               // laynew.setVisibility(View.INVISIBLE);
                newpro.setBackgroundResource(R.drawable.tab_background);
                //laydiscount.setVisibility(View.VISIBLE);
                discount.setBackgroundResource(R.drawable.tab_background_selected);

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
                selltop.setBackgroundResource(R.drawable.tab_background);
                //layspecial.setVisibility(View.INVISIBLE);
                special.setBackgroundResource(R.drawable.tab_background);
                //laynew.setVisibility(View.VISIBLE);
                newpro.setBackgroundResource(R.drawable.tab_background_selected);
                //laydiscount.setVisibility(View.INVISIBLE);
                discount.setBackgroundResource(R.drawable.tab_background);

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
                selltop.setBackgroundResource(R.drawable.tab_background_selected);
                //layspecial.setVisibility(View.INVISIBLE);
                special.setBackgroundResource(R.drawable.tab_background);
                //laynew.setVisibility(View.INVISIBLE);
                newpro.setBackgroundResource(R.drawable.tab_background);
               // laydiscount.setVisibility(View.INVISIBLE);
                discount.setBackgroundResource(R.drawable.tab_background);

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

                            for (int i = 0; i < AppConfig.newPro.length(); i++) {
                                featuresArray = new ArrayList<>();
                                imagesArray = new ArrayList<>();
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

                                    Product product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                            c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, c.getString("provider_name"));


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

                            for (int i = 0; i < AppConfig.topsellpro.length(); i++) {
                                featuresArray = new ArrayList<>();
                                imagesArray = new ArrayList<>();
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

                                    Product product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                            c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, c.getString("provider_name"));


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

                            for (int i = 0; i < AppConfig.topseenPro.length(); i++) {
                                featuresArray = new ArrayList<>();
                                imagesArray = new ArrayList<>();
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

                                    Product product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                            c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, c.getString("provider_name"));


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
            JSONObject c = AppConfig.fullbanner.getJSONObject(0);
            firstslideImage = c.getString("slide_image");


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

    public String getFirstBigTile() {


        String id = "";
        Category category = null;

        try {
            JSONObject c = AppConfig.bigTile.getJSONObject(0);
            FirstBigTile = c.getString("icon");

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

    public String getSecondBigTile() {


        String id = "";
        Category category = null;

        try {
            JSONObject c = AppConfig.bigTile.getJSONObject(1);
            SecondBigTile = c.getString("icon");

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

    public String getFirstSmallTile() {

        String id = "";
        Category category = null;

        try {
            JSONObject c = smallTile.getJSONObject(0);
            FirstSmallTile = c.getString("icon");

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

    public String getSecondSmallTile() {


        String id = "";
        Category category = null;

        try {
            JSONObject c = smallTile.getJSONObject(1);
            SecondSmallTile = c.getString("icon");

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

    public String getThirdSmallTile() {


        String id = "";
        Category category = null;

        try {
            JSONObject c = smallTile.getJSONObject(2);
            ThirdSmallTile = c.getString("icon");

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

    public String getFourthSmallTile() {

        String id = "";
        Category category = null;

        try {
            JSONObject c = smallTile.getJSONObject(3);
            FourthSmallTile = c.getString("icon");

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

        cards = new ArrayList<>();
        for (int i = 0; i < AppConfig.topsellpro.length(); i++) {

            featuresArray = new ArrayList<>();
            imagesArray = new ArrayList<>();
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

                Product product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, c.getString("provider_name"));


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

        for (int i = 0; i < AppConfig.topseenPro.length(); i++) {
            featuresArray = new ArrayList<>();
            imagesArray = new ArrayList<>();
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

                Product product = new Product(c.getString("category_id1"),c.getString("id"), c.getString("name"), c.getString("description"),
                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, c.getString("provider_name"));


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

        //  cards = new ArrayList<>();
        for (int i = 0; i < AppConfig.newPro.length(); i++) {

            featuresArray = new ArrayList<>();
            imagesArray = new ArrayList<>();
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


                Newproducts.add(product);

            } catch (JSONException e) {

                AppConfig.error(e);

            }
        }


    }

    public void getCardsSpecialPro() {
        ArrayList<Feature> featuresArray;
        ArrayList<Image> imagesArray;

        for (int i = 0; i < AppConfig.specialPro.length(); i++) {
            featuresArray = new ArrayList<>();
            imagesArray = new ArrayList<>();
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


                //System.out.println("homecategory_id1==" + c.getString("category_id1"));
                Product product = new Product(c.getString("category_id1"),c.getString("id"), c.getString("name"), c.getString("description"),
                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, c.getString("provider_name"));


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

        for (int i = 0; i < AppConfig.topseenPro.length(); i++) {
            featuresArray = new ArrayList<>();
            imagesArray = new ArrayList<>();
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

                Product product = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, c.getString("provider_name"));


                Disproducts.add(product);

            } catch (JSONException e) {

                AppConfig.error(e);

            }
        }

    }

    public String getFooterCards() {

        String id = "";
        Category category = null;

        for (int i = 0; i < AppConfig.footerBanner.length(); i++) {
            try {
                JSONObject c = AppConfig.footerBanner.getJSONObject(i);
                System.out.println("getFooterCards=" + c);
                firstCardImage = c.getString("slide_image");

                category = new Category(c.getString("id"), c.getString("name"), c.getString("parent_id")
                        , c.getString("icon"), c.getString("slide_image"));

                FooterCard card = new FooterCard(AppConfig.act, category);
                footer_linearLayout.addView(card);

            } catch (JSONException e) {

                AppConfig.error(e);
            }
        }
        if (category != null) {
            id = category.id;
        }

        return id;
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
