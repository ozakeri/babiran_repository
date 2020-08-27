package net.babiran.app.Rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import net.babiran.app.R;

import java.util.List;


/**
 * Created by D on 11/2/2017.
 */

public class AdapterUserListMy extends RecyclerView.Adapter<AdapterUserListMy.MyViewHolder>
{
    private Context mContext;
    private List<String> listProducts;


    public AdapterUserListMy(Context mContext, List<String> listProducts)
    {
        this.mContext = mContext;
        this.listProducts = listProducts;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rec_rss, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {

        String list[] = listProducts.get(position).split("##");
        //holder.imageView.setVisibility(View.GONE);
        System.out.println("listProducts=====" + listProducts.get(position));

        holder.SeenP.setText(list[0]);
        holder.link.setText(list[1]);
        holder.tf.setText(list[2]);
        Glide.with(mContext).load(list[3]).fitCenter().placeholder(R.drawable.logoloading).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView SeenP,link,tf;
        ImageView imageView;



        public MyViewHolder(View view)
        {
            super(view);
            SeenP = (TextView) view.findViewById(R.id.txt_rc_rss);
            link = (TextView) view.findViewById(R.id.txt_rc_rss_link);
            tf = (TextView) view.findViewById(R.id.tfghjkhjkhjkk);
            imageView = (ImageView) view.findViewById(R.id.img_rc_rss);


        }


    }

}
