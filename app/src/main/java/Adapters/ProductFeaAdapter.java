package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;

import java.util.ArrayList;

import Models.Moshakhasat;
import ui_elements.MyTextView;

public class ProductFeaAdapter extends RecyclerView.Adapter<ProductFeaAdapter.MyViewHolder> {

    Context context;
    ArrayList<Moshakhasat> featureArrayList;
    LayoutInflater inflater;

    public ProductFeaAdapter(Context context, ArrayList<Moshakhasat> features) {
        this.context = context;
        this.featureArrayList = features;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pro_info_item, parent, false);

        return new ProductFeaAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Moshakhasat moshakhasat = featureArrayList.get(position);
        holder.name.setText(moshakhasat.getName());
        holder.value.setText(moshakhasat.getValue());

    }

    @Override
    public int getItemCount() {
        return featureArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private MyTextView name, value;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.fea_name);
            value = itemView.findViewById(R.id.fea_value);
        }
    }
}
