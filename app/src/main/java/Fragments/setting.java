package Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import net.babiran.app.MainActivity;
import net.babiran.app.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mohammad on 6/13/2017.
 */


public class setting extends Fragment{

    View v;
    RelativeLayout setting ;

    Switch aSwitch ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.setting, container, false);

        MainActivity.setting.setVisibility(View.VISIBLE);

        SharedPreferences prefs = getActivity().getSharedPreferences("notification", MODE_PRIVATE);

        setting = (RelativeLayout) v.findViewById(R.id.settingConfirm);
        aSwitch = (Switch) v.findViewById(R.id.switch1) ;
        if(prefs.getBoolean("IsCheck",true)){
            aSwitch.setChecked(true);
        }
        else{
            aSwitch.setChecked(false);

        }
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setting.setVisibility(View.INVISIBLE);

                SharedPreferences pref = getActivity().getSharedPreferences("notification", 0);
                SharedPreferences.Editor editor = pref.edit();
                if(!aSwitch.isChecked()){
                    aSwitch.setChecked(false);
                    editor.putBoolean("IsOn", false);
                    editor.putBoolean("IsCheck",false);
                    editor.commit();
                    Toast.makeText(getActivity(),"تنظیمات نوتیفیکیشن غیر فعال شد ",Toast.LENGTH_LONG).show();

                }
                else {
                    aSwitch.setChecked(true);
                    editor.putBoolean("IsOn", true);
                    editor.putBoolean("IsCheck",true);
                    editor.commit();
                    Toast.makeText(getActivity(),"تنظیمات نوتیفیکیشن فعال شد ",Toast.LENGTH_LONG).show();

                }


            }
        });



        return v ;
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
                    MainActivity.setting.setVisibility(View.INVISIBLE);


                    return true;
                }
                return false;
            }
        });
    }
}

