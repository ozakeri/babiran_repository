package Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.babiran.app.R;

import java.util.ArrayList;
import java.util.List;

import Fragments.CountDialog;
import Fragments.ProductFragment;
import Models.Product;
import tools.AppConfig;
import tools.Util;
import ui_elements.MyTextView;


public class SearchProductListAdapter extends RecyclerView.Adapter<SearchProductListAdapter.CustomViewHolder> {
    private Context context;
    private List<Product> categories = new ArrayList<>();
    private LayoutInflater inflater;


    public SearchProductListAdapter(Context context, List<Product> categories) {
        this.context = context;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item_new, parent, false);


        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {

        if (!categories.get(i).name.equals("null") && !categories.get(i).name.equals("") && categories.get(i).name != null) {
            holder.name.setText(categories.get(i).name);
            System.out.println("name====" + categories.get(i).name);
        }


        int Percentage1 = 0;
        int Percentage2 = 0;
        int result = 0;
        if (!categories.get(i).dis_price.equals("null") && !categories.get(i).dis_price.equals("") && categories.get(i).dis_price != null) {
            Percentage1 = Integer.parseInt(categories.get(i).dis_price);
        }
        if (!categories.get(i).price.equals("null") && !categories.get(i).price.equals("") && categories.get(i).price != null) {
            Percentage2 = Integer.parseInt(categories.get(i).price);
        }

        if (categories.get(i).getStock() != null) {
            if (categories.get(i).getStock().equals("0")) {
                holder.noProduct.setVisibility(View.VISIBLE);
                holder.addToBasket.setVisibility(View.GONE);
            } else {
                holder.noProduct.setVisibility(View.GONE);
                holder.addToBasket.setVisibility(View.VISIBLE);
            }
        }


        if (Percentage2 != 0) {
            result = ((Percentage1 - Percentage2) * 100) / Percentage2;
        }


        if (result == 0) {
            holder.layout_percentage_discount.setVisibility(View.INVISIBLE);
        } else {
            holder.txt_percentage_discount.setText(Util.latinNumberToPersian(String.valueOf(result)) + "%" + "OFF");
            holder.layout_percentage_discount.setVisibility(View.VISIBLE);
        }

        holder.item_Button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"show advertising with id : \n"+categories.get(i).id, Toast.LENGTH_SHORT).show();
                try {
              /*      AppConfig.fragmentManager.beginTransaction().add(R.id.Productcontainer, new ProductFragment(categories.get(i))).commit();
                    FragmentManager fm = ((Activity) context).getFragmentManager();
                    ProductFragment countDialog = new ProductFragment(categories.get(i));
                    fm.beginTransaction().add(R.id.Productcontainer,countDialog).hashCode();*/

                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        holder.addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = ((Activity) context).getFragmentManager();
                CountDialog countDialog = new CountDialog(categories.get(i));
                countDialog.show(fm, "CountDialog");

            }
        });


        if (!categories.get(i).dis_price.equals("null") && !categories.get(i).dis_price.equals("") && categories.get(i).dis_price != null) {
            holder.price_dis.setText(Util.ConvertEnToPe(Util.convertToFormalString(Integer.parseInt(categories.get(i).dis_price) + "")) + " ت ");
            //   price_free.setVisibility(INVISIBLE);
        }

        if (!categories.get(i).price.equals("null") && !categories.get(i).price.equals("") && categories.get(i).price != null) {
            holder.price_free.setText(Util.ConvertEnToPe(Util.convertToFormalString(categories.get(i).price + "")) + " ت ");
            holder.price_free.setPaintFlags(holder.price_free.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        for (int j = 0; j < categories.get(i).images.size(); j++) {
            if (categories.get(i).images.get(j) != null && categories.get(i).images.get(j).toString().length() > 5) {
                Glide.with(context).load(categories.get(i).images.get(j).image_link).fitCenter().placeholder(R.drawable.logoloading).into(holder.img);
            }
        }

    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(categories.get(i).id);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        private MyTextView name, addToBasket, noProduct;
        private LinearLayout item_Button;
        private MyTextView price_dis, price_free, txt_percentage_discount;
        private RelativeLayout layout_percentage_discount;
        private ImageView img;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (MyTextView) itemView.findViewById(R.id.txt_name_n);
            img = (ImageView) itemView.findViewById(R.id.img);
            item_Button = (LinearLayout) itemView.findViewById(R.id.item_button_n);
            addToBasket = (MyTextView) itemView.findViewById(R.id.addTo);
            price_dis = (MyTextView) itemView.findViewById(R.id.txt_price);
            price_free = (MyTextView) itemView.findViewById(R.id.txt_free_price);
            txt_percentage_discount = itemView.findViewById(R.id.txt_percentage_discount);
            layout_percentage_discount = itemView.findViewById(R.id.layout_percentage_discount);
            noProduct = itemView.findViewById(R.id.noProduct);
        }
    }

}
