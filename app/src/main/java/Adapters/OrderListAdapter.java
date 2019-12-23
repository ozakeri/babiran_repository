package Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import net.babiran.app.R;

import java.util.ArrayList;

import Handlers.DatabaseHandler;
import Models.Product;
import ui_elements.MyTextView;

public class OrderListAdapter extends BaseAdapter {
    RequestQueue queue;
    DatabaseHandler db ;
    String id_user = "-1";
    Context context;
    ProgressDialog d ;
    ArrayList<Product> productArray = new ArrayList<>() ;
    LayoutInflater inflater;

    public OrderListAdapter(Context context, ArrayList<Product> products){

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
            convertView = this.inflater.inflate(R.layout.order_item,
                    viewGroup, false);


            holder.name = (MyTextView) convertView.findViewById(R.id.order_txt_name);
            holder.description = (MyTextView) convertView.findViewById(R.id.order_txt_description);
            holder.price_dis = (MyTextView) convertView.findViewById(R.id.order_txt_price);
            holder.price_free = (MyTextView) convertView.findViewById(R.id.order_txt_free_price);
            holder.count = (MyTextView) convertView.findViewById(R.id.countValue);

            holder.img = (ImageView) convertView.findViewById(R.id.order_img);
            holder.item_Button = (RelativeLayout) convertView.findViewById(R.id.order_item_button) ;


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!productArray.get(i).name.equals("null") && !productArray.get(i).name.equals("") && productArray.get(i).name != null ){
            holder.name.setText(productArray.get(i).name);

        }

        if(!productArray.get(i).count.equals("null") && !productArray.get(i).count.equals("") && productArray.get(i).count != null ){
            holder.count.setText(ConvertEnToPe(productArray.get(i).count) + " عدد ");

        }

        if(!productArray.get(i).description.equals("null") && !productArray.get(i).description.equals("") && productArray.get(i).description != null ){

            if (productArray.get(i).description.length() > 38) {
                holder.description.setText(productArray.get(i).description.substring(0, 35));
            }else{
                holder.description.setText(productArray.get(i).description);

            }
        }

        int dis1 = 0 ;
        int dis2 = 0 ;
        int dis3 = 0 ;


        int  discountPrice = dis1 + dis2 + dis3;

        if(!productArray.get(i).dis_price.equals("null") && !productArray.get(i).dis_price.equals("") && productArray.get(i).dis_price != null){
            holder.price_dis.setText(ConvertEnToPe(convertToFormalString(Integer.parseInt(productArray.get(i).dis_price)+""))+" ت ");
            //   price_free.setVisibility(INVISIBLE);
        }

        if(!productArray.get(i).price.equals("null") && !productArray.get(i).price.equals("") && productArray.get(i).price != null) {
            holder.price_free.setText(ConvertEnToPe(convertToFormalString(productArray.get(i).price + "")) + " ت ");
            holder.price_free.setPaintFlags(holder.price_free.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }




        for(int j = 0 ; j < productArray.get(i).images.size() ; j ++){
            if(productArray.get(i).images.get(j) != null && productArray.get(i).images.get(j).toString().length()>5) {
                Glide.with(context).load(productArray.get(i).images.get(j).image_link).diskCacheStrategy( DiskCacheStrategy.NONE )
                        .skipMemoryCache( true ).fitCenter().placeholder(R.drawable.logoloading).into(holder.img);
            }
        }



        return convertView;
    }

    private class ViewHolder {

        MyTextView name;
        MyTextView description;
        MyTextView price_dis ,price_free , count;
        RelativeLayout item_Button;
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
