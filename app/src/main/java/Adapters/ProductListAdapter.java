package Adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import Fragments.CountDialog;

import net.babiran.app.R;

import Fragments.ProductFragment;
import Models.Product;
import tools.AppConfig;
import ui_elements.MyTextView;


/**
 * Created by Tohid on 2/7/2017 AD.
 */

public class ProductListAdapter extends BaseAdapter {
    Context context;
    List<Product> categories = new ArrayList<>();
    LayoutInflater inflater;


    public ProductListAdapter(Context context, List<Product> categories){
        this.context = context;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Product getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(categories.get(i).id);
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.product_item_new,
                    viewGroup, false);

            holder.name = (MyTextView) convertView.findViewById(R.id.txt_name_n);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.item_Button = (LinearLayout) convertView.findViewById(R.id.item_button_n) ;
            holder.addToBasket = (MyTextView) convertView.findViewById(R.id.addTo) ;


            holder.price_dis = (MyTextView) convertView.findViewById(R.id.txt_price);
            holder.price_free = (MyTextView) convertView.findViewById(R.id.txt_free_price);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!categories.get(i).name.equals("null") && !categories.get(i).name.equals("") && categories.get(i).name != null ){
            holder.name.setText(categories.get(i).name);

        }

        holder.item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"show advertising with id : \n"+categories.get(i).id, Toast.LENGTH_SHORT).show();
               AppConfig.fragmentManager.beginTransaction().replace(R.id.Productcontainer, new ProductFragment(categories.get(i))).commit();
                System.out.println("iiiii====" + i);


            }
        });

        holder.addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = ((Activity)context).getFragmentManager();
                CountDialog countDialog = new CountDialog(categories.get(i));
                countDialog.show(fm,"CountDialog");

            }
        });

        if(isEven(i))
        {
            holder.item_Button.setBackgroundResource(R.color.bac);
        }
        else

        {
            holder.item_Button.setBackgroundResource(R.color.bac2);
        }



        if(!categories.get(i).dis_price.equals("null") && !categories.get(i).dis_price.equals("") && categories.get(i).dis_price != null){
            holder.price_dis.setText(ConvertEnToPe(convertToFormalString(Integer.parseInt(categories.get(i).dis_price)+""))+" ت ");
            //   price_free.setVisibility(INVISIBLE);
        }

        if(!categories.get(i).price.equals("null") && !categories.get(i).price.equals("") && categories.get(i).price != null) {
            holder.price_free.setText(ConvertEnToPe(convertToFormalString(categories.get(i).price + "")) + " ت ");
            holder.price_free.setPaintFlags(holder.price_free.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        for(int j = 0 ; j < categories.get(i).images.size() ; j ++){
            if(categories.get(i).images.get(j) != null && categories.get(i).images.get(j).toString().length()>5) {
                Glide.with(context).load(categories.get(i).images.get(j).image_link).fitCenter().placeholder(R.drawable.logoloading).into(holder.img);
            }
        }

        return convertView;
    }
    private static boolean isEven(int number)
    {
        return (number & 1) == 0;
    }

    private class ViewHolder {

        MyTextView name, addToBasket;
        LinearLayout item_Button ;
        MyTextView price_dis ,price_free;

        ImageView img;

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
    public String convertToFormalString(String input){
        String priceString = "";
        for(int i = 0 ; i < input.length() ; i++){
            int j = input.length() - i ;
            if(j%3 !=1){
                priceString += input.substring(i,i+1);
            }
            else{
                priceString += input.substring(i,i+1)+ ",";
            }

        }
        return priceString.substring(0,priceString.length()-1) ;
    }


}
