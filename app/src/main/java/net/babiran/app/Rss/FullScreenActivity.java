package net.babiran.app.Rss;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import net.babiran.app.AppStore;
import net.babiran.app.R;
import net.babiran.app.Servic.GETINGBlog;
import net.babiran.app.Servic.GetComm;
import net.babiran.app.Servic.GetSucc;
import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyServices;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import tools.AppConfig;
import tools.TouchImageView;
import ui_elements.MyButton;
import ui_elements.MyEditText;
import ui_elements.MyTextView;

public class FullScreenActivity extends AppCompatActivity {
    private TouchImageView image_View;
    private RelativeLayout layoutToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        image_View = (TouchImageView) findViewById(R.id.image_View);
        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String path = bundle.getString("imgUrl");
            if (path != null){
                Glide.with(FullScreenActivity.this).load(path).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).placeholder(R.drawable.logoloading).into(image_View);
            }
        }

        image_View.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                PointF point = image_View.getScrollPosition();
                RectF rect = image_View.getZoomedRect();
                float currentZoom = image_View.getCurrentZoom();
                boolean isZoomed = image_View.isZoomed();
            }
        });

    }

    public Bitmap resizeBitmap(String photoPath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int scaleFactor = 1;
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21
        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
