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

import Models.EventbusModel;
import ui_elements.MyTextView;

public class ListColorAdapter extends RecyclerView.Adapter<ListColorAdapter.CustomView> {

    private Context context;
    private List<String> colors;
    private String selectedItemId;

    public ListColorAdapter(Context context, List<String> colors) {
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

        String color = colors.get(position);

        if (color != null) {
            //holder.im_color.setBackgroundResource(R.drawable.circle_background_select_color);
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.border_select_color);
            if (unwrappedDrawable != null) {
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, android.graphics.Color.parseColor(color));
                holder.im_color.setBackgroundResource(R.drawable.border_select_color);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItemId = color;
                    //notifyDataSetChanged();
                    EventBus.getDefault().post(new EventbusModel("", color, position));

                }
            });

            if (color.equals(selectedItemId)) {
                holder.card_layout_main.setBackgroundResource(R.drawable.circle_background_selected);
            } else {
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
        private RelativeLayout card_layout_main;

        public CustomView(@NonNull View itemView) {
            super(itemView);
            im_color = itemView.findViewById(R.id.im_color);
            card_layout_main = itemView.findViewById(R.id.card_layout_main);
        }
    }
}
