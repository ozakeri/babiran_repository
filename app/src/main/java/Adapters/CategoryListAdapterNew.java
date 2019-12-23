package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class CategoryListAdapterNew extends BaseAdapter
{
    Context context;
    ArrayList<CategoryInfo> categories = new ArrayList<>();
    LayoutInflater inflater;
    String prev = "" ;

    public CategoryListAdapterNew(Context context, ArrayList<CategoryInfo> categories){
        this.context = context;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);

    }
    public CategoryListAdapterNew(Context context, ArrayList<CategoryInfo> categories, String prev){
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
    public CategoryInfo getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(categories.get(i).getId());
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.category_item_new,
                    viewGroup, false);


            holder.name = (TextView) convertView.findViewById(R.id.name_eee_eee_eee);
            holder.time = (TextView) convertView.findViewById(R.id.time_ddd_ddd);
            holder.dec = (TextView) convertView.findViewById(R.id.txt_dd_dd_dd);

            holder.item_Button = (LinearLayout) convertView.findViewById(R.id.ln_back);


            holder.name.setText(categories.get(i).getService_area());
            holder.time.setText(categories.get(i).getWork_time());
            new DownLoadLaouytTask( holder.item_Button,context).execute(categories.get(i).getImage());
            holder.dec.setText(categories.get(i).getMinimum_order());





            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       /* if(!categories.get(i).description.equals("null") && !categories.get(i).description.equals("") && categories.get(i).description != null ) {

            holder.description.setText(categories.get(i).description);
        }*/
        holder.item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer,new SecondCategoryFragment(categories.get(i).getId())).commit();

                Log.e("DDDDDD",categories.get(i).getParent_id());
                AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer,new SecondCategoryFragment(categories.get(i).getParent_id())).commit();

            }
        });

        return convertView;
    }

    private class ViewHolder {

        TextView name,time,dec;
        LinearLayout item_Button;
    }
}
