package net.babiran.app;

import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.farsitel.bazaar.IUpdateCheckService;

import java.util.HashMap;

import Handlers.DatabaseHandler;
import tools.AppConfig;


public class SplashActivity extends AppCompatActivity {

    IUpdateCheckService service;
    UpdateServiceConnection connection;
    private static final String TAG = "UpdateCheck";


    class UpdateServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IUpdateCheckService.Stub
                    .asInterface((IBinder) boundService);
            try {
                long vCode = service.getVersionCode("net.babiran.app");
                long current_version = -2;

                //Toast.makeText(SplashActivity.this, "Version Code:" + vCode, Toast.LENGTH_LONG).show();
                AppConfig.vCode = vCode;
                Log.e("vCode", vCode + "");


            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onServiceConnected(): Connected");
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.d(TAG, "onServiceDisconnected(): Disconnected");
        }
    }

    SharedPreferences.Editor editor;

    /*public void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            // Place your dialog code here to display the dialog

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();

        }

        else
        {

            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
            this.finish();

        }

    }*/

    SharedPreferences prefs = null;


    public void playanim() {

        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);

        animationView.setAnimation("data.json");

        animationView.setImageAssetsFolder("images");


        animationView.loop(false);

        animationView.playAnimation();

        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Toast.makeText(getApplicationContext(),"finished",Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getApplicationContext(), SlidesActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Log.e("Splash", "Splash");


        prefs = getSharedPreferences("co.nkit.babiran", MODE_PRIVATE);

        initService();
        AppConfig.code = "";

        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        //checkFirstRun();

        if (db.getRowCount() > 0) {

            HashMap user = db.getUserDetails();
            AppConfig.id = user.get("id").toString();
            AppConfig.phone = user.get("phone1").toString();
            //Toast.makeText(getBaseContext(),AppConfig.id,Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
            finish();
        } else {
            playanim();
        }

    }

    private void initService() {
        Log.i(TAG, "initService()");
        connection = new UpdateServiceConnection();
        Intent i = new Intent(
                "com.farsitel.bazaar.service.UpdateCheckService.BIND");
        i.setPackage("com.farsitel.bazaar");
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "initService() bound value: " + ret);
    }

    /**
     * This is our function to un-binds this activity from our service.
     */
    private void releaseService() {
        unbindService(connection);
        connection = null;
        Log.d(TAG, "releaseService(): unbound.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
    }

    private void requestSmsPermission() {
        String permission = android.Manifest.permission.READ_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            try {

                prefs.edit().putBoolean("firstrun", false).commit();

            } catch (Exception ex) {

                AppConfig.error(ex);

            }
        } else {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
            finish();
        }

    }


}
