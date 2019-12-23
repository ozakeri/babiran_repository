package net.babiran.app.Rss;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.shawnlin.numberpicker.NumberPicker;

import net.babiran.app.AppStore;
import net.babiran.app.BlankAcct;
import net.babiran.app.R;

import java.util.Locale;

import ui_elements.MyButton;

public class SettingActivity extends AppCompatActivity {
    MyButton btnColor, btnSize, Refresh, Defalt;
    TextView text;
    AppStore appStore;
    int font = 101, color = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        appStore = new AppStore(this);

        INIT();

    }

    //         #616161   14
    private void INIT() {
        btnColor = (MyButton) findViewById(R.id.fgnbfghgh);
        btnSize = (MyButton) findViewById(R.id.dscddfdv);
        Refresh = (MyButton) findViewById(R.id.fgbnfgfgfb);
        Defalt = (MyButton) findViewById(R.id.fgbnfvgfgfb);
        text = (TextView) findViewById(R.id.dgfrgrtyh);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(SettingActivity.this)
                        .setTitle("انتخاب رنگ")
                        .initialColor(R.color.white)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                Log.e("LOG", "" + selectedColor);

                                //    appStore.SAVESHAREPREFRENCEINT(selectedColor,AppStore.COLOR,SettingActivity.this);
                                text.setTextColor(selectedColor);
                            }
                        })
                        .setPositiveButton("تایید", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                Log.e("LOG", "" + selectedColor);

                                color = selectedColor;
                                String hex = Integer.toHexString(selectedColor);
                                text.setTextColor(selectedColor);
                                Log.e("LOGSX", "" + hex);
                                Log.e("xxxxxxxx", "" + R.color.white);
                            }
                        })
                        .setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });
        btnSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DilagAdd();
            }
        });


        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fg = color;
                int fdg = font;
                if (fdg != 101) {
                    appStore.SAVESHAREPREFRENCEINT(font, AppStore.FONTS, SettingActivity.this);


                }
                if (fg != 101) {
                    appStore.SAVESHAREPREFRENCEINT(color, AppStore.COLOR, SettingActivity.this);
                }


                finish();
                startActivity(new Intent(SettingActivity.this, BlankAcct.class));
            }
        });

        Defalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fg = R.color.gray_dark;
                int fdg = 12;
                if (fdg != 101) {
                    appStore.SAVESHAREPREFRENCEINT(fdg, AppStore.FONTS, SettingActivity.this);


                }
                if (fg != 101) {
                    appStore.SAVESHAREPREFRENCEINT(fg, AppStore.COLOR, SettingActivity.this);
                }


                finish();
                startActivity(new Intent(SettingActivity.this, BlankAcct.class));
            }
        });


    }


    private void DilagAdd() {
        final Dialog dialog = new Dialog(SettingActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        //TODO Dilaog

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.count_dialog_ssss);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();


        RelativeLayout btn = (RelativeLayout) dialog.findViewById(R.id.adding);


/////////////////////////////

        final int[] pos = {0};
        NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.number_picker);
        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(22);

        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.e("LOG", String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                text.setTextSize(newVal);
                font = newVal;
//                Log.e("LOG",nums[newVal]);
                pos[0] = (newVal - 1);

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
