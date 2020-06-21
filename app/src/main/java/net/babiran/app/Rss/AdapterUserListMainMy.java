package net.babiran.app.Rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.babiran.app.R;

import java.util.List;


/**
 * Created by D on 11/2/2017.
 */

public class AdapterUserListMainMy extends RecyclerView.Adapter<AdapterUserListMainMy.MyViewHolder> {
    private Context mContext;
    private List<String> listProducts;


    public AdapterUserListMainMy(Context mContext, List<String> listProducts) {
        this.mContext = mContext;
        this.listProducts = listProducts;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rec_rss_main, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String list[] = listProducts.get(position).split("##");
        holder.link.setText(list[1]);
        holder.title.setText(list[0]);
        Glide.with(mContext).load(list[2]).fitCenter().placeholder(R.drawable.logoloading).into(holder.img_blog);
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, link;
        private ImageView img_blog;

        public MyViewHolder(View view) {
            super(view);
            link = (TextView) view.findViewById(R.id.txt_rc_rss_mainn_link);
            title = (TextView) view.findViewById(R.id.txt_rc_rss_mainn_des);
            img_blog = view.findViewById(R.id.img_blog);
        }


    }

}
