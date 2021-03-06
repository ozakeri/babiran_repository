package ui_elements;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;

import net.babiran.app.R;


public class MyButton extends androidx.appcompat.widget.AppCompatButton {

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public MyButton(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context)
    {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "iransans.ttf"));
        //this.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        //this.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        this.setTextSize(12);
        this.setShadowLayer(0f, 5, 5, getContext().getResources().getColor(R.color.white));
    }
}