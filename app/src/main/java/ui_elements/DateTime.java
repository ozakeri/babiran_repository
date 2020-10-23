package ui_elements;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import net.babiran.app.R;

import Models.TimeList;


public class DateTime extends RelativeLayout {

    private RadioButton radio_button;
    private TimeList timeList;


    public DateTime(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //init(context);
    }

    public DateTime(Context context, TimeList timeList) {
        super(context);
        this.timeList = timeList;
        init(context, timeList);
    }


    public void init(Context context, TimeList timeList) {
        inflate(getContext(), R.layout.delivery_time_layout, this);
        radio_button = findViewById(R.id.radio_button);
        this.timeList = timeList;
        radio_button.setText(timeList.getText());
    }
}