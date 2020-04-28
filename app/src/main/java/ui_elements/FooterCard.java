package ui_elements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.babiran.app.R;

import Fragments.ProductFragment;
import Models.Category;
import Models.Product;
import tools.AppConfig;


public class FooterCard extends RelativeLayout {


    public Context contex;
    public ImageView img_footer_banner;
    public String categoryId;
    public Category category;


    public FooterCard(Context context) {
        super(context);
        //init(context);
    }


    public FooterCard(Context context, Category category) {
        super(context);

        this.category = category;
        init(context, category);
    }

    public FooterCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init(context);
    }


    public FooterCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //init(context);
    }


    public void init(final Context context, final Category category) {


        contex = context;
        this.category = category;
        inflate(getContext(), R.layout.footer_card, this);

        this.img_footer_banner = (ImageView) findViewById(R.id.img_footer_banner);
            Glide.with(contex).load(category.slide_image).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(img_footer_banner);

        this.categoryId = category.id;

        findViewById(R.id.footer_card_banner).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

               // AppConfig.fragmentManager.beginTransaction().replace(R.id.Productcontainer, new ProductFragment(product)).commit();

            }
        });
    }

    public String ConvertEnToPe(String value) {
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i))) {
                builder.append(arabicChars[(int) (value.charAt(i)) - 48]);
            } else {
                builder.append(value.charAt(i));
            }
        }
        return builder.toString();
    }

}