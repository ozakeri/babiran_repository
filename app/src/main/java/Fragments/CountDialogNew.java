package Fragments;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;

import net.babiran.app.R;

import Handlers.DatabaseHandler;
import Models.Product;
import Models.ProductNew;
import tools.AppConfig;
import ui_elements.MyTextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mohammad on 6/16/2017.
 */

public class CountDialogNew extends DialogFragment {

    View v;
    RequestQueue queue;
    public String user_id = "";
    DatabaseHandler db;
    ProductNew product;
    String Count = "";
    boolean IsUpdateCount = false;


    public CountDialogNew(ProductNew p) {
        this.product = p;
    }

    NumberPicker numberpicker;
    RelativeLayout addToBasketDialog;
    MyTextView noStock;
    MyTextView tvStock;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        v = inflater.inflate(R.layout.count_dialog, container, false);
        getDialog().setTitle("CountDialog");
        numberpicker = (NumberPicker) v.findViewById(R.id.numberPickerList);
        addToBasketDialog = (RelativeLayout) v.findViewById(R.id.adding);
        noStock = (MyTextView) v.findViewById(R.id.nostock);
        tvStock = (MyTextView) v.findViewById(R.id.tv_stock);



        if (TextUtils.isEmpty(product.stock) || Integer.parseInt(product.stock) < 1) {
            tvStock.setText("");
        } else {
            tvStock.setText("موجودی در انبار: " + product.stock + "\n" + "درخواست بیش از موجودی امکان پذیر نیست.");
        }
        numberpicker.setMinValue(1);
        if (!TextUtils.isEmpty(product.stock) && Integer.parseInt(product.stock) > 0) {
            noStock.setVisibility(View.INVISIBLE);
            numberpicker.setVisibility(View.VISIBLE);
            numberpicker.setMaxValue(Integer.parseInt(product.stock));
        } else {
            numberpicker.setVisibility(View.INVISIBLE);
            noStock.setVisibility(View.VISIBLE);
        }

        numberpicker.setValue(SetCount());

        Count = String.valueOf(numberpicker.getValue());
        numberpicker.setWrapSelectorWheel(false);

        numberpicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                Count = String.valueOf(numberpicker.getValue());
            }
        });
        addToBasketDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!product.stock.equals("0")) {
                    addtoBasket(Count);
                } else {
                    Toast.makeText(getActivity(), "این محصول ناموجود است", Toast.LENGTH_LONG).show();
                    getDialog().dismiss();
                }
            }
        });
        return v;
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

    public void addtoBasket(final String Count) {
        final SharedPreferences.Editor editor = getActivity().getSharedPreferences("productsArray", MODE_PRIVATE).edit();
        UpdateCount();
        Log.e("IsUpdate", IsUpdateCount + "");
        if (IsUpdateCount) {
            Gson gson = new Gson();
            String proObj = gson.toJson(AppConfig.products);
            editor.putString("products", proObj);
            editor.commit();
        } else {
            product.count = Count;
            AppConfig.products.add(this.product);
            Gson gson = new Gson();
            String proObj = gson.toJson(AppConfig.products);
            editor.putString("products", proObj);

            editor.commit();
        }
        getDialog().dismiss();
        Toast.makeText(getActivity(), "به سبد خرید اضافه شد ", Toast.LENGTH_LONG).show();

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
          AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();


        }catch (Exception e)
        {
           AppConfig.REFRESH= "qqqqqqqqqqqqqqqqqqqqqqqq";
        }



    }

    public void UpdateCount() {
        int count = 0;
        for (int i = 0; i < AppConfig.products.size(); i++) {
            if (this.product.id.equals(AppConfig.products.get(i).id)) {
                count = Integer.parseInt(AppConfig.products.get(i).count) + Integer.parseInt(Count);
                AppConfig.products.get(i).count = String.valueOf(count);
                IsUpdateCount = true;
            }
        }
    }






//    public void addtoBasket()
//    {
//
//        final SharedPreferences.Editor editor = context.getSharedPreferences("productsArray", MODE_PRIVATE).edit();
//
//        UpdateCount();
//        Log.e("IsUpdate", IsUpdateCount + "");
//        if (IsUpdateCount)
//        {
//            Gson gson = new Gson();
//            String proObj = gson.toJson(AppConfig.products);
//            editor.putString("products", proObj);
//            editor.commit();
//        }
//        else
//        {
//            product.count = Count;
//            AppConfig.products.add(this.product);
//            Gson gson = new Gson();
//            String proObj = gson.toJson(AppConfig.products);
//            editor.putString("products", proObj);
//
//            editor.commit();
//        }
//
//
//        Toast.makeText(context, "به سبد خرید اضافه شد ", Toast.LENGTH_LONG).show();
//        try {
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(context, notification);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();
//
//    }







    @Override
    public void onResume() {
        super.onResume();

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        int w = (int) (width * 0.7);
//        getDialog().getWindow().setLayout(w, ViewGroup.LayoutParams.WRAP_CONTENT);
//        getDialog().getWindow().setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = w;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);

        super.onResume();
    }


}
