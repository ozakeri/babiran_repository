package net.babiran.app;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import Models.Product;
import ui_elements.MyTextView;

/**
 * Created by Mohammad on 7/27/2017.
 */

public class GuideUsageActivity extends AppCompatActivity {

    Product product = null;
    ListView Feautures;
    MyTextView name, nofeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_common_questions);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.btnBack.setVisibility(View.GONE);
                MainActivity.viewLogo.setVisibility(View.VISIBLE);
                finish();
            }
        });
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("http://babiran.net/api/app-guide");
    }
    @Override
    public void onBackPressed() {
        MainActivity.btnBack.setVisibility(View.GONE);
        MainActivity.viewLogo.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }
}
