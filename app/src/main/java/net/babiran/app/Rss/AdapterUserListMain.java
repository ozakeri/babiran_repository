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

public class AdapterUserListMain extends RecyclerView.Adapter<AdapterUserListMain.MyViewHolder>
{
    private Context mContext;
    private List<RssList> listProducts;


    public AdapterUserListMain(Context mContext, List<RssList> listProducts)
    {
        this.mContext = mContext;
        this.listProducts = listProducts;


        ;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rec_rss_site, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {

        RssList list = listProducts.get(position);
        holder.link.setText(list.getLink());
        holder.title.setText(list.getTitle());

    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title,link;




        public MyViewHolder(View view)
        {
            super(view);
            link = (TextView) view.findViewById(R.id.txt_rc_rss_mainn_link);
            title = (TextView) view.findViewById(R.id.txt_rc_rss_mainn_des);



        }


    }

}
