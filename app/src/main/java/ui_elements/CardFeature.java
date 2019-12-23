package ui_elements;

import android.content.Context;

import androidx.cardview.widget.CardView;

import net.babiran.app.R;

/**
 * Created by Mohammad on 6/3/2017.
 */

public class CardFeature extends CardView {

    public  Context context ;
    public MyTextView name;
    public MyTextView value;

    public CardFeature(Context context,String name,String value) {
        super(context);

        init(context,name,value);
    }

    public void init(Context context,String name ,String value) {

        this.context=context;

        inflate(getContext(), R.layout.featurecard, this);

        this.name = (MyTextView) findViewById(R.id.feaName);
        this.value = (MyTextView) findViewById(R.id.feaValue);

        this.name.setText(name);
        this.value.setText(value);

    }
}
