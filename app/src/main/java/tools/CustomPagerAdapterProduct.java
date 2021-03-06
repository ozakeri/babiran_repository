package tools;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;

import net.babiran.app.MainActivity;
import net.babiran.app.R;
import net.babiran.app.Rss.FullScreenActivity;

import java.util.ArrayList;


/**
 * Created by Tohid on 7/15/15.
 */
public class CustomPagerAdapterProduct extends PagerAdapter {


    public ArrayList<String> ids = new ArrayList<>();
    public ArrayList<String> subject = new ArrayList<>();
    public ArrayList<String> description = new ArrayList<>();
    public ArrayList<String> links = new ArrayList<>();
    public ArrayList<String> imageLink = new ArrayList<>();
    public ArrayList<String> whichSlide = new ArrayList<>();
    public ArrayList<String> isActive = new ArrayList<>();
    public ArrayList<String> createdAtJalali = new ArrayList<>();
    public ArrayList<String> updatedAtJalali = new ArrayList<>();
    public ArrayList<String> createdAt = new ArrayList<>();
    public ArrayList<String> updateAt = new ArrayList<>();


    RequestQueue queue;
    public static final String TAG = "TAG";

    Context mContext;
    LayoutInflater mLayoutInflater;

    MainActivity activity;


    public CustomPagerAdapterProduct(Context context) {
        mContext = context;
//        activity = (MainActivity)mContext;

        this.mContext = context;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return imageLink.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        Glide.with(mContext).load(imageLink.get(position)).into(imageView);
        container.addView(itemView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, FullScreenActivity.class);
                intent.putExtra("imgUrl", imageLink.get(position));
                mContext.startActivity(intent);

            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


}
