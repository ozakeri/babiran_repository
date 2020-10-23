package ui_elements;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.babiran.app.R;

import Fragments.ProductFragment;
import Models.Product;
import tools.AppConfig;
import tools.Util;


public class Card extends RelativeLayout {

    public Context context;
    public ImageView productThumbnailImage;
    public String productId;
    public TextView name, price_dis, price_free;
    private int Percentage1 = 0;
    private int Percentage2 = 0;
    private int result = 0;


    public Product product;


    public Card(Context context) {
        super(context);
        //init(context);
    }


    public Card(Context context, Product product) {
        super(context);

        this.product = product;
        init(context, product);
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init(context);
    }


    public Card(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //init(context);
    }


    public void init(final Context context, final Product product) {


        this.context = context;
        this.product = product;
        inflate(getContext(), R.layout.card, this);

        this.productThumbnailImage = (ImageView) findViewById(R.id.productThumbnailImage);
        this.name = (TextView) findViewById(R.id.card_name);
        this.price_dis = (TextView) findViewById(R.id.card_price_dis);
        this.price_free = (TextView) findViewById(R.id.card_price);
        RelativeLayout layout_percentage_discount = findViewById(R.id.layout_percentage_discount);
        MyTextView txt_percentage_discount = findViewById(R.id.txt_percentage_discount);

        //  AppConfig.BASE_URL+product.images.get(0)
        //   if(product.images.get(0)!= null)
        if (product.images.size() > 0) {
            Glide.with(context).load(product.images.get(0).image_link).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(productThumbnailImage);
        }


        this.productId = product.id;

        int dis1 = 0;
        int dis2 = 0;
        int dis3 = 0;

        int discount = dis1 + dis2 + dis3;

        String s = product.name;
        if (s.length() <= 20) {
            this.name.setText(s);
        } else {
            this.name.setText(s.substring(0,20)+"...");
        }


        if (!product.dis_price.equals("null") && !product.dis_price.equals("") && product.dis_price != null) {
            price_dis.setText(ConvertEnToPe(convertToFormalString(Integer.parseInt(product.dis_price) + "")) + " ت ");
            //   price_free.setVisibility(INVISIBLE);
        }

        if (!product.price.equals("null") && !product.price.equals("") && product.price != null) {
            price_free.setText(ConvertEnToPe(convertToFormalString(product.price + "")) + " ت ");
            price_free.setPaintFlags(price_free.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        findViewById(R.id.card_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConfig.fragmentManager.beginTransaction().replace(R.id.Productcontainer, new ProductFragment(product)).commit();

            }
        });

        Percentage1 = Integer.parseInt(product.dis_price);
        Percentage2 = Integer.parseInt(product.price);
        result = ((Percentage1 - Percentage2) * 100) / Percentage2;

        if (result == 0) {
            layout_percentage_discount.setVisibility(View.INVISIBLE);
        } else {
            txt_percentage_discount.setText(Util.latinNumberToPersian(String.valueOf(result)) + "% " + "OFF");
            layout_percentage_discount.setVisibility(View.VISIBLE);
        }


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

    public String convertToFormalString(String input) {
        String priceString = "";
        for (int i = 0; i < input.length(); i++) {
            int j = input.length() - i;
            if (j % 3 != 1) {
                priceString += input.substring(i, i + 1);
            } else {
                priceString += input.substring(i, i + 1) + ",";
            }

        }
        return priceString.substring(0, priceString.length() - 1);
    }
}