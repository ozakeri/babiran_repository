package net.babiran.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyMesa;
import net.babiran.app.Servic.MyServices;

import retrofit2.Call;
import retrofit2.Callback;
import tools.AppConfig;

public class CreditActivity extends AppCompatActivity {

    private EditText edt_price;
    private RadioButton radioButton1, radioButton2, radioButton3;
    private TextView txt_validity;
    private RelativeLayout btn_pay;
    public static final int REQUEST_CODE_PAY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        String credit = AppController.getInstance().getSharedPreferences().getString("credit", "");
        edt_price = findViewById(R.id.edt_price);
        radioButton1 = findViewById(R.id.option_first);
        radioButton2 = findViewById(R.id.option_second);
        radioButton3 = findViewById(R.id.option_third);
        txt_validity = findViewById(R.id.txt_validity);
        btn_pay = findViewById(R.id.btn_pay);

        if (credit.isEmpty() || credit.equals("")) {
            credit = "0";
        }
        txt_validity.setText("موجودی کیف پول شما : " + credit + " تومان ");
        radioButton1.setChecked(true);
        edt_price.setText("100");


        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton1.setChecked(true);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                edt_price.setText("100");
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton2.setChecked(true);
                radioButton1.setChecked(false);
                radioButton3.setChecked(false);
                edt_price.setText("200");
            }
        });

        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton3.setChecked(true);
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                edt_price.setText("300");
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionPay();
            }
        });
    }

    private void actionPay() {


        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            System.out.println("--------" + Integer.parseInt(AppConfig.id) + "---" + edt_price.getText().toString());
            Call<MyMesa> call = n.BuyCredit(Integer.parseInt(AppConfig.id), edt_price.getText().toString());

            call.enqueue(new Callback<MyMesa>() {
                @Override
                public void onResponse(@NonNull Call<MyMesa> call, @NonNull retrofit2.Response<MyMesa> response) {
                    try {
                        if (response.body() != null) {

                            System.out.println("response======" + response.body());
                            Integer fetching = response.body().getSuccess();

                            if (fetching == 1) {

                                Log.e("URL  ", response.body().getUrl());
                          /*  Log.e("URL  ", response.body().getUrl());
                            Intent intent = new Intent(SharjActivity.this, Actip2.class);
                            intent.putExtra("url", response.body().getUrl());
                            intent.putExtra("sharj", "sharj");*/

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().getUrl()));
                                startActivityForResult(browserIntent, REQUEST_CODE_PAY);
                                //startActivity(browserIntent);

                            } else {
                                Toast.makeText(CreditActivity.this, "مشکلی در ارتباط با سرور پیش امده", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAY) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String s = data.getStringExtra("msg11111");
                    Log.e("RESULT11111", s);
                    Toast.makeText(CreditActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            } else {
                if (data != null) {
                    String s = data.getStringExtra("msg222222");
                    Log.e("RESULT222222", s);
                    Toast.makeText(CreditActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

