package Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import net.babiran.app.R;
import net.babiran.app.Sefaresh.ListFoodActivity;
import net.babiran.app.Sefaresh.ShowActivity;

import Fragments.SecondCategoryFragment;
import Models.Category;
import tools.AppConfig;
import ui_elements.MyTextView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.DataObjectHolder> {


    private Context context;
    private static MyClickListener myClickListener;
    public ArrayList<Category> mDataset;
    String imgURL;

    public CategoryAdapter(ArrayList<Category> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

        Log.e("image_link",mDataset.size()+"");


        if(mDataset.get(position).icon!= null && !mDataset.get(position).icon.equals("null")
                && !mDataset.get(position).icon.equals("")) {

            //Glide.with(context).load(mDataset.get(position).icon).diskCacheStrategy(DiskCacheStrategy.NONE)
                   // .skipMemoryCache(true).fitCenter().placeholder(R.drawable.logoloading).into(holder.img);

            Glide.with(context).load(mDataset.get(position).icon).into(holder.img);

            //Picasso.with(context).load(mDataset.get(position).icon).into(holder.img);
        }

        if(mDataset.get(position).name!= null && !mDataset.get(position).name.equals("null")
                && !mDataset.get(position).name.equals("")) {
            holder.txt.setText(mDataset.get(position).name);
        }

        holder.catLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(  mDataset.get(position).id .equals("1196") )
                {
                    Intent intent = new Intent(context, ListFoodActivity.class);
                    intent.putExtra("IDD","1196");
                    context.startActivity(intent);

                  //  AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer,new SecondCategoryFragmentNew(mDataset.get(position).id)).commit();

                }
                else if( mDataset.get(position).id .equals("1197"))
                {
                   // AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer,new SecondCategoryFragmentNew(mDataset.get(position).id)).commit();
                    Intent intent = new Intent(context, ListFoodActivity.class);
                    intent.putExtra("IDD","1197");
                    context.startActivity(intent);
                }

                AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer,new SecondCategoryFragment(mDataset.get(position).id)).commit();

               /* Intent intent = new Intent(context,SecondCategory.class);
                intent.putExtra("parentID",mDataset.get(position).id);
                context.startActivity(intent);*/
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        ImageView img;
        MyTextView txt ;
        RelativeLayout catLayout ;

        public DataObjectHolder(View itemView) {
            super(itemView);


            img = (ImageView) itemView.findViewById(R.id.category_icon);
            txt = (MyTextView) itemView.findViewById(R.id.category_name);
            catLayout = (RelativeLayout) itemView.findViewById(R.id.cat_card_layout_main);

            DisplayMetrics metrics = itemView.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;

            catLayout.getLayoutParams().width = (int)(width *0.45) ;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

}