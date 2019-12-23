package Fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import net.babiran.app.MainActivity;
import net.babiran.app.R;

import ui_elements.MyTextView;

/**
 * Created by Mohammad on 6/13/2017.
 */


public class AboutFragment extends Fragment {

    View v;
    MyTextView aboutText ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.about, container, false);

        MainActivity.about.setVisibility(View.VISIBLE);


        aboutText = (MyTextView) v.findViewById(R.id.about) ;
        aboutText.setText("نیاز به حمایت های مرکز در راستای بازاریابی و تجاری سازی محصول و برخورداری از اعتبارهای معنوی و پشتیبانی مرکز در عقد قرارداد با سازمان ها و مراکز مختلف وهمچنین داشتن دفتر کار و امکانات اولیه ی توسعه ی این سرویس مهم ترین دلیل درخواست ما به پذیرش سازمان است. از طرفی در صورت پذیرش سازمان می توانیم سایر طرح های خود را نیز که استعداد ورود به بازار و تجاری شدن را دارند را نیز پیاده سازی کرده و تجاری کنیم\n" +
                "بازده انسانی و غیر انسانی را بالا می برد و هزینه ها را به صورت چشمگیری می کاهد و استفاده از منابع موجود را به حداکثر می رساند و در صورتی که سازمان ها و موسسات یک جامعه بتوانند به این نقطه برسند که بازدهی بسیار بالایی داشته باشند، افزایش بهره وری و ساعت کاری مفید در ارگان آنها به وجود خواهد آمد که در افزایش سود سازمان ها نیز تاثیر چشمگیری خواهد داشت و گامی بزرگ در توسعه و صنعتی شدن جامعه برداشته می شود.\n"
                );
        return v ;
    }

    public void AboutFragment(){

    }
    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    MainActivity.about.setVisibility(View.INVISIBLE);


                    return true;
                }
                return false;
            }
        });
    }
}

