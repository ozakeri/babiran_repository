package Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.RequestQueue;
import net.babiran.app.MainActivity;
import net.babiran.app.R;


import android.os.Handler;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import tools.AppConfig;
import ui_elements.MyTextView;

import static android.content.Context.MODE_PRIVATE;


public class FactorFragment extends Fragment {

    View v;
    RequestQueue queue;

    String factor_id = "";
    Long startTime;
    SeekBar seekBar;
    MyTextView minute;
    MyTextView second;
    Button start, stop;
    MyCountDownTimer myCountDownTimer;
    RelativeLayout factorTimer ;
    ProgressDialog d ;

    int i = 0 ;
    RelativeLayout finish;
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.factor, container, false);


        final SharedPreferences prefs = getActivity().getSharedPreferences("factor", MODE_PRIVATE);

        factor_id = prefs.getString("factor_id", "");
        startTime = prefs.getLong("currentTime", 0);

        if (factor_id != null) {
            MainActivity.home.setVisibility(View.INVISIBLE);
            MainActivity.deliver.setVisibility(View.VISIBLE);
        }
        minute = (MyTextView) v.findViewById(R.id.minute);
        second = (MyTextView) v.findViewById(R.id.second);
        seekBar = (SeekBar) v.findViewById(R.id.seekBar1);
        factorTimer = (RelativeLayout) v.findViewById(R.id.factorTimer);

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        finish = (RelativeLayout) v.findViewById(R.id.finish);

        myCountDownTimer = new MyCountDownTimer(1000 * 60 * 15, 1000);
        myCountDownTimer.start();


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("آیا سفارش به دستتان رسید ؟")
                        .setCancelable(false)
                        .setPositiveButton("بله",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                        SharedPreferences.Editor editor= getActivity().getSharedPreferences("factor", MODE_PRIVATE).edit();
                                        editor.putString("factor_id",null);
                                        editor.putString("factorID",factor_id);
                                        editor.commit();
                                        myCountDownTimer.cancel();
                                        MainActivity.deliver.setVisibility(View.INVISIBLE);
                                        MainActivity.factorcontainer.setVisibility(View.VISIBLE);
                                        AppConfig.fragmentManager.beginTransaction().replace(R.id.Nazarcontainer, new NazarFragment()).commit();
                                    }
                                })
                        .setNegativeButton("فعلا نه",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();



            }
        });

        return v;
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            seekBar.setMax((int) millisInFuture);

        }

        @Override
        public void onTick(long millisUntilFinished) {

            final int progress = (int) (millisUntilFinished/1000);

            final long diff = System.currentTimeMillis() - startTime ;
            int minutes = (int) ((diff / (1000*60)) % 60);
            int seconds = (int) (diff / 1000) % 60 ;

            minute.setText(String.valueOf(14-minutes));
            second.setText(String.valueOf(59-seconds));

            if(14- minutes < 0 ) {
                myCountDownTimer.cancel();
                minute.setText("00");
                second.setText("00");
            }
            if(isAdded()){
                if(14 - minutes < 10)
                    factorTimer.setBackgroundColor(getResources().getColor(R.color.orange));

                if(14 - minutes < 5)
                    factorTimer.setBackgroundColor(getResources().getColor(R.color.red));
            }

            seekBar.setProgress((int) diff);


        }

        @Override
        public void onFinish() {
            myCountDownTimer.cancel();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll("");
        }
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
                    MainActivity.factorcontainer.setVisibility(View.INVISIBLE);
                    BasketListFragment.needToRefrish = true;

                    MainActivity.basketlist.setVisibility(View.VISIBLE);
                    AppConfig.fragmentManager.beginTransaction().replace(R.id.BasketListcontainer, new BasketListFragment()).commit();


                    return true;
                }
                return false;
            }
        });
    }


}
