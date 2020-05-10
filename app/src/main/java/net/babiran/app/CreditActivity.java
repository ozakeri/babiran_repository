package net.babiran.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyMesa;
import net.babiran.app.Servic.MyServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Handlers.DatabaseHandler;
import retrofit2.Call;
import retrofit2.Callback;
import tools.AppConfig;
import tools.Util;

public class CreditActivity extends AppCompatActivity {

    private EditText edt_price;
    private RadioButton radioButton1, radioButton2, radioButton3;
    private TextView txt_validity;
    private RelativeLayout btn_pay;
    public static final int REQUEST_CODE_PAY = 101;
    private AppCompatImageView btn_back;
    private String id = "";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
        btn_back = findViewById(R.id.btn_back);
        btn_pay = findViewById(R.id.btn_pay);


        getUserId();

        edt_price.setTypeface(Typeface.createFromAsset(getAssets(), "iransans.ttf"));
        radioButton1.setTypeface(Typeface.createFromAsset(getAssets(), "iransans.ttf"));
        radioButton2.setTypeface(Typeface.createFromAsset(getAssets(), "iransans.ttf"));
        radioButton3.setTypeface(Typeface.createFromAsset(getAssets(), "iransans.ttf"));

        if (credit.isEmpty() || credit.equals("")) {
            credit = "0";
        }

        radioButton1.setChecked(true);

        //edt_price.setText(Util.PersianNumber("10000"));
        edt_price.setText(Util.latinNumberToPersian("10000"));
        edt_price.setText(Util.latinNumberToPersian(Util.convertToFormalString(("20000"))));
        radioButton1.setText(Util.latinNumberToPersian(Util.convertToFormalString(("20000"))));
        radioButton2.setText(Util.latinNumberToPersian(Util.convertToFormalString(("30000"))));
        radioButton3.setText(Util.latinNumberToPersian(Util.convertToFormalString(("50000"))));

        edt_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // edt_price.setText(Util.latinNumberToPersian(Util.convertToFormalString((s.toString()))));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_price.length() > 10){
                    return;
                }
                edt_price.removeTextChangedListener(this);
                String text=edt_price.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    String textWithoutComma = text.replaceAll(",", "");
                    String englishNums=new BigDecimal(textWithoutComma).toString();//****
                    double number = Double.valueOf(englishNums);//****
                    String formattedNumber=formatNumber(number);
                    edt_price.setText(Util.latinNumberToPersian(formattedNumber));
                    edt_price.setSelection(formattedNumber.length());
                }
                edt_price.addTextChangedListener(this);
            }
        });

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton1.setChecked(true);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                //edt_price.setText(Util.PersianNumber("15000"));
                edt_price.setText(Util.latinNumberToPersian(Util.convertToFormalString(("20000"))));
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton2.setChecked(true);
                radioButton1.setChecked(false);
                radioButton3.setChecked(false);
                //edt_price.setText(Util.PersianNumber("30000"));
                edt_price.setText(Util.latinNumberToPersian(Util.convertToFormalString(("30000"))));
            }
        });

        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton3.setChecked(true);
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                //edt_price.setText(Util.PersianNumber("50000"));
                edt_price.setText(Util.latinNumberToPersian(Util.convertToFormalString(("50000"))));
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionPay();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void actionPay() {


        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
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

    public void getCreditRequest() {

        //sendToken();

        RequestQueue queue = Volley.newRequestQueue(CreditActivity.this);

        if (id.equals("")) {
            id = "-1";
        }
        Log.d("idd", id);

        final String url = AppConfig.BASE_URL + "api/main/getCredit/" + id;
        System.out.println("getCredit=====" + url);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            String credit = null;
                            try {
                                credit = response.getString("credit");
                                if (response.getString("credit") == null) {
                                    credit = "0";
                                }
                                Pattern p = Pattern.compile("\\d+");
                                Matcher m = p.matcher(credit);
                                while (m.find()) {
                                    txt_validity.setText(Util.PersianNumber(m.group()));
                                    txt_validity.setText(Util.latinNumberToPersian(Util.convertToFormalString((m.group()))));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppConfig.error(error);
                    }
                }
        );

        queue.add(getRequest);

    }

    public void getUserId() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        if (db.getRowCount() > 0) {
            HashMap<String, String> userDetailsHashMap = db.getUserDetails();
            id = userDetailsHashMap.get("id");
            if (id != null) {
                getCreditRequest();
            }

        }
    }

    public String formatNumber(double number){
        DecimalFormat format=new DecimalFormat("#,###");
        return format.format(number);
    }
}

