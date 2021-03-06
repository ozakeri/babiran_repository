package net.babiran.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Actip2 extends AppCompatActivity
{

    private ProgressBar progressBar;
    //private String SHARJ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(Actip2.this), "HtmlViewer");
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                if (url.contains("babiran.net/api/chargCallback"))
                    webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);

            }


        });
        webView.loadUrl(getIntent().getStringExtra("url"));
        //SHARJ = getIntent().getStringExtra("sharj");
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

       /* @android.webkit.JavascriptInterface
        public void showHTML(String html)
        {

            Log.e("HTMK :" , html);
            html = html.replace("<html><head></head><body><pre style=\"word-wrap: break-word; white-space: pre-wrap;\">","");
            html = html.replace("</pre></body></html>","");
            if (TextUtils.isEmpty(html))
            {
                Log.e("~!~!~!1","AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                Toast.makeText(ctx, "پاسخ دریافتی معتبر نمی باشد.", Toast.LENGTH_LONG).show();
            }
            else
            {



                    ActivityPay.PayResponse payResponse = new Gson().fromJson(html, ActivityPay.PayResponse.class);
                    if (payResponse != null && payResponse.success == 1)
                    {

                        Intent intent = new Intent(Actip2.this,SharjActivity.class);
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
                        Intent intent = new Intent(Actip2.this,SharjActivity.class);
                        assert payResponse != null;
                        intent.putExtra("msg",payResponse.message);
                        setResult(Activity.RESULT_CANCELED,intent);

                        finish();


                    }




            }
        }*/
    }

   /* public class PayResponse {
        @SerializedName("success")
        public int success;
        @SerializedName("message")
        public String message;
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("~!~!~!3","");
        //  NullBasket();
        Intent intent = new Intent(Actip2.this,SharjActivity.class);

        intent.putExtra("msg","VV");
        setResult(Activity.RESULT_CANCELED,intent);

        finish();
    }
}

