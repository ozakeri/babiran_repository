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
import tools.AppConfig;

public class SmallTileAdapter extends RecyclerView.Adapter<SmallTileAdapter.CustomView> {

    private Context context;
    private List<Category> categories;

    public SmallTileAdapter(Context context, List<Category> categories) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_card, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
            Category category = categories.get(position);
        Glide.with(context).load(category.slide_image).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(holder.img_footer_banner);

        holder.img_footer_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.fragmentManager.beginTransaction().replace(R.id.CardbannerContainer, new CardBanner(category.id, "first")).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {
        private AppCompatImageView img_footer_banner;
        public CustomView(@NonNull View itemView) {
            super(itemView);
            img_footer_banner = itemView.findViewById(R.id.img_footer_banner);
        }
    }
}
