package Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import net.babiran.app.ActivityComments;
import net.babiran.app.MainActivity;
import net.babiran.app.R;
import net.babiran.app.Sms_Register;
import net.babiran.app.productInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.ListColorAdapter;
import Handlers.AdvertisingDatabaseHandler;
import Handlers.DatabaseHandler;
import Models.Category;
import Models.Color;
import Models.EventbusModel;
import Models.Feature;
import Models.Image;
import Models.Moshakhasat;
import Models.Product;
import me.relex.circleindicator.CircleIndicator;
import tools.AppConfig;
import tools.CustomPagerAdapterProduct;
import tools.RecyclerItemClickListener;
import tools.Util;
import ui_elements.CardFeature;
import ui_elements.MyTextView;

import static android.content.Context.MODE_PRIVATE;
import static tools.AppConfig.products;

public class ProductFragment extends Fragment implements View.OnClickListener {


    ViewPager viewPager;
    CircleIndicator customIndicator;
    private LinearLayout layout_selectColor, layout_color;
    Product product;
    Category category;
    public static String prev = "";
    CardView information, comment;
    private RecyclerView recyclerView_colorList;

    DatabaseHandler db;
    boolean IsUpdateCount = false;
    ArrayList<Product> productArrayList;
    public AdvertisingDatabaseHandler dba;
    RequestQueue queue;
    public static final String TAG = "TAG";
    MyTextView txt_colorName, noStock,btn_addToBasket;
    View v;
    String Count = "";
    String user_id = "-1";
    String id = "-1";
    ImageView fav;
    private boolean getProduct = false;
    NumberPicker numberpicker;
    public CustomPagerAdapterProduct mCustomPagerAdapterByUrlMain;
    private ListColorAdapter listColorAdapter;

    @SuppressLint("ValidFragment")
    public ProductFragment(Product product, Category category, String prev) {
        this.category = category;
        this.product = product;
        this.prev = prev;
    }

    @SuppressLint("ValidFragment")
    public ProductFragment(Product product, Category category, ArrayList<Product> productArrayList, String prev) {
        this.productArrayList = productArrayList;
        this.category = category;
        this.product = product;
        this.prev = prev;
    }

    public ProductFragment(Product product) {
        this.product = product;
    }

    @SuppressLint("ValidFragment")
    public ProductFragment(Product product, boolean getProduct) {
        this.product = product;
        this.getProduct = getProduct;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public boolean first = true;
    public Timer timer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_product_frgment, container, false);


        viewPager = (ViewPager) v.findViewById(R.id.home_viewpager);
        information = (CardView) v.findViewById(R.id.information);
        comment = (CardView) v.findViewById(R.id.comment);
        comment.setBackgroundResource(R.drawable.border_button);
        information.setBackgroundResource(R.drawable.border_button);

        noStock = v.findViewById(R.id.nostock);
        btn_addToBasket = v.findViewById(R.id.btn_addToBasket);
        txt_colorName = v.findViewById(R.id.txt_colorName);
        layout_selectColor = v.findViewById(R.id.layout_selectColor);
        recyclerView_colorList = v.findViewById(R.id.recyclerView_colorList);
        layout_color = v.findViewById(R.id.layout_color);


        AppConfig.frag = ProductFragment.this;
        MainActivity.product.setVisibility(View.VISIBLE);

        SharedPreferences prefs = getActivity().getSharedPreferences("proCount", MODE_PRIVATE);


        db = new DatabaseHandler(getActivity());


        if (db.getRowCount() > 0) {

            HashMap<String, String> userDetailsHashMap = db.getUserDetails();


            user_id = userDetailsHashMap.get("id");
        }

        MyTextView name = (MyTextView) v.findViewById(R.id.proname);
        MyTextView description = (MyTextView) v.findViewById(R.id.descriptionpro);
        //MyTextView discount = (MyTextView) v.findViewById(R.id.discountpro);
        MyTextView price = (MyTextView) v.findViewById(R.id.pricepro);
        MyTextView price_free = (MyTextView) v.findViewById(R.id.pricefree);
        MyTextView providerName = (MyTextView) v.findViewById(R.id.providerName);
        ImageView providerCategory = v.findViewById(R.id.providerCategory);
        MyTextView txt_category = v.findViewById(R.id.txt_category);

        final CardView addToBasket = (CardView) v.findViewById(R.id.addtobasket);

        addToBasket.setBackgroundResource(R.drawable.border_button);

        numberpicker = (NumberPicker) v.findViewById(R.id.numberPicker);

        LinearLayout featureCard = (LinearLayout) v.findViewById(R.id.productlinear);



       /* try {
            colorList = new ArrayList<>();
            root = new JSONObject(json);
            array = root.getJSONArray("color");
            System.out.println("array=======" + array.length());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String colorName = object.getString("color_name");
                String colorCode = object.getString("color_code");

                Color color = new Color(colorName,colorCode);
                colorList.add(color);

            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
            recyclerView_colorList.setLayoutManager(linearLayoutManager);
            listColorAdapter = new ListColorAdapter(getActivity(),colorList);
            listColorAdapter.notifyDataSetChanged();
            recyclerView_colorList.setAdapter(listColorAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //addRadioButtons(4);


        if (product != null) {

            System.out.println("product=====" + product.toString());
            this.id = product.id;
            DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            viewPager.getLayoutParams().height = (int) (width * 0.7);

            information.getLayoutParams().width = (int) (width * 0.40);
            comment.getLayoutParams().width = (int) (width * 0.40);

            mCustomPagerAdapterByUrlMain = new CustomPagerAdapterProduct(getActivity());

            customIndicator = (CircleIndicator) v.findViewById(R.id.product_indicator_custom);

            for (int i = 0; i < product.getImages().size(); i++) {

                if (product.getImages().get(i) != null && product.getImages().get(i).toString().length() > 5) {
                    mCustomPagerAdapterByUrlMain.imageLink.add(product.getImages().get(i).image_link);
                }
            }


            viewPager.setAdapter(mCustomPagerAdapterByUrlMain);


            numberpicker.setMin(1);
            if (Integer.parseInt(product.getStock()) > 0) {
                noStock.setVisibility(View.INVISIBLE);
                numberpicker.setVisibility(View.VISIBLE);
                Log.e("stock", product.getStock());
                numberpicker.setMax(Integer.parseInt(product.getStock()));
            } else {
                numberpicker.setVisibility(View.INVISIBLE);
                noStock.setVisibility(View.VISIBLE);
            }

            try {
                numberpicker.setValue(SetCount());
            } catch (Exception e) {

            }

            Count = String.valueOf(numberpicker.getValue());

            numberpicker.setValueChangedListener(new ValueChangedListener() {
                @Override
                public void valueChanged(int value, ActionEnum action) {
                    Count = String.valueOf(value);
                }
            });


            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String proObj = gson.toJson(product);
                    Intent intent = new Intent(getActivity(), ActivityComments.class);
                    intent.putExtra("product", proObj);
                    intent.putExtra("product_id", product.id);
                    startActivity(intent);
                }
            });
            information.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String proObj = gson.toJson(product);
                    Intent intent = new Intent(getActivity(), productInfo.class);
                    intent.putExtra("product", proObj);
                    startActivity(intent);
                }
            });
            addToBasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    try {
//                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                        Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
//                        r.play();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    if (user_id.equals("-1")) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setTitle("ورود کاربران");

                        alertDialog.setMessage("آیا مایلید ثبت نام کنید ؟");
                        alertDialog.setIcon(R.drawable.prof);

                        alertDialog.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(getActivity(), Sms_Register.class);
                                startActivity(intent);
                                getActivity().finish();

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
                        //Log.e("stockkk", product.stock);
                        if (!product.getStock().equals("") && !product.getStock().equals("null") && product.getStock() != null && Integer.parseInt(product.getStock()) < 1) {
                            Toast.makeText(getActivity(), "این محصول ناموجود است", Toast.LENGTH_LONG).show();
                        } else {
                            showGuideDialog();
                        }


                    }
                }
            });


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


            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 500, 4000);


            // just set viewPager
            customIndicator.setViewPager(viewPager);

            if (Integer.parseInt(product.getStock()) == 0){
                btn_addToBasket.setText("نا موجـــود");
                btn_addToBasket.setEnabled(false);
            }

            name.setText(product.name);
            System.out.println("providerName=========" + product.providerName);
            if (product.providerName != null) {
                providerName.setText(product.providerName);
            } else {
                providerName.setText("فاقد اطلاعات");
            }

            if (product.getColors() != null && product.getColors().size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView_colorList.setLayoutManager(linearLayoutManager);
                listColorAdapter = new ListColorAdapter(getActivity(), product.getColors());
                //listColorAdapter.notifyDataSetChanged();
                recyclerView_colorList.setAdapter(listColorAdapter);
                layout_color.setVisibility(View.VISIBLE);
            } else {
                layout_color.setVisibility(View.GONE);
            }

            recyclerView_colorList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Color color = product.getColors().get(position);
                    product.setColorName(color.getColorName());
                    product.setColorCode(color.getColorCode());

                    System.out.println("color=========" + color.getColorName());
                    System.out.println("color=========" + color.getColorCode());
                }
            }));

            //providerCategory.setText(product.getCategory_id());
            if (product.getCategory_id() == null) {
                providerCategory.setVisibility(View.GONE);
                txt_category.setVisibility(View.GONE);
            } else {
                providerCategory.setVisibility(View.VISIBLE);
                txt_category.setVisibility(View.VISIBLE);
            }
            providerCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCategory(product.getCategory_id());
                }
            });

            if (!product.getDis_price().equals("null") && !product.getDis_price().equals("") && product.getDis_price() != null) {

                price.setText(ConvertEnToPe(convertToFormalString(product.getDis_price())) + " تومان ");
            }

            if (!product.getPrice().equals("null") && !product.getPrice().equals("") && product.getPrice() != null) {

                if (product.getPrice().equals(product.getDis_price())) {

                    price_free.setVisibility(View.INVISIBLE);
                } else {
                    price_free.setText(ConvertEnToPe(convertToFormalString(product.getPrice())) + " تومان ");
                    price_free.setPaintFlags(price_free.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                }
            }

            description.setText(product.description);
            if (description.getText().toString().equals("") || description.getText().toString().isEmpty()) {
                v.findViewById(R.id.linearLayout_description).setBackground(null);
            }



               /* switch (product.rate) {
                    case 0:
                        rate.setImageResource(R.drawable.zerostar);
                        break;

                    case 1:
                        rate.setImageResource(R.drawable.onestar);
                        break;

                    case 2:
                        rate.setImageResource(R.drawable.twostar);
                        break;

                    case 3:
                        rate.setImageResource(R.drawable.threestar);
                        break;

                    case 4 :
                        rate.setImageResource(R.drawable.fourstar);
                        break;

                    case 5 :
                        rate.setImageResource(R.drawable.fivestar);
                        break;
                }*/


            featureCard.removeAllViews();

            for (int i = 0; i < product.getFeatures().size(); i++) {

                CardFeature cardFeature = new CardFeature(getActivity(), product.getFeatures().get(i).name, product.getFeatures().get(i).value);
                featureCard.addView(cardFeature);
            }

        }

        return v;

    }


    public void UpdateCount() {
        int count = 0;

        System.out.println("=-=-=-count-=-=-" + count);
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {
                if (this.product.getId().equals(products.get(i).getId())) {
                    count = Integer.parseInt(products.get(i).count) + Integer.parseInt(Count);
                    products.get(i).count = String.valueOf(count);
                    IsUpdateCount = true;
                }
            }
        }
    }

    public int SetCount() {
        int count = 1;
        for (int i = 0; i < products.size(); i++) {
            if (this.product.id.equals(products.get(i).id)) {
                Count = products.get(i).count;
                count = Integer.parseInt(Count);
            }
        }
        return count;
    }

    private void startSound(String filename) {
        try {
            AssetFileDescriptor afd = getActivity().getAssets().openFd(filename);
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (queue != null) {
            queue.cancelAll(TAG);
        }
        MainActivity.btnBack.setVisibility(View.VISIBLE);
        MainActivity.viewLogo.setVisibility(View.GONE);
        MainActivity.layout_search.setVisibility(View.VISIBLE);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        MainActivity.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.btnBack.setVisibility(View.GONE);
                MainActivity.viewLogo.setVisibility(View.VISIBLE);

                if ( MainActivity.search.getVisibility() == View.VISIBLE){
                    MainActivity.search.setVisibility(View.INVISIBLE);
                    MainActivity.btnBack.setVisibility(View.GONE);
                    MainActivity.viewLogo.setVisibility(View.VISIBLE);
                    MainActivity.layout_search.setVisibility(View.VISIBLE);
                    MainActivity.product.setVisibility(View.INVISIBLE);
                    MainActivity.home.setVisibility(View.VISIBLE);
                    System.out.println("VISIBLE=====222====");
                    return;
                }

                if ( MainActivity.product.getVisibility() == View.VISIBLE){
                    MainActivity.product.setVisibility(View.INVISIBLE);
                    MainActivity.btnBack.setVisibility(View.VISIBLE);
                    MainActivity.layout_search.setVisibility(View.VISIBLE);
                    MainActivity.viewLogo.setVisibility(View.GONE);
                    System.out.println("VISIBLE=====111====");
                    return;
                }


                if (MainActivity.productlist.getVisibility() == View.VISIBLE) {
                    System.out.println("===MainActivity==111===");
                    //MainActivity.productlist.setVisibility(View.INVISIBLE);
                    FragmentManager fm = getFragmentManager();
                    if (fm != null) {
                        System.out.println("===fm != null===");
                        ProductListFragment fragm = (ProductListFragment) fm.findFragmentById(R.id.ProductListcontainer);
                        if (fragm != null) {
                            System.out.println("===fragm != null===");
                            Handler handler = new Handler();
                            Thread someThread = new Thread() {

                                @Override
                                public void run() {

                                    //some actions
                                    handler.post(new Runnable() {
                                        public void run() {
                                            fragm.backpress2();
                                        }
                                    });
                                }
                            };

                            someThread.start();

                        }
                    }

                    return;
                }
                MainActivity.product.setVisibility(View.INVISIBLE);
                MainActivity.btnBack.setVisibility(View.GONE);
                MainActivity.viewLogo.setVisibility(View.VISIBLE);
                System.out.println("VISIBLE=====333====");

            }
        });
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    MainActivity.btnBack.setVisibility(View.GONE);
                    MainActivity.viewLogo.setVisibility(View.VISIBLE);

                    if ( MainActivity.search.getVisibility() == View.VISIBLE){
                        MainActivity.search.setVisibility(View.INVISIBLE);
                        MainActivity.btnBack.setVisibility(View.GONE);
                        MainActivity.viewLogo.setVisibility(View.VISIBLE);
                        MainActivity.layout_search.setVisibility(View.VISIBLE);
                        MainActivity.product.setVisibility(View.INVISIBLE);
                        MainActivity.home.setVisibility(View.VISIBLE);
                        System.out.println("VISIBLE=====222====");
                        return true;
                    }

                    if ( MainActivity.product.getVisibility() == View.VISIBLE){
                        MainActivity.product.setVisibility(View.INVISIBLE);
                        MainActivity.btnBack.setVisibility(View.VISIBLE);
                        MainActivity.layout_search.setVisibility(View.VISIBLE);
                        MainActivity.viewLogo.setVisibility(View.GONE);
                        System.out.println("VISIBLE=====111====");
                        return true;
                    }


                    if (MainActivity.productlist.getVisibility() == View.VISIBLE) {
                        System.out.println("===MainActivity==111===");
                        //MainActivity.productlist.setVisibility(View.INVISIBLE);
                        FragmentManager fm = getFragmentManager();
                        if (fm != null) {
                            System.out.println("===fm != null===");
                            ProductListFragment fragm = (ProductListFragment) fm.findFragmentById(R.id.ProductListcontainer);
                            if (fragm != null) {
                                System.out.println("===fragm != null===");
                                Handler handler = new Handler();
                                Thread someThread = new Thread() {

                                    @Override
                                    public void run() {

                                        //some actions
                                        handler.post(new Runnable() {
                                            public void run() {
                                                fragm.backpress2();
                                            }
                                        });
                                    }
                                };

                                someThread.start();

                            }
                        }

                        return true;
                    }
                    MainActivity.product.setVisibility(View.INVISIBLE);
                    MainActivity.btnBack.setVisibility(View.GONE);
                    MainActivity.viewLogo.setVisibility(View.VISIBLE);
                    System.out.println("VISIBLE=====333====");
                }
                return false;
            }
        });

    }


    public void addtoBasket() {

        final SharedPreferences.Editor editor = getActivity().getSharedPreferences("productsArray", MODE_PRIVATE).edit();

        UpdateCount();
        Log.e("IsUpdate", IsUpdateCount + "");

        if (products != null) {
            if (IsUpdateCount) {
                Gson gson = new Gson();
                String proObj = gson.toJson(products);
                editor.putString("products", proObj);
                editor.commit();

            } else {
                product.count = Count;
                products.add(this.product);
                Gson gson = new Gson();
                String proObj = gson.toJson(products);
                editor.putString("products", proObj);
                editor.commit();
                System.out.println("proObj======" + proObj);
            }
        }


        Toast.makeText(getActivity(), "به سبد خرید اضافه شد ", Toast.LENGTH_LONG).show();
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();

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

    public String convertToFormalString(String input) {
        String priceString = "";
        for (int i = 0; i < input.length(); i++) {
            int j = input.length() - i;
            if (j % 3 != 1) {
                priceString += input.substring(i, i + 1);
            } else {
                priceString += input.substring(i, i + 1) + ",";
            }

        }
        return priceString.substring(0, priceString.length() - 1);
    }


    public void showGuideDialog() {
        final Dialog alert = new Dialog(getActivity());
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.custom_dialog_select_count);
        NumberPicker numberPicker = alert.findViewById(R.id.numberPicker);
        TextView txt_action = alert.findViewById(R.id.txt_action);


        numberPicker.setMin(1);
        if (Integer.parseInt(product.getStock()) > 0) {
            noStock.setVisibility(View.INVISIBLE);
            numberPicker.setVisibility(View.VISIBLE);
            Log.e("stock", product.getStock());
            numberPicker.setMax(Integer.parseInt(product.getStock()));
        } else {
            numberPicker.setVisibility(View.INVISIBLE);
            noStock.setVisibility(View.VISIBLE);
        }

        try {
            numberPicker.setValue(SetCount());
        } catch (Exception e) {

        }

        Count = String.valueOf(numberPicker.getValue());


        txt_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.frag = ProductFragment.this;
                addtoBasket();
                alert.dismiss();
            }
        });

        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void getCategory(final String category_id) {


        //  getActivity().findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
        //Volley Start

        final ProgressDialog d = new ProgressDialog(getActivity());
        d.setMessage("چند لحظه صبرکنید ...");
        d.setIndeterminate(true);
        d.setCancelable(false);
        d.show();

        queue = Volley.newRequestQueue(getActivity());
        String url = AppConfig.BASE_URL + "api/main/search";
        System.out.println("url=====" + url);

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            d.dismiss();
                            Log.e("response=========", response);
                            ArrayList<Product> products = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                ArrayList<Feature> featuresArray = new ArrayList<>();
                                ArrayList<Image> imagesArray = new ArrayList<>();
                                ArrayList<Moshakhasat> moshakhasatArrayList = new ArrayList<>();
                                ArrayList<Color> colorArrayList = new ArrayList<>();
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


                                //c.getString("category_id1")
                                Product product = new Product(c.getString("category_id1"), c.getString("id"), c.getString("name"), c.getString("description"),
                                        c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray, moshakhasatArrayList, colorArrayList, c.getString("provider_name"));

                                products.add(product);
                            }
                            if (products.size() > 0) {
                                //   getActivity().findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);
                                MainActivity.product.setVisibility(View.INVISIBLE);
                                AppConfig.fragmentManager.beginTransaction().replace(R.id.ProductListcontainer, new ProductListFragment(products, "notcat")).commit();

                            } else {
                                //   getActivity().findViewById(R.id.progressLayout).setVisibility(View.INVISIBLE);


                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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

    @Subscribe
    public void getEvent(EventbusModel model) {
        if (model.getColor() != null) {
            txt_colorName.setText(model.getColor());
            int position = model.getPosition();
            listColorAdapter.notifyDataSetChanged();
            recyclerView_colorList.setAdapter(listColorAdapter);
            recyclerView_colorList.smoothScrollToPosition(position);
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
        if (queue != null) {
            queue.cancelAll(TAG);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, " Name " + ((RadioButton) v).getText() + " Id is " + v.getId());
        txt_colorName.setText(((RadioButton) v).getText());
    }
}
