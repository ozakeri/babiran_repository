package net.babiran.app.Sefaresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.babiran.app.R;

import java.util.List;

import Models.Category;
import Models.CategoryInfo;


/**
 * Created by D on 11/2/2017.
 */

public class AdapterfoolplaceList extends RecyclerView.Adapter<AdapterfoolplaceList.MyViewHolder> {
    private Context mContext;
    private List<Category> listProducts;
    private List<CategoryInfo> categoryInfoList;

    public AdapterfoolplaceList(Context mContext, List<Category> listProducts, List<CategoryInfo> categoryInfoList) {
        this.mContext = mContext;
        this.listProducts = listProducts;
        this.categoryInfoList = categoryInfoList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rec_rss_r, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        Category lCategory = listProducts.get(position);


        holder.link.setText(lCategory.name);
        for (int i = 0; i < categoryInfoList.size(); i++) {
            CategoryInfo lCategoryInfo = categoryInfoList.get(i);
            if (lCategory.id.equals(lCategoryInfo.getParent_id())) {
                holder.SeenP.setText(lCategoryInfo.getWork_time());
                holder.area.setText(" محدوده سرویس دهی : " + lCategoryInfo.getService_area());
                holder.price.setText(" حداقل سفارش :  " + lCategoryInfo.getMinimum_order());
            }
        }


        Picasso.with(mContext).load(lCategory.icon).into(holder.imageView);
        holder.id.setText(lCategory.id);
        holder.plink.setText(lCategory.icon);

    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView SeenP, link, id, plink, area, price;
        ImageView imageView;


        public MyViewHolder(View view) {
            super(view);
            SeenP = (TextView) view.findViewById(R.id.sdfgbwtghkjd);
            link = (TextView) view.findViewById(R.id.aefhtryjh);
            imageView = (ImageView) view.findViewById(R.id.hrejkbgjg);
            plink = (TextView) view.findViewById(R.id.adfbgdfhgrtyhrutj);
            id = (TextView) view.findViewById(R.id.erbdvbds);
            area = (TextView) view.findViewById(R.id.sdgferthrthdr);
            price = (TextView) view.findViewById(R.id.dfbsdfghsrth);


        }


    }

}
