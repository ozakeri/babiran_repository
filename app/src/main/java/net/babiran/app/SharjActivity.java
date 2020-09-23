package net.babiran.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyMesa;
import net.babiran.app.Servic.MyServices;
import net.babiran.app.Sharj.SharjHistoryActivity;

import retrofit2.Call;
import retrofit2.Callback;
import ui_elements.MyTextView;

public class SharjActivity extends AppCompatActivity {
    private String Mablagh = "88", Type = "88", operator = "88"; //Type=1=>mostaghim   && operator= 1 =>irancel ,2=>hamrahaval ,3=>rightel
    private ImageView Irancell, Hamrah, Righttel;
    private LinearLayout btn, History;
    private EditText editText;
    private TextView tx;
    public static final int REQUEST_CODE_PAY = 101;
    private RelativeLayout layout_irancell, layout_hamrah, layout_ritel;
    private LinearLayout layout_afterSelect;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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

        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri != null) {
                String success = uri.getQueryParameter("success");
                showGuideDialog(success);
            }
        }

    }


    private void INIT() {
        Irancell = findViewById(R.id.irancel);
        Hamrah = findViewById(R.id.hamrahaval);
        Righttel = findViewById(R.id.righttel);
        layout_irancell = findViewById(R.id.layout_irancell);
        layout_hamrah = findViewById(R.id.layout_hamrah);
        layout_ritel = findViewById(R.id.layout_ritel);
        layout_afterSelect = findViewById(R.id.layout_afterSelect);
        editText = (EditText) findViewById(R.id.ed_number);
        tx = (TextView) findViewById(R.id.txt_show_op);
        editText.setTypeface(Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));
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
        //RadioGroup rg = (RadioGroup) findViewById(R.id.myRadioGroup);
        RadioButton r1 = findViewById(R.id.hezar);
        RadioButton r2 = findViewById(R.id.dohezar);
        RadioButton r3 = findViewById(R.id.hezar5);
        RadioButton r4 = findViewById(R.id.hezar10);
        RadioButton mostaghim = findViewById(R.id.mostaghim);
        RadioButton ghiremostaghim = findViewById(R.id.ghiremostaghim);
        r1.setTypeface((Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));
        r2.setTypeface((Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));
        r3.setTypeface((Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));
        r4.setTypeface((Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));
        mostaghim.setTypeface((Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));
        ghiremostaghim.setTypeface((Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));

       /* rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        });*/
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
        layout_irancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_afterSelect.setVisibility(View.VISIBLE);
                operator = "1";
                tx.setText("ایرانسل");
                tx.setVisibility(View.VISIBLE);
                layout_irancell.setBackgroundResource(R.color.forooze_transparent);
                layout_hamrah.setBackgroundResource(0);
                layout_ritel.setBackgroundResource(0);
            }
        });
        layout_hamrah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_afterSelect.setVisibility(View.VISIBLE);
                operator = "2";
                tx.setText("همراه اول");
                tx.setVisibility(View.VISIBLE);
                layout_hamrah.setBackgroundResource(R.drawable.hamrah_background);
                layout_irancell.setBackgroundResource(0);
                layout_ritel.setBackgroundResource(0);
            }
        });
        layout_ritel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_afterSelect.setVisibility(View.VISIBLE);
                operator = "3";
                tx.setText("رایتل");
                tx.setVisibility(View.VISIBLE);
                //layout_ritel.setBackgroundResource(R.color.forooze_transparent);
                layout_ritel.setBackgroundResource(R.drawable.rigtel_background);
                layout_irancell.setBackgroundResource(0);
                layout_hamrah.setBackgroundResource(0);
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
                        if (response.body() != null) {
                            Integer fetching = response.body().getSuccess();

                            if (fetching == 1) {

                                Log.e("URL  ", response.body().getUrl());
                          /*  Log.e("URL  ", response.body().getUrl());
                            Intent intent = new Intent(SharjActivity.this, Actip2.class);
                            intent.putExtra("url", response.body().getUrl());
                            intent.putExtra("sharj", "sharj");*/

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().getUrl()));
                                //startActivityForResult(intent, REQUEST_CODE_PAY);
                                startActivity(browserIntent);
                                finish();
                            } else {
                                Toast.makeText(SharjActivity.this, "مشکلی در ارتباط با سرور پیش امده", Toast.LENGTH_LONG).show();
                            }
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
                if (data != null) {
                    String s = data.getStringExtra("msg");
                    Log.e("RESULT", s);
                    Dilago(s);
                }

            } else {
                // Toast.makeText(SharjHistoryActivity.this,"not",Toast.LENGTH_LONG).show();
                if (data != null) {
                    String s = data.getStringExtra("msg");
                    Log.e("RESULT", s);
                    Toast.makeText(SharjActivity.this, s, Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

    public void showGuideDialog(String success) {
        final Dialog alert = new Dialog(SharjActivity.this);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.custom_dialog_after_pay);
        TextView buttonTextView = alert.findViewById(R.id.txt_action);
        MyTextView txt_status = alert.findViewById(R.id.txt_status);
        AppCompatImageView statusIcon = alert.findViewById(R.id.statusIcon);


        if (success != null && success.equals("1")) {
            statusIcon.setBackgroundResource(R.drawable.success);
            txt_status.setText("پرداخت با موفقیت انجام شد");

        } else {
            statusIcon.setBackgroundResource(R.drawable.unsucceess);
            txt_status.setText("پرداخت نا موفق");
        }

        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}

