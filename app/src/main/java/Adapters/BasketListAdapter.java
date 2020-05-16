package Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import java.util.ArrayList;

import Fragments.BasketListFragment;
import Handlers.DatabaseHandler;
import Models.Category;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import net.babiran.app.R;

import Fragments.ProductFragment;
import Models.Product;
import tools.AppConfig;
import ui_elements.MyTextView;

import static android.content.Context.MODE_PRIVATE;
import static android.media.CamcorderProfile.get;

/**
 * Created by Tohid on 2/7/2017 AD.
 */
public class BasketListAdapter extends BaseAdapter {
    RequestQueue queue;
    DatabaseHandler db ;
    String id_user = "-1";
    Context context;
    ProgressDialog d ;
    public int dis1 = 0 ;
    public int dis2 = 0 ;
    public int dis3 = 0 ;
    ArrayList<Product> products = new ArrayList<>();
    LayoutInflater inflater;
    public BasketListAdapter(Context context, ArrayList<Product> products,Category category , boolean editable){
        this.context = context;
        this.products = products;

        // this.editable = editable;
        this.inflater = LayoutInflater.from(context);
        // this.category = category;
        //this.prev = "category";

    }

    public BasketListAdapter(Context c){
        this.context = c ;
    }
    public BasketListAdapter(Context context, ArrayList<Product> products){
        this.context = context;
        this.products = products;

        this.inflater = LayoutInflater.from(context);

    }

    public BasketListAdapter(Context context, ArrayList<Product> categories,String prev){
        this.context = context;
        this.products = categories;

        // this.prev = prev;
        this.inflater = LayoutInflater.from(context);

    }



    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(products.get(i).id);
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.basket_item,
                    viewGroup, false);

            holder.name = (MyTextView) convertView.findViewById(R.id.txt_name_basket);
            holder.count = (MyTextView) convertView.findViewById(R.id.item_count);
            holder.discount = (MyTextView) convertView.findViewById(R.id.item_discount);
            holder.img = (ImageView) convertView.findViewById(R.id.img_basket);
            holder.item_Button = (RelativeLayout) convertView.findViewById(R.id.item_content_basket) ;

            holder.delete_button = (RelativeLayout) convertView.findViewById(R.id.deletebasket);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



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


        for(int j = 0 ; j < products.get(i).getImages().size() ; j ++){
            if(products.get(i).getImages().get(j) != null && products.get(i).getImages().get(j).toString().length()>5) {
                Glide.with(context).load(products.get(i).getImages().get(j).image_link).diskCacheStrategy( DiskCacheStrategy.NONE )
                        .skipMemoryCache( true ).fitCenter().placeholder(R.drawable.logoloading).into(holder.img);
            }
        }

        holder.name.setText(products.get(i).getName());
        if(products.get(i).count != null && !products.get(i).count.equals("null") ){
            holder.count.setText(ConvertEnToPe(products.get(i).count));
        }

        final SharedPreferences prefs = context.getSharedPreferences("proCount", MODE_PRIVATE);


     /*   if(prefs.getString("pro_id","0").equals(products.get(i).id)){
            holder.count.setText(prefs.getString("count","0"));
        }*/


        int Total = dis1 + dis2 + dis3 ;
        holder.discount.setText(Total + "%") ;


        return convertView;
    }

    public String ConvertEnToPe(String value){
        char[] arabicChars = {'٠','١','٢','٣','٤','٥','٦','٧','٨','٩'};
        StringBuilder builder = new StringBuilder();
        for(int i =0;i<value.length();i++){
            if(Character.isDigit(value.charAt(i))){
                builder.append(arabicChars[(int)(value.charAt(i))-48]);
            }
            else{
                builder.append(value.charAt(i));
            }
        }
        return builder.toString();
    }
    private class ViewHolder {

        MyTextView name;
        MyTextView count;
        MyTextView discount ;
        RelativeLayout item_Button;
        ImageView img;
        RelativeLayout delete_button ;

    }


    public void deleteFrombasket(final int id){


        SharedPreferences.Editor editor= context.getSharedPreferences("productsArray", MODE_PRIVATE).edit();
        products.remove(id);
        AppConfig.products = products ;
        try {
            Gson gson = new Gson();
            String proObj = gson.toJson(AppConfig.products);
            editor.putString("products", proObj);
            editor.commit();
        }catch (Exception e){
            e.getMessage();
        }
        AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();


    }




}