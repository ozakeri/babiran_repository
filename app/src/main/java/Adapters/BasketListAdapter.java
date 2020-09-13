package Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import net.babiran.app.R;

import java.util.ArrayList;

import Fragments.BasketListFragment;
import Fragments.ProductFragment;
import Handlers.DatabaseHandler;
import Models.Category;
import Models.Product;
import tools.AppConfig;
import tools.Util;
import ui_elements.MyTextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Tohid on 2/7/2017 AD.
 */
public class BasketListAdapter extends RecyclerView.Adapter<BasketListAdapter.CustomView> {
    RequestQueue queue;
    DatabaseHandler db;
    String id_user = "-1";
    Context context;
    ProgressDialog d;
    public int dis1 = 0;
    public int dis2 = 0;
    public int dis3 = 0;
    ArrayList<Product> products = new ArrayList<>();
    LayoutInflater inflater;
    private int currentValue;
    private BasketListFragment fragment;

    public BasketListAdapter(Context context, ArrayList<Product> products, Category category, boolean editable) {
        this.context = context;
        this.products = products;

        // this.editable = editable;
        this.inflater = LayoutInflater.from(context);
        // this.category = category;
        //this.prev = "category";

    }

    public BasketListAdapter(Context c) {
        this.context = c;
    }

    public BasketListAdapter(Context context, ArrayList<Product> products, BasketListFragment fragment) {
        this.context = context;
        this.products = products;
        this.fragment = fragment;
        this.inflater = LayoutInflater.from(context);
    }

    public BasketListAdapter(Context context, ArrayList<Product> categories, String prev) {
        this.context = context;
        this.products = categories;
        // this.prev = prev;
        this.inflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int i) {
        holder.item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfig.fragmentManager.beginTransaction().replace(R.id.Productcontainer, new ProductFragment(products.get(i))).commit();
            }
        });


        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("آیا مایلید از لیست سبد حذف شود ؟")
                        .setCancelable(false)
                        .setPositiveButton("حذف",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        deleteFrombasket(i);

                                    }
                                })
                        .setNegativeButton("انصراف",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        for (int j = 0; j < products.get(i).getImages().size(); j++) {
            if (products.get(i).getImages().get(j) != null && products.get(i).getImages().get(j).toString().length() > 5) {
                Glide.with(context).load(products.get(i).getImages().get(j).image_link).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).fitCenter().placeholder(R.drawable.logoloading).into(holder.img);
            }
        }

        holder.name.setText(products.get(i).getName());
        if (products.get(i).count != null && !products.get(i).count.equals("null")) {
            holder.count.setText(Util.latinNumberToPersian(products.get(i).count));
            holder.txt_value.setText(Util.latinNumberToPersian(products.get(i).count));
        }

        System.out.println("currentValue333===" + currentValue);
        SharedPreferences.Editor editor = context.getSharedPreferences("productsArray", MODE_PRIVATE).edit();

        holder.card_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentValue = Integer.valueOf(products.get(i).count);
                System.out.println("currentValue11===" + currentValue);
                if (currentValue < Integer.valueOf(products.get(i).stock)) {
                    currentValue++;
                    products.get(i).setCount(String.valueOf(currentValue));
                } else {
                    Toast.makeText(context, " حداکثر تعداد " + products.get(i).name + " را انتخاب کرده اید ", Toast.LENGTH_SHORT).show();
                }
                holder.txt_value.setText(Util.latinNumberToPersian(String.valueOf(currentValue)));
                holder.count.setText(Util.latinNumberToPersian(String.valueOf(currentValue)));
                Gson gson = new Gson();
                String proObj = gson.toJson(products);
                editor.putString("products", proObj);
                editor.commit();
                fragment.updateList();
            }
        });

        holder.card_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentValue = Integer.valueOf(products.get(i).count);
                System.out.println("currentValue22===" + currentValue);
                if (currentValue >= 1) {
                    if (currentValue == 1) {
                        return;
                    }
                    currentValue--;
                    holder.delete_button.setVisibility(View.VISIBLE);
                    products.get(i).setCount(String.valueOf(currentValue));

                }
                holder.txt_value.setText(Util.latinNumberToPersian(String.valueOf(currentValue)));
                holder.count.setText(Util.latinNumberToPersian(String.valueOf(currentValue)));
                Gson gson = new Gson();
                String proObj = gson.toJson(products);
                editor.putString("products", proObj);
                editor.commit();
                fragment.updateList();
            }
        });

     /*   if(prefs.getString("pro_id","0").equals(products.get(i).id)){
            holder.count.setText(prefs.getString("count","0"));
        }*/


        int Total = dis1 + dis2 + dis3;
        holder.discount.setText(Total + "%");

    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(products.get(i).id);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void deleteFrombasket(final int id) {


        SharedPreferences.Editor editor = context.getSharedPreferences("productsArray", MODE_PRIVATE).edit();
        products.remove(id);
        AppConfig.products = products;


        try {
            Gson gson = new Gson();
            String proObj = gson.toJson(AppConfig.products);
            editor.putString("products", proObj);
            editor.commit();
        } catch (Exception e) {
            e.getMessage();
        }
        AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();

    }


    public class CustomView extends RecyclerView.ViewHolder {
        MyTextView name, count, discount, txt_value;
        RelativeLayout item_Button;
        ImageView img;
        CardView card_decrease,card_increase;
        RelativeLayout delete_button;

        public CustomView(@NonNull View convertView) {
            super(convertView);
            name = convertView.findViewById(R.id.txt_name_basket);
            count = convertView.findViewById(R.id.item_count);
            discount = convertView.findViewById(R.id.item_discount);
            img = convertView.findViewById(R.id.img_basket);
            item_Button = convertView.findViewById(R.id.item_content_basket);
            delete_button = convertView.findViewById(R.id.deletebasket);
            txt_value = convertView.findViewById(R.id.txt_value);
            card_decrease = convertView.findViewById(R.id.card_decrease);
            card_increase = convertView.findViewById(R.id.card_increase);
        }
    }
}