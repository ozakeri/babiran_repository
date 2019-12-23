package net.babiran.app;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Random;

import tools.AppConfig;
import ui_elements.CustomFontEditText;


public class Sms_Register extends AppCompatActivity {

    RelativeLayout BTNSend;
    CustomFontEditText ETName;

    private static String receptor10;
    private static String message10;
    private String receptor;
    private boolean flag = false;

    public String getRandomCode() {
        final int r_ssminimum = 1116;
        final int r_ssmaximum = 9999;
        Random random = new Random();
        final int int_random = random.nextInt(r_ssmaximum - r_ssminimum + 1) + r_ssminimum;
        final String str_Random = String.valueOf(int_random);
        return str_Random;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms__register);

        showGuideDialog();
        requestSmsPermission();

        BTNSend = (RelativeLayout) findViewById(R.id.BTNSend);
        ETName = (CustomFontEditText) findViewById(R.id.ETPhon);
        PhoneNumberUtils.formatNumber(ETName.getText().toString());
        final ProgressDialog d = new ProgressDialog(Sms_Register.this);
        d.setMessage("در حال ارسال کد فعالسازی...");
        BTNSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TESTvR();


                BTNSend.setEnabled(false);
                if (TextUtils.isEmpty(ETName.getText().toString())) {
                    Toast.makeText(Sms_Register.this, "شماره موبایل را وارد کنید", Toast.LENGTH_LONG).show();
                    return;
                }
                d.show();
                RequestHandler.getToken(new GetTokenCallback() {
                    @Override
                    public void onGetTokenSuccessAction(String token) {
                        final String random = getRandomCode();
                        SharedPreferences save = getSharedPreferences("NUMBER", MODE_PRIVATE);
                        SharedPreferences.Editor editor = save.edit();
                        editor.putString("TKN", ETName.getText().toString());
                        editor.apply();

                        Log.e("G", random + "");
                        RequestHandler.sendSMS2(token, random, ETName.getText().toString(), new SendSmsCallback() {
                            @Override
                            public void onSendSmsSuccessAction() {
                                d.dismiss();

                                AppConfig.sent = random;
                                AppConfig.phone = ETName.getText().toString();
                                Toast.makeText(Sms_Register.this, "پیامک حاوی کد فعالسازی به زودی برای شما ارسال می شود.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Sms_Register.this, To_Start.class));
                                finish();
                            }

                            @Override
                            public void onSendSmsErrorAction(String error) {
                                BTNSend.setEnabled(true);
                                d.dismiss();

                                Toast.makeText(Sms_Register.this, error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onGetTokenErrorAction(String error) {
                        BTNSend.setEnabled(true);
                        d.dismiss();
                        startActivity(new Intent(Sms_Register.this, To_Start.class));
                        Toast.makeText(Sms_Register.this, error, Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

    }


    public void sendSms() {

    }

    private void requestSmsPermission() {
        String permission = Manifest.permission.READ_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    public void showGuideDialog() {
        final Dialog alert = new Dialog(Sms_Register.this);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.custom_dialog_guide);
        TextView buttonTextView = alert.findViewById(R.id.txt_action);
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
