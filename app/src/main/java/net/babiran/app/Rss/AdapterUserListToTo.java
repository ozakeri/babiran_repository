package net.babiran.app.Rss;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.babiran.app.R;

import java.util.List;

import tools.Util;


/**
 * Created by D on 11/2/2017.
 */

public class AdapterUserListToTo extends RecyclerView.Adapter<AdapterUserListToTo.MyViewHolder> {
    private Context mContext;
    private List<BLOGME> listProducts;


    public AdapterUserListToTo(Context mContext, List<BLOGME> listProducts) {
        this.mContext = mContext;
        this.listProducts = listProducts;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rec_rss_2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final BLOGME list = listProducts.get(position);
        Picasso.get().load(list.image_link).into(holder.imageView);

        String s = list.titr;
        if (s.length() <= 50) {
            holder.SeenP.setText(s);
        } else {
            holder.SeenP.setText(s.substring(0,50)+"...");
        }

        holder.txt_newsDate.setText(Util.convertEnToPe(list.created_at_int));

        //holder.txt_newsDate.setText(" تاریخ خبر " + Util.convertEnToPe(s.get(i).getCreatedAtInt()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListtoListActivity.ID_ME = list.id;
                Intent intent = new Intent(mContext, ShowRssActivity.class);
                mContext.startActivity(intent);
            }
        });

        holder.SeenP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListtoListActivity.ID_ME = list.id;
                Intent intent = new Intent(mContext, ShowRssActivity.class);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView SeenP, txt_newsDate;
        private ImageView imageView;


        public MyViewHolder(View view) {
            super(view);
            SeenP = view.findViewById(R.id.txt_rc_rss2);
            txt_newsDate = view.findViewById(R.id.txt_newsDate);
            imageView = view.findViewById(R.id.img_rc_rss2);

        }


    }

}
