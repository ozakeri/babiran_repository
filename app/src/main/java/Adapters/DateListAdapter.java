package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;

import java.util.ArrayList;
import java.util.List;

import Models.DeliveryTime;
import Models.TimeList;


public class DateListAdapter extends RecyclerView.Adapter<DateListAdapter.CustomViewHolder> {
    private Context context;
    private List<DeliveryTime> deliveryTimes;
    private LayoutInflater inflater;


    public DateListAdapter(Context context, List<DeliveryTime> deliveryTimes) {
        this.context = context;
        this.deliveryTimes = deliveryTimes;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivery_time_layout, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {

        DeliveryTime deliveryTime = deliveryTimes.get(i);

        holder.txt_date.setText(deliveryTime.getDate());
        holder.recycler_view.setLayoutManager(new LinearLayoutManager(context));
        holder.recycler_view.setAdapter(new TimeListAdapter(context,deliveryTime.getTimeLists()));


    }

    @Override
    public int getItemCount() {
        return deliveryTimes.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_date;
        private RecyclerView recycler_view;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_date = itemView.findViewById(R.id.txt_date);
            recycler_view = itemView.findViewById(R.id.recycler_view);

        }
    }

}
