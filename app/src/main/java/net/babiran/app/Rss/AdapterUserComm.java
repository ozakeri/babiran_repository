package net.babiran.app.Rss;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import net.babiran.app.R;
import net.babiran.app.Servic.GetComm;
import net.babiran.app.Servic.GetSucc;
import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import tools.AppConfig;
import ui_elements.MyTextView;


/**
 * Created by D on 11/2/2017.
 */

public class AdapterUserComm extends RecyclerView.Adapter<AdapterUserComm.MyViewHolder>
{
    private Context mContext;
    private List<String> listProducts;
int id_blog;

    public AdapterUserComm(Context mContext, List<String> listProducts,int id_blog)
    {
        this.mContext = mContext;
        this.listProducts = listProducts;
        this.id_blog = id_blog;

        ;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rec_rss_cc, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {

        final String i [] =listProducts.get(position).split("##");

          holder.SeenP.setText(i[0]);
        holder.tex.setText(i[1]);
        holder.rate.setText("پسندیده شده :"+i[2]);
        holder.id.setText(i[3]);


        final String[] st = {""};
        final boolean like=false;
        holder.  imgDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!like)
                {
                    st[0] ="down";
                    holder.  imgDisLike.setImageResource(R.drawable.dilike);
                    holder.  imgLike.setImageResource(R.drawable.like);
                    VoteCommet( st[0], holder.imgDisLike,i[3]);
                }
            }
        });

        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!like)
                {
                    st[0] ="up";
                    holder. imgLike.setImageResource(R.drawable.like);
                    holder. imgDisLike.setImageResource(R.drawable.dilike);
                    VoteCommet( st[0], holder.imgLike,i[3]);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public MyTextView SeenP,tex,rate;
        TextView id;
        ImageView imgLike,imgDisLike;

        public MyViewHolder(View view)
        {
            super(view);
            SeenP = (MyTextView) view.findViewById(R.id.txt_rc_rsfgs);
            tex = (MyTextView) view.findViewById(R.id.txt_rc_rsddfgs);
            rate = (MyTextView) view.findViewById(R.id.gghghgh);
            id = (TextView) view.findViewById(R.id.kjdefngkjf);
             imgLike = (ImageView)view.findViewById(R.id.jhdbfhrf);

           imgDisLike = (ImageView)view.findViewById(R.id.grg);

        }


    }

    private void VoteCommet( String d,View mv,String id)
    {

        Log.e("BLOG_comment_id",""+id_blog);
        Log.e("BLOG_user_id",""+AppConfig.id);
        Log.e("BLOG_vote",""+d);


        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(5)
                .playOn(mv);
        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<GetSucc> call = n.setVote(Integer.parseInt(id),Integer.parseInt(AppConfig.id),d);

            call.enqueue(new Callback<GetSucc>()
            {
                @Override
                public void onResponse(@NonNull Call<GetSucc> call, @NonNull retrofit2.Response<GetSucc> response)
                {

                    if(response.code() ==200)
                    {

                        Toast.makeText(mContext,"با موفقیت ثبت شد",Toast.LENGTH_SHORT).show();
                       // ListedGetCommet();
                    }

                }
                @Override
                public void onFailure(Call<GetSucc> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage()+"\n"+t.toString());

                }
            });
        }catch (Exception ex)
        {
            Log.e("response 3 :", ex.getMessage());
        }


        //    prograsDialog.dismiss();
    }





}
