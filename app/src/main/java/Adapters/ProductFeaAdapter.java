package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.cardview.widget.CardView;

import net.babiran.app.R;

import java.util.ArrayList;

import Models.Feature;
import ui_elements.MyTextView;

public class ProductFeaAdapter extends BaseAdapter {

    Context context;
    ArrayList<Feature> featureArrayList = new ArrayList<>() ;
    LayoutInflater inflater;

    public ProductFeaAdapter(Context context, ArrayList<Feature> features){

        this.context = context;
        this.featureArrayList = features;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return featureArrayList.size();
    }

    @Override
    public Feature getItem(int i) {
        return featureArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.pro_info_item,
                    viewGroup, false);


            holder.name = (MyTextView) convertView.findViewById(R.id.fea_name);
            holder.value = (MyTextView) convertView.findViewById(R.id.fea_value);

            holder.item_Button = (CardView) convertView.findViewById(R.id.feature_list_item_layout) ;



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!featureArrayList.get(i).name.equals("") && !featureArrayList.get(i).name.equals("null") && featureArrayList.get(i).name!= null){
            holder.name.setText(featureArrayList.get(i).name + " : ");

        }
        Log.e("valuee",featureArrayList.get(i).value) ;
        if(!featureArrayList.get(i).value.equals("") && !featureArrayList.get(i).value.equals("null") && featureArrayList.get(i).value!= null) {
            holder.value.setText(featureArrayList.get(i).value);
        }

        holder.item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return convertView;
    }

    private class ViewHolder {

        MyTextView name,value;
        CardView item_Button;

    }
}
