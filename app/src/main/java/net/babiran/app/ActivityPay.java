package net.babiran.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Fragments.BasketListFragment;
import Handlers.DatabaseHandler;
import Models.Product;
import tools.AppConfig;

/**
 * Created by Alireza on 1/30/2018.
 */

public class ActivityPay extends AppCompatActivity
{

    private ProgressBar progressBar;
    private String SHARJ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);



       // final WebView webView = (WebView) findViewById(R.id.webview);
     /*   webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");
        webView.setWebViewClient(new MyWebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url)
            {
                if (url.contains("babiran.net/api/mellatcallback"))
                    webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);

            }
        });*/


      /*  webView.loadUrl(getIntent().getStringExtra("url"));
        SHARJ = getIntent().getStringExtra("sharj");
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @android.webkit.JavascriptInterface
        public void showHTML(String html)
        {
            Log.e("~!~!loaded", "$$$ html: " + html);
            html = html.replace("<html><head></head><body><pre style=\"word-wrap: break-word; white-space: pre-wrap;\">","");
            html = html.replace("</pre></body></html>","");
            if (TextUtils.isEmpty(html))
            {
                Log.e("~!~!~!1","AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                Toast.makeText(ctx, "پاسخ دریافتی معتبر نمی باشد.", Toast.LENGTH_LONG).show();
            }
            else
                {
                    if(TextUtils.isEmpty(SHARJ))  //    true ===>  Retrn to list
                    {


                        PayResponse payResponse = new Gson().fromJson(html, PayResponse.class);
                        if (payResponse != null && payResponse.success == 1)
                        {

                            Intent intent = new Intent(ActivityPay.this,SharjActivity.class);
                            intent.putExtra("msg",payResponse.message);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                        else//    false ===>  Retrn to Sharj
                        {
//                        NullBasket();
                            Log.e("~!~!~!3","");
                            //  NullBasket();
                            Toast.makeText(ctx, payResponse == null ? "پاسخ دریافتی معتبر نمی باشد." : payResponse.message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ActivityPay.this,SharjActivity.class);
                            assert payResponse != null;
                            intent.putExtra("msg",payResponse.message);
                            setResult(Activity.RESULT_CANCELED,intent);

                            finish();


                        }
                    }
                    else
                    {


                        PayResponse payResponse = new Gson().fromJson(html, PayResponse.class);
                        if (payResponse != null && payResponse.success == 1)
                        {

                            Intent intent = new Intent();
                            intent.putExtra("msg",payResponse.message);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                        else
                        {
//                        NullBasket();
                            Log.e("~!~!~!3","");
                            //  NullBasket();
                            Toast.makeText(ctx, payResponse == null ? "پاسخ دریافتی معتبر نمی باشد." : payResponse.message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            assert payResponse != null;
                            intent.putExtra("msg",payResponse.message);
                            setResult(Activity.RESULT_CANCELED,intent);

                            finish();
                        }
                    }



            }
        }
    }

    public class PayResponse {
        @SerializedName("success")
        public int success;
        @SerializedName("message")
        public String message;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("~!~!~!3","");
        //  NullBasket();
        Intent intent = new Intent(ActivityPay.this,SharjActivity.class);

        intent.putExtra("msg","VV");
        setResult(Activity.RESULT_CANCELED,intent);

        finish();
    }

   /* public class MyWebViewClient extends WebViewClient {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Intent i = new Intent(Intent.ACTION_VIEW, request.getUrl());
            startActivity(i);
            return true;
        }

    }*/
}
