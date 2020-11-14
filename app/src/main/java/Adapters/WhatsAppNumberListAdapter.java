package Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;

import java.util.ArrayList;
import java.util.List;

import tools.Util;
import ui_elements.MyTextView;


public class WhatsAppNumberListAdapter extends RecyclerView.Adapter<WhatsAppNumberListAdapter.CustomViewHolder> {
    private Context context;
    private List<String> stringList = new ArrayList<>();
    private LayoutInflater inflater;


    public WhatsAppNumberListAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.whats_app_item_number, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int i) {
        if (stringList != null) {
            if (stringList.size() > 0) {
                holder.txt_title.setText(" اپراتور " + (Util.latinNumberToPersian(String.valueOf(i + 1))));
            }
        }


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=" + stringList.get(i);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(stringList.get(i));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        private MyTextView txt_title;
        private RelativeLayout relativeLayout;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

}
