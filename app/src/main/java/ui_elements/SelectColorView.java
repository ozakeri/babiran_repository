package ui_elements;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import net.babiran.app.R;

import org.greenrobot.eventbus.EventBus;

import Models.EventbusModel;


public class SelectColorView extends RelativeLayout {


    private Context context;
    private String colorName = null;
    private String colorCode = null;
    private RelativeLayout card_layout;
    private LinearLayout layout_selectColor;
    private TextView txt_color;
    private ImageView im_color;
    private String colorStrName;
    private  GradientDrawable drawable;;


    public SelectColorView(Context context) {
        super(context);
        //init(context);
    }


    public SelectColorView(Context context, String colorName, String colorCode) {
        super(context);
        this.colorName = colorName;
        this.colorCode = colorCode;
        init(context, colorName, colorCode);
    }

    public SelectColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init(context);
    }


    public SelectColorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //init(context);
    }


    public void init(final Context context, String colorName, String colorCode) {

        this.context = context;
        inflate(getContext(), R.layout.item_select_color_view, this);

        card_layout = findViewById(R.id.card_layout);
        layout_selectColor = findViewById(R.id.layout_selectColor);
        txt_color = findViewById(R.id.txt_color);
        im_color = findViewById(R.id.im_color);
        txt_color.setText(colorName);

        drawable = (GradientDrawable) layout_selectColor.getBackground();
        drawable.setStroke(3, context.getResources().getColor(R.color.gray_lighter)); // set stroke width and stroke color
        switch (colorCode) {
            case "blue": {
                colorStrName = "blue";
                txt_color.setTextColor(Color.BLUE);
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.circle_background_select_color);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, Color.BLUE);
                im_color.setBackgroundResource(R.drawable.circle_background_select_color);

                break;
            }
            case "red": {
                colorStrName = "red";
                txt_color.setTextColor(Color.RED);
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.circle_background_select_color);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, Color.RED);
                im_color.setBackgroundResource(R.drawable.circle_background_select_color);

                break;
            }
            case "black": {
                colorStrName = "black";
                txt_color.setTextColor(Color.BLACK);
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.circle_background_select_color);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, Color.BLACK);
                im_color.setBackgroundResource(R.drawable.circle_background_select_color);
                break;
            }

        }

       /* layout_selectColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (colorStrName) {
                    case "blue":
                        drawable = (GradientDrawable) layout_selectColor.getBackground();
                        drawable.setStroke(3, Color.BLUE); // set stroke width and stroke color
                        EventBus.getDefault().post(new EventbusModel("","آبی"));

                        break;
                    case "red":
                        drawable = (GradientDrawable) layout_selectColor.getBackground();
                        drawable.setStroke(3, Color.RED); // set stroke width and stroke color
                        EventBus.getDefault().post(new EventbusModel("","قرمز"));

                        break;
                    case "black":
                        drawable = (GradientDrawable) layout_selectColor.getBackground();
                        drawable.setStroke(3, Color.BLACK); // set stroke width and stroke color
                        EventBus.getDefault().post(new EventbusModel("","مشکی"));
                        break;
                }
            }
        });*/

    }

}