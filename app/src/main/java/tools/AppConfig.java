package tools;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import net.babiran.app.Servic.GETING;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Models.Advertising;
import Models.Category;
import Models.Product;


public class AppConfig {

    public static String BASE_URL = "http://babiran.net/";
    public static FragmentManager fragmentManager;
    public static FragmentTransaction ft;
    public static ArrayList<Category> total_categories = new ArrayList<>();

    public static String NULLBASKET = "";
    public static String phone = "";
    public static String code = "";
    public static String id = "";
    public static String city_id = "-1";
    public static String province_id = "-1";
    public static String BACK_TO_LIOST = "0";
    public static String sent = "";
    public static String token = "";
    public static AppCompatActivity tempActivity;
    public static Advertising advertising;

    public static List<GETING> GETT;
    public static Product product;

    public static String toAdvFrag = "";
    public static long vCode = -2;
    public static long current_version = 1;

    public static boolean checkReciveSms = false, btnSubmitOk = false, checkExistDb = false;
    public static JSONObject myJsonObject = null;
    public static JSONArray slides1 = null, slides2 = null, categories = null, restaurants_info = null, topsellpro = null, topseenPro = null,footerBanner = null, newPro = null, NewPro = null, specialPro = null, discountpro = null, basket = null, mostorder = null, fullbanner = null , cardbanner = null,
            smallTile = null, bigTile = null, carriercost = null, getproduct = null;
    public static boolean isEnter = false;
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String SHARED_PREF = "ah_firebase";
    public static final String TOPIC_GLOBAL = "global";
    public static ArrayList<Product> products = new ArrayList<>();


    public static AppCompatActivity act = null;


    public static Fragment frag;

    private static Exception exception;
    public static String REFRESH = "";

    public static void error(Exception ex) {

        exception = ex;

    }

}
