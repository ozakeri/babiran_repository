package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.babiran.app.R;

import java.util.ArrayList;

import Fragments.SecondCategoryFragment;
import Models.Category;
import tools.AppConfig;
import ui_elements.MyTextView;

/**
 * Created by Tohid on 2/7/2017 AD.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CustomView> {
    Context context;
    ArrayList<Category> categories = new ArrayList<>();

    LayoutInflater inflater;
    String prev = "";

    public CategoryListAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);

    }

    public CategoryListAdapter(Context context, ArrayList<Category> categories, String prev) {
        this.context = context;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
        this.prev = prev;

    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, final int position) {

        if (!categories.get(position).name.equals("null") && !categories.get(position).name.equals("") && categories.get(position).name != null) {

            holder.name.setText(categories.get(position).name);
            Picasso.get().load(categories.get(position).icon).into(holder.img_icon);

            holder.item_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConfig.fragmentManager.beginTransaction().replace(R.id.SecondCategorycontainer, new SecondCategoryFragment(categories.get(position).id)).commit();
                }
            });

        }
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(categories.get(i).id);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


     class CustomView extends RecyclerView.ViewHolder {

        MyTextView name;
        RelativeLayout item_Button;
        ImageView img_icon;

        public CustomView(@NonNull View itemView) {
            super(itemView);
            name = (MyTextView) itemView.findViewById(R.id.txt_name);
            img_icon = (ImageView) itemView.findViewById(R.id.img_icon);
            item_Button = itemView.findViewById(R.id.item_button);
        }
    }
}
