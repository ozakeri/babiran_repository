package Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.AppController;
import net.babiran.app.R;

import java.util.ArrayList;
import java.util.List;

import Models.TimeList;


public class TimeListAdapter extends RecyclerView.Adapter<TimeListAdapter.CustomViewHolder> {
    private Context context;
    private List<TimeList> timeLists = new ArrayList<>();
    private LayoutInflater inflater;
    int previousSelectedPosition = -1;

    public TimeListAdapter(Context context, List<TimeList> timeLists) {
        this.context = context;
        this.timeLists = timeLists;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_item_list, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {

        TimeList timeList = timeLists.get(i);
        holder.radio_button.setText(timeList.getText());


    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(timeLists.get(i).id);
    }

    @Override
    public int getItemCount() {
        return timeLists.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        private RadioButton radio_button;
        private RelativeLayout relativeLayout;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            radio_button = itemView.findViewById(R.id.radio_button);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.setBackgroundResource(R.color.organizationColor);
                }
            });

        }
    }



}
