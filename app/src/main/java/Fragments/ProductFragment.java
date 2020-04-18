package Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import net.babiran.app.ActivityComments;
import net.babiran.app.MainActivity;
import net.babiran.app.R;
import net.babiran.app.Sms_Register;
import net.babiran.app.productInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Handlers.AdvertisingDatabaseHandler;
import Handlers.DatabaseHandler;
import Models.Category;
import Models.Product;
import me.relex.circleindicator.CircleIndicator;
import tools.AppConfig;
import tools.CustomPagerAdapterByUrlMain;
import ui_elements.CardFeature;
import ui_elements.MyTextView;

import static android.content.Context.MODE_PRIVATE;

public class ProductFragment extends Fragment {


    ViewPager viewPager;
    CircleIndicator customIndicator;

    Product product;
    Category category;
    public static String prev = "";
    CardView information, comment;


    DatabaseHandler db;
    boolean IsUpdateCount = false;
    ArrayList<Product> productArrayList;

    public AdvertisingDatabaseHandler dba;
    RequestQueue queue;
    public static final String TAG = "TAG";

    MyTextView ed_visit, noStock;
    View v;
    String Count = "";


    String user_id = "-1";
    String id = "-1";
    ImageView fav;

    private boolean getProduct = false;
    NumberPicker numberpicker;

    public CustomPagerAdapterByUrlMain mCustomPagerAdapterByUrlMain;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_product_frgment, container, false);


        viewPager = (ViewPager) v.findViewById(R.id.home_viewpager);
        information = (CardView) v.findViewById(R.id.information);
        comment = (CardView) v.findViewById(R.id.comment);

        comment.setBackgroundResource(R.drawable.border_button);
        information.setBackgroundResource(R.drawable.border_button);

        noStock = (MyTextView) v.findViewById(R.id.nostock);


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


        final CardView addToBasket = (CardView) v.findViewById(R.id.addtobasket);

        addToBasket.setBackgroundResource(R.drawable.border_button);

        numberpicker = (NumberPicker) v.findViewById(R.id.numberPicker);

        LinearLayout featureCard = (LinearLayout) v.findViewById(R.id.productlinear);


        if (product != null) {

            this.id = product.id;
            DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            viewPager.getLayoutParams().height = (int) (width * 0.7);

            information.getLayoutParams().width = (int) (width * 0.40);
            comment.getLayoutParams().width = (int) (width * 0.40);

            mCustomPagerAdapterByUrlMain = new CustomPagerAdapterByUrlMain(getActivity());

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


            name.setText(product.name);
            providerName.setText(product.providerName);
            System.out.println("providerName====" + product.providerName);
            //discount.setText(product.discountName1);
            // price.setText(ConvertEnToPe(convertToFormalString(product.price)) + " تومان ");

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
        if (AppConfig.products != null) {
            for (int i = 0; i < AppConfig.products.size(); i++) {
                if (this.product.getId().equals(AppConfig.products.get(i).getId())) {
                    count = Integer.parseInt(AppConfig.products.get(i).count) + Integer.parseInt(Count);
                    AppConfig.products.get(i).count = String.valueOf(count);
                    IsUpdateCount = true;
                }
            }
        }
    }

    public int SetCount() {
        int count = 1;
        for (int i = 0; i < AppConfig.products.size(); i++) {
            if (this.product.id.equals(AppConfig.products.get(i).id)) {
                Count = AppConfig.products.get(i).count;
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
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        MainActivity.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.product.setVisibility(View.INVISIBLE);
                MainActivity.btnBack.setVisibility(View.GONE);
                MainActivity.viewLogo.setVisibility(View.VISIBLE);
            }
        });
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    MainActivity.product.setVisibility(View.INVISIBLE);
                    MainActivity.btnBack.setVisibility(View.GONE);
                    MainActivity.viewLogo.setVisibility(View.VISIBLE);

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


    public void addtoBasket() {

        final SharedPreferences.Editor editor = getActivity().getSharedPreferences("productsArray", MODE_PRIVATE).edit();

        UpdateCount();
        Log.e("IsUpdate", IsUpdateCount + "");
        if (IsUpdateCount) {

            try {
                Gson gson = new Gson();
                String proObj = gson.toJson(AppConfig.products);
                editor.putString("products", proObj);
                editor.commit();
            } catch (Exception e) {
                e.getMessage();
            }

        } else {

            try {
                product.count = Count;
                AppConfig.products.add(this.product);
                Gson gson = new Gson();
                String proObj = gson.toJson(AppConfig.products);
                editor.putString("products", proObj);
                editor.commit();
            } catch (Exception e) {
                e.getMessage();
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

        numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                Count = String.valueOf(value);
            }
        });


        txt_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoBasket();
                alert.dismiss();
            }
        });

        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

}
