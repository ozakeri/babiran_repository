package ui_elements;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import net.babiran.app.AppStore;
import net.babiran.app.R;


public class MyTextView extends AppCompatTextView {

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public MyTextView(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context)
    {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "IRANSansMobile_Bold.ttf"));

     //   this.setShadowLayer(0f, 5, 5, getContext().getResources().getColor(R.color.green));


//
    }

}