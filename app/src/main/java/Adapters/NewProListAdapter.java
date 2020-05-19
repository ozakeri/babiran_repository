package Adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;

import com.bumptech.glide.Glide;

import net.babiran.app.R;

import java.util.ArrayList;

import Fragments.CountDialog;
import Fragments.ProductFragment;
import Handlers.DatabaseHandler;
import Models.Product;
import tools.AppConfig;
import tools.Util;
import ui_elements.MyTextView;

public class NewProListAdapter extends BaseAdapter {
    RequestQueue queue;
    DatabaseHandler db ;
    String id_user = "-1";
    Context context;
    ProgressDialog d ;
    ArrayList<Product> productArray = new ArrayList<>() ;
    LayoutInflater inflater;

    public NewProListAdapter(Context context, ArrayList<Product> products){

        this.context = context;
        this.productArray = products;
        this.inflater = LayoutInflater.from(context);

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
            convertView = this.inflater.inflate(R.layout.new_pro_item_new,
                    viewGroup, false);


            holder.name = (MyTextView) convertView.findViewById(R.id.new_pro_txt_name);
            holder.img = (ImageView) convertView.findViewById(R.id.new_pro_img);
            holder.item_Button = (LinearLayout) convertView.findViewById(R.id.new_pro_item_button) ;
            holder.addToBasket = (MyTextView) convertView.findViewById(R.id.addToNew) ;

            holder.price_dis = (MyTextView) convertView.findViewById(R.id.new_pro_txt_price);
            holder.price_free = (MyTextView) convertView.findViewById(R.id.new_pro_txt_free_price);
            holder.txt_percentage_discount = (MyTextView) convertView.findViewById(R.id.txt_percentage_discount);
            holder.layout_percentage_discount = (RelativeLayout) convertView.findViewById(R.id.layout_percentage_discount);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String s =productArray.get(i).name;
        if (s.length() <= 20) {
            holder.name.setText(s);
        } else {
            holder.name.setText(s.substring(0,20)+"...");
        }

        if(isEven(i))
        {
            holder.item_Button.setBackgroundResource(R.color.bac);
        }
        else

        {
            holder.item_Button.setBackgroundResource(R.color.bac2);
        }




        holder.item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfig.fragmentManager.beginTransaction().replace(R.id.Productcontainer, new ProductFragment(productArray.get(i))).commit();
            }
        });
        holder.addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = ((Activity)context).getFragmentManager();
                CountDialog countDialog = new CountDialog(productArray.get(i));
                countDialog.show(fm,"CountDialog");

            }
        });


        int Percentage1 = 0;
        int Percentage2 = 0;
        int result = 0;

        if(!productArray.get(i).dis_price.equals("null") && !productArray.get(i).dis_price.equals("") && productArray.get(i).dis_price != null){
            holder.price_dis.setText(ConvertEnToPe(convertToFormalString(Integer.parseInt(productArray.get(i).dis_price)+""))+" ت ");
            Percentage1 = Integer.parseInt(productArray.get(i).dis_price);
            //   price_free.setVisibility(INVISIBLE);
        }

        if(!productArray.get(i).price.equals("null") && !productArray.get(i).price.equals("") && productArray.get(i).price != null) {
            holder.price_free.setText(ConvertEnToPe(convertToFormalString(productArray.get(i).price + "")) + " ت ");
            holder.price_free.setPaintFlags(holder.price_free.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Percentage2 = Integer.parseInt(productArray.get(i).price);
        }

        result = ((Percentage1 - Percentage2) * 100) / Percentage2;

        if (result == 0) {
            holder.layout_percentage_discount.setVisibility(View.INVISIBLE);
        } else {
            holder.txt_percentage_discount.setText(Util.latinNumberToPersian(String.valueOf(result)) + "%" + "OFF");
            holder.layout_percentage_discount.setVisibility(View.VISIBLE);
        }

        for(int j = 0 ; j < productArray.get(i).images.size() ; j ++){
            if(productArray.get(i).images.get(j) != null && productArray.get(i).images.get(j).toString().length()>5) {
                Glide.with(context).load(productArray.get(i).images.get(j).image_link).fitCenter().placeholder(R.drawable.logoloading).into(holder.img);
            }
        }



        return convertView;
    }

    private class ViewHolder {

        MyTextView name,addToBasket;
        LinearLayout item_Button;
        ImageView img;
        MyTextView price_dis ,price_free,txt_percentage_discount;
        RelativeLayout layout_percentage_discount;

    }

    private static boolean isEven(int number)
    {
        return (number & 1) == 0;
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
