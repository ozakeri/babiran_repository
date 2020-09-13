package net.babiran.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyMesa;
import net.babiran.app.Servic.MyServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Handlers.DatabaseHandler;
import retrofit2.Call;
import retrofit2.Callback;
import tools.AppConfig;
import tools.GlobalValues;
import tools.NumberTextWatcherForThousand;
import tools.Util;
import ui_elements.MyTextView;

public class CreditActivity extends AppCompatActivity {

    private EditText edt_price;
    private RadioButton radioButton1, radioButton2, radioButton3;
    private MyTextView txt_validity;
    private RelativeLayout btn_pay;
    public static final int REQUEST_CODE_PAY = 101;
    private AppCompatImageView btn_back;
    private String id = "";
    private GlobalValues globalValues = new GlobalValues();
    private CircularProgressView waitProgress;

    @SuppressLint("SetTextI18n")
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
        btn_back = findViewById(R.id.btn_back);
        btn_pay = findViewById(R.id.btn_pay);
        waitProgress = findViewById(R.id.waitProgress);
        //edt_price.setEnabled(false);

        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri != null) {
                String success = uri.getQueryParameter("success");
                getCreditRequest();
                showGuideDialog(success);
            }
        }


        getUserId();

        edt_price.setTypeface(Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));
        radioButton1.setTypeface(Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));
        radioButton2.setTypeface(Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));
        radioButton3.setTypeface(Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));
        txt_validity.setTypeface(Typeface.createFromAsset(getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));

        //radioButton1.setChecked(true);

        //edt_price.setText(Util.PersianNumber("50000"));
        //edt_price.setText(Util.convertToFormalString(String.valueOf((10000))));
        radioButton1.setText(Util.latinNumberToPersian(Util.convertToFormalString(("50000"))));
        radioButton2.setText(Util.latinNumberToPersian(Util.convertToFormalString(("100000"))));
        radioButton3.setText(Util.latinNumberToPersian(Util.convertToFormalString(("150000"))));
        edt_price.addTextChangedListener(new NumberTextWatcherForThousand(edt_price));
        edt_price.setSelection(edt_price.getText().length());


        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton1.setChecked(true);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                edt_price.setText(Util.convertToFormalString(String.valueOf((50000))));
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton2.setChecked(true);
                radioButton1.setChecked(false);
                radioButton3.setChecked(false);
                edt_price.setText(Util.convertToFormalString(String.valueOf((100000))));
            }
        });

        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton3.setChecked(true);
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                edt_price.setText(Util.convertToFormalString(String.valueOf((150000))));
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = edt_price.getText().toString().replaceAll(",", "");
                System.out.println("onClick=====" + s);
                if (s.length() > 0) {

                    Handler handler = new Handler();
                    Thread someThread = new Thread() {

                        @Override
                        public void run() {

                            //some actions
                            handler.post(new Runnable() {
                                public void run() {
                                    actionPay(Integer.parseInt(s));
                                }
                            });
                        }
                    };

                    someThread.start();
                    //actionPay(Integer.parseInt(s));
                }else {
                    Toast.makeText(CreditActivity.this, "مبلغ را وارد کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MainActivity.btnBack.setVisibility(View.INVISIBLE);
                MainActivity.viewLogo.setVisibility(View.VISIBLE);
                MainActivity.layout_search.setVisibility(View.VISIBLE);
                MainActivity.home.setVisibility(View.VISIBLE);
            }
        });


    }

    private void actionPay(int price) {
        System.out.println("price1=====" + price);
        waitProgress.setVisibility(View.VISIBLE);
        try {
        System.out.println("AppConfig.id=====" + AppConfig.id);
        System.out.println("AppConfig.id=====" + id);

            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<MyMesa> call = n.BuyCredit(Integer.parseInt(id), price);
            System.out.println("price2=====" + price);
            call.enqueue(new Callback<MyMesa>() {
                @Override
                public void onResponse(@NonNull Call<MyMesa> call, @NonNull retrofit2.Response<MyMesa> response) {
                    try {
                        if (response.body() != null) {
                            waitProgress.setVisibility(View.GONE);
                            Integer fetching = response.body().getSuccess();

                            if (fetching == 1) {

                                Log.e("URL  ", response.body().getUrl());
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().getUrl()));
                                startActivityForResult(browserIntent, REQUEST_CODE_PAY);
                                finish();

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

        txt_validity = (MyTextView) findViewById(R.id.txt_validity);
        RequestQueue queue = Volley.newRequestQueue(CreditActivity.this);

        if (id.equals("")) {
            id = "-1";
        }

        final String url = AppConfig.BASE_URL + "api/main/getCredit/" + id;
        System.out.println("getCredit=====" + url);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                String credit = response.getString("credit");

                                if (response.getString("credit") == null) {
                                    credit = "0";
                                }
                                //EventBus.getDefault().post(new EventbusModel(credit));
                                globalValues.setCreditValue(credit);
                                Pattern p = Pattern.compile("\\d+");
                                Matcher m = p.matcher(credit);
                                while (m.find()) {
                                    txt_validity.setText(Util.convertToFormalString((m.group())));

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
            System.out.println("=====id=====" + id);
            if (id != null) {
                getCreditRequest();
            }

        }
    }

    public String formatNumber(double number) {
        DecimalFormat format = new DecimalFormat("#,###");
        return format.format(number);
    }

    public void showGuideDialog(String success) {
        final Dialog alert = new Dialog(CreditActivity.this);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.custom_dialog_after_pay);
        TextView buttonTextView = alert.findViewById(R.id.txt_action);
        MyTextView txt_status = alert.findViewById(R.id.txt_status);
        AppCompatImageView statusIcon = alert.findViewById(R.id.statusIcon);
        alert.setCancelable(false);

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

    @Override
    protected void onResume() {
        super.onResume();
        getCreditRequest();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        MainActivity.btnBack.setVisibility(View.INVISIBLE);
        MainActivity.viewLogo.setVisibility(View.VISIBLE);
        MainActivity.layout_search.setVisibility(View.VISIBLE);
        MainActivity.home.setVisibility(View.VISIBLE);
    }
}

