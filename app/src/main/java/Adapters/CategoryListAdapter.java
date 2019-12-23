package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.squareup.picasso.Picasso;

import net.babiran.app.R;

import java.util.ArrayList;

import Fragments.SecondCategoryFragment;
import Models.Category;
import Models.CategoryInfo;
import tools.AppConfig;
import ui_elements.MyTextView;

/**
 * Created by Tohid on 2/7/2017 AD.
 */

public class CategoryListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Category> categories = new ArrayList<>();

    LayoutInflater inflater;
    String prev = "" ;

    public CategoryListAdapter(Context context,ArrayList<Category> categories){
        this.context = context;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);

    }
    public CategoryListAdapter(Context context,ArrayList<Category> categories,String prev){
        this.context = context;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
        this.prev = prev ;

    }
    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int i) {
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
            convertView = this.inflater.inflate(R.layout.category_item,
                    viewGroup, false);


            holder.name = (MyTextView) convertView.findViewById(R.id.txt_name);
            holder.img_icon = (ImageView)convertView.findViewById(R.id.img_icon);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if(!categories.get(i).name.equals("null") && !categories.get(i).name.equals("") && categories.get(i).name != null ){

            holder.name.setText(categories.get(i).name);
            Picasso.with(context).load(categories.get(i).icon).into(holder.img_icon);
            System.out.println("icon=====" + categories.get(i).icon);

        }
       /* if(!categories.get(i).description.equals("null") && !categories.get(i).description.equals("") && categories.get(i).description != null ) {

            holder.description.setText(categories.get(i).description);
        }*/
        convertView.findViewById(R.id.item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer,new SecondCategoryFragment(categories.get(i).id)).commit();


            }
        });

        return convertView;
    }

    private class ViewHolder {

        MyTextView name;
        RelativeLayout item_Button;
        ImageView img_icon;
    }
}
