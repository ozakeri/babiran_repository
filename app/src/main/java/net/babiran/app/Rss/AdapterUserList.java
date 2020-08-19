package net.babiran.app.Rss;

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


/**
 * Created by D on 11/2/2017.
 */

public class AdapterUserList extends RecyclerView.Adapter<AdapterUserList.MyViewHolder> {
    private Context mContext;
    private List<RssFeedModel> listProducts;


    public AdapterUserList(Context mContext, List<RssFeedModel> listProducts) {
        this.mContext = mContext;
        this.listProducts = listProducts;


        ;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rec_rss, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        RssFeedModel list = listProducts.get(position);
        Picasso.with(mContext).load(list.img_url).into(holder.imageView);
        holder.SeenP.setText(list.title);
        holder.link.setText(list.link);
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView SeenP, link;
        ImageView imageView;


        public MyViewHolder(View view) {
            super(view);
            SeenP = (TextView) view.findViewById(R.id.txt_rc_rss);
            link = (TextView) view.findViewById(R.id.txt_rc_rss_link);
            imageView = (ImageView) view.findViewById(R.id.img_rc_rss);


        }


    }

}
