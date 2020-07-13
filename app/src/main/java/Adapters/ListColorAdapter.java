package Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import Models.Color;
import Models.EventbusModel;
import ui_elements.MyTextView;

public class ListColorAdapter extends RecyclerView.Adapter<ListColorAdapter.CustomView> {

    private Context context;
    private List<Color> colors;
    private String selectedItemId;

    public ListColorAdapter(Context context, List<Color> colors) {
        this.colors = colors;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.colors_item, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {

        Color color = colors.get(position);

        if (color != null){
            //holder.im_color.setBackgroundResource(R.drawable.circle_background_select_color);
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.circle_background_select_color);
            if (unwrappedDrawable != null){
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, android.graphics.Color.parseColor("#" + color.colorCode));
                holder.im_color.setBackgroundResource(R.drawable.circle_background_select_color);
            }

            holder.txt_colorName.setText(color.getColorName());
            System.out.println("getColorName===" + color.getColorName());
            System.out.println("getColorName===" + color.getId());
            System.out.println("getColorName===" + color.getColorCode());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItemId = color.getId();
                    //notifyDataSetChanged();
                    EventBus.getDefault().post(new EventbusModel("",color.getColorName(),position));

                }
            });

            if(color.getId().equals(selectedItemId)){
                holder.card_layout_main.setBackgroundResource(R.drawable.circle_background_selected);
            }else {
                holder.card_layout_main.setBackgroundResource(R.drawable.border_select_color);
            }
        }

    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {
        private AppCompatImageView im_color;
        private MyTextView txt_colorName;
        private RelativeLayout card_layout_main;

        public CustomView(@NonNull View itemView) {
            super(itemView);
            im_color = itemView.findViewById(R.id.im_color);
            txt_colorName = itemView.findViewById(R.id.txt_colorName);
            card_layout_main = itemView.findViewById(R.id.card_layout_main);
        }
    }
}
