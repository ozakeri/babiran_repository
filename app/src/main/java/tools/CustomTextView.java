package tools;

/**
 * Created by Mohamad Cheraghi on 08/22/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    private TextView textView;

    public CustomTextView(Context context) {
        super(context);
        applyCustomFont(context);
        singleLine(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
        singleLine(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
        singleLine(context);
        init();
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("IRANSans(FaNum)_Light.ttf", context);
        setTypeface(customFont);

    }

    private void singleLine(Context context) {
        textView = new TextView(context);
        textView.setSingleLine(false);
    }

    private void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "IRANSans(FaNum)_Light.ttf"));
    }
}