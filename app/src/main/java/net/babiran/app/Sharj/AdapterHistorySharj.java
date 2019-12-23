package net.babiran.app.Sharj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;

import java.util.List;

import ui_elements.MyTextView;


/**
 * Created by D on 11/2/2017.
 */

public class AdapterHistorySharj extends RecyclerView.Adapter<AdapterHistorySharj.MyViewHolder> {
    private Context mContext;

    private List<String> categoryInfoList;

    public AdapterHistorySharj(Context mContext, List<String> categoryInfoList) {
        this.mContext = mContext;
        this.categoryInfoList = categoryInfoList;

        ;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history_sharj, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String[] Arr = categoryInfoList.get(position).split("##");

        holder.sim.setText(Arr[0]);

        if (Arr[1].equals("1")) {
            holder.operator.setText("ایرانسل");
        } else if (Arr[1].equals("2")) {
            holder.operator.setText("همراه اول");
        } else {
            holder.operator.setText("رایتل");
        }
        holder.mablagh.setText(Arr[2]);

        holder.date.setText(Arr[3]);
        holder.peygiri.setText(Arr[4]);

        // operator= 1 =>irancel ,2=>hamrahaval ,3=>rightel


    }

    @Override
    public int getItemCount() {
        return categoryInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyTextView sim, operator, mablagh, date, peygiri;


        public MyViewHolder(View view) {
            super(view);
            sim = (MyTextView) view.findViewById(R.id.aaaaaaa);
            operator = (MyTextView) view.findViewById(R.id.aaaaaaa1);
            mablagh = (MyTextView) view.findViewById(R.id.aaaaaaa2);
            date = (MyTextView) view.findViewById(R.id.aaaaaaa3);
            peygiri = (MyTextView) view.findViewById(R.id.aaaaaaa4);


        }


    }

}
