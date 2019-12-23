package net.babiran.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyMesa;
import net.babiran.app.Servic.MyServices;
import net.babiran.app.Sharj.SharjHistoryActivity;

import retrofit2.Call;
import retrofit2.Callback;

public class SharjActivity extends AppCompatActivity {
    private String Mablagh = "88", Type = "88", operator = "88"; //Type=1=>mostaghim   && operator= 1 =>irancel ,2=>hamrahaval ,3=>rightel
    private ImageView Irancell, Hamrah, Righttel;
    private LinearLayout btn, History;
    private EditText editText;
    private TextView tx;
    public static final int REQUEST_CODE_PAY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
//
        /////////////////////////////////////
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

////////////////////////////////////////////
        INIT();
    }


    private void INIT() {
        Irancell = (ImageView) findViewById(R.id.irancel);
        Hamrah = (ImageView) findViewById(R.id.hamrahaval);
        Righttel = (ImageView) findViewById(R.id.righttel);

        editText = (EditText) findViewById(R.id.ed_number);
        tx = (TextView) findViewById(R.id.txt_show_op);

        History = (LinearLayout) findViewById(R.id.kdjfnbgkjdfnkj);
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SharjActivity.this, SharjHistoryActivity.class));
            }
        });

        btn = (LinearLayout) findViewById(R.id.pymeny_sharj);
        RadioBTNMablagh();
        RadioBTNType();
        opretator();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    if (!Mablagh.equals("88") && !Type.equals("88") && !operator.equals("88")) {
                        SenDToServer();
                    } else {
                        Toast.makeText(SharjActivity.this, "تمام موارد را به درستی انتخاب نمایید", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SharjActivity.this, "شماره همراه خود را صحیح وارد نمایید", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void RadioBTNMablagh() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.myRadioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.hezar:
                        Mablagh = "1000";
                        // do operations specific to this selection
                        break;
                    case R.id.dohezar:
                        Mablagh = "2000";
                        // do operations specific to this selection
                        break;
                    case R.id.hezar5:
                        Mablagh = "5000";
                        // do operations specific to this selection
                        break;
                    case R.id.hezar10:
                        Mablagh = "10000";
                        // do operations specific to this selection
                        break;

                }
            }
        });
    }

    private void RadioBTNType() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.myRadi);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mostaghim:
                        Type = "1";
                        // do operations specific to this selection
                        break;
                    case R.id.ghiremostaghim:
                        Type = "0";
                        // do operations specific to this selection
                        break;

                }
            }
        });
    }

    private void opretator() {
        Irancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = "1";
                tx.setText("ایرانسل");
                tx.setVisibility(View.VISIBLE);
            }
        });
        Hamrah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = "2";
                tx.setText("همراه اول");
                tx.setVisibility(View.VISIBLE);
            }
        });
        Righttel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = "3";
                tx.setText("رایتل");
                tx.setVisibility(View.VISIBLE);
            }
        });
    }

    private void SenDToServer() {


        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<MyMesa> call = n.BuySahrj(editText.getText().toString(), Mablagh, Type, operator);

            call.enqueue(new Callback<MyMesa>() {
                @Override
                public void onResponse(@NonNull Call<MyMesa> call, @NonNull retrofit2.Response<MyMesa> response) {
                    try {
                        Integer fetching = response.body().getSuccess();

                        if (fetching == 1) {
                            Log.e("URL  ", response.body().getUrl());
                            Intent intent = new Intent(SharjActivity.this, Actip2.class);
                            intent.putExtra("url", response.body().getUrl());
                            intent.putExtra("sharj", "sharj");
                            startActivityForResult(intent, REQUEST_CODE_PAY);

                        } else {
                            Toast.makeText(SharjActivity.this, "مشکلی در ارتباط با سرور پیش امده", Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {
                        Log.e("EX", e.getMessage());
                    }


                }

                @Override
                public void onFailure(Call<MyMesa> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage() + "\n" + t.toString());
                }
            });
        } catch (Exception ex) {
            Log.e("response 3 :", ex.getMessage());
        }


    }

    private void Dilago(String m) {
        final Dialog dialog = new Dialog(SharjActivity.this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.coustom_dialog_sharj);
        dialog.setCancelable(true);
        dialog.show();

        TextView txt = (TextView) dialog.findViewById(R.id.txtidsharjcode);
        txt.setText(m);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAY) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(SharjHistoryActivity.this,"ok",Toast.LENGTH_LONG).show();
                if (data != null){
                    String s = data.getStringExtra("msg");
                    Log.e("RESULT", s);
                    Dilago(s);
                }

            } else {
                // Toast.makeText(SharjHistoryActivity.this,"not",Toast.LENGTH_LONG).show();
                if (data != null){
                    String s = data.getStringExtra("msg");
                    Log.e("RESULT", s);
                    Toast.makeText(SharjActivity.this, s, Toast.LENGTH_SHORT).show();
                }

            }

        }

    }
}

