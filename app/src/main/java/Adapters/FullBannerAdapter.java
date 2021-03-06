package Adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.babiran.app.MainActivity;
import net.babiran.app.R;

import java.util.ArrayList;

import Fragments.CountDialog;
import Fragments.ProductFragment;
import Handlers.DatabaseHandler;
import Models.Product;
import tools.AppConfig;
import ui_elements.MyTextView;

public class FullBannerAdapter extends BaseAdapter {
    RequestQueue queue;
    DatabaseHandler db;
    String id_user = "-1";
    Context context;
    ProgressDialog d;
    ArrayList<Product> productArray = new ArrayList<>();
    LayoutInflater inflater;
    String prev = "";

    public FullBannerAdapter(Context context, ArrayList<Product> products, String prev) {

        this.context = context;
        this.productArray = products;
        this.inflater = LayoutInflater.from(context);
        this.prev = prev;
        Log.d("proArrayin adap", productArray.size() + "");
    }

    @Override
    public int getCount() {
        return productArray.size();
    }

    @Override
    public Product getItem(int i) {
        return productArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(productArray.get(i).id);
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.full_banner_item,
                    viewGroup, false);


            holder.name = (MyTextView) convertView.findViewById(R.id.full_banner_txt_name);
            holder.noProduct = (MyTextView) convertView.findViewById(R.id.noProduct);
            holder.img = (ImageView) convertView.findViewById(R.id.full_banner_img);
            holder.item_Button = (RelativeLayout) convertView.findViewById(R.id.full_banner_item_button);
            holder.addToBasket = (RelativeLayout) convertView.findViewById(R.id.addToFull);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(productArray.get(i).name);
        if (productArray.get(i).getStock() != null) {
            if (productArray.get(i).getStock().equals("0")) {
                holder.noProduct.setVisibility(View.VISIBLE);
                holder.addToBasket.setVisibility(View.GONE);
            } else {
                holder.noProduct.setVisibility(View.GONE);
                holder.addToBasket.setVisibility(View.VISIBLE);
            }
        }

        holder.item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (prev.equals("first")) {
                    ProductFragment.prev = "FirstFullBanner";
                    AppConfig.fragmentManager.beginTransaction().replace(R.id.Productcontainer, new ProductFragment(productArray.get(i)), ProductFragment.prev).commit();
                    MainActivity.fullbanner.setVisibility(View.INVISIBLE);
                } else if (prev.equals("second")) {
                    ProductFragment.prev = "SecondFullBanner";
                    AppConfig.fragmentManager.beginTransaction().replace(R.id.Productcontainer, new ProductFragment(productArray.get(i)), ProductFragment.prev).commit();
                    MainActivity.fullbanner.setVisibility(View.INVISIBLE);
                }


            }
        });
        holder.addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = ((Activity) context).getFragmentManager();
                CountDialog countDialog = new CountDialog(productArray.get(i));
                countDialog.show(fm, "CountDialog");

            }
        });

        for (int j = 0; j < productArray.get(i).images.size(); j++) {
            if (productArray.get(i).images.get(j) != null && productArray.get(i).images.get(j).toString().length() > 5) {
                Glide.with(context).load(productArray.get(i).images.get(j).image_link).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).fitCenter().placeholder(R.drawable.logoloading).into(holder.img);
            }
        }
        return convertView;
    }

    private class ViewHolder {

        MyTextView name, noProduct;
        RelativeLayout item_Button, addToBasket;
        ImageView img;
    }
}
