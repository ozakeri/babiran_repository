package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.babiran.app.R;

import java.util.List;

import Fragments.CardBanner;
import Models.Category;
import Models.Color;
import tools.AppConfig;
import ui_elements.MyTextView;

public class ListColorAdapter extends RecyclerView.Adapter<ListColorAdapter.CustomView> {

    private Context context;
    private List<Color> colors;

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
        holder.im_color.setBackgroundResource(R.drawable.circle_background_select_color);

        holder.txt_colorName.setText(color.getColorName());
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {
        private AppCompatImageView im_color;
        private MyTextView txt_colorName;
        public CustomView(@NonNull View itemView) {
            super(itemView);
            im_color = itemView.findViewById(R.id.im_color);
            txt_colorName = itemView.findViewById(R.id.txt_colorName);
        }
    }
}
