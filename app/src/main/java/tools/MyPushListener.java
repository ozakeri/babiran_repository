package tools;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.volley.RequestQueue;

import net.babiran.app.AppController;
import net.babiran.app.FactorList;
import net.babiran.app.MainActivity;
import net.babiran.app.R;
import net.babiran.app.Rss.ListtoListActivity;
import net.babiran.app.Rss.ShowRssActivity;
import net.babiran.app.Sefaresh.ShowActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Handlers.DatabaseHandler;
import co.ronash.pushe.PusheListenerService;

public class MyPushListener extends PusheListenerService {
    private NotificationManager mNotificationManager;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;
    private DatabaseHandler db;

    @Override
    public void onMessageReceived(final JSONObject message, final JSONObject content) {

       /* JSONObject resultJson = null;
        try {
            resultJson = new JSONObject(message.toString());
            if (!resultJson.isNull("type")) {
                String type = resultJson.getString("type");
                if (type.equals("123")){
                    System.out.println("resultJson====" + resultJson);
                    new Thread(new Task()).start();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/



        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {


                try {
                    JSONObject contentJson = new JSONObject(String.valueOf(content));
                    System.out.println("contentJson=====" + contentJson);
                    if (!contentJson.isNull("content")) {
                        JSONObject successJson = contentJson.getJSONObject("content");
                        if (!successJson.isNull("success")) {
                            String successStr = successJson.getString("success");
                            System.out.println("successStr=====" + successStr);
                            if (successStr != null) {
                                new Thread(new Task()).start();
                                return;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    JSONObject resultJson = new JSONObject(message.toString());

                    String title = null;
                    String body = null;
                    String id = null;
                    String type = null;
                    String image = null;
                    String catId = null;
                    String proId = null;


                    if (!resultJson.isNull("type")) {
                        String type123 = resultJson.getString("type");
                        if (type123.equals("123")){
                            System.out.println("resultJson====" + resultJson);
                            new Thread(new Task()).start();
                        }
                    }


                    if (!resultJson.isNull("title")) {
                        title = resultJson.getString("title");
                    } else {
                        title = "بابیران";
                    }

                    if (!resultJson.isNull("body")) {
                        body = resultJson.getString("body");
                    } else {
                        body = "بابیران";
                    }

                    if (!resultJson.isNull("id")) {
                        id = resultJson.getString("id");
                    }

                    if (!resultJson.isNull("type")) {
                        type = resultJson.getString("type");
                    }

                    if (!resultJson.isNull("img_url")) {
                        image = resultJson.getString("img_url");
                    }

                    if (!resultJson.isNull("cat_id")) {
                        catId = resultJson.getString("cat_id");
                    }

                    if (!resultJson.isNull("pro_id")) {
                        proId = resultJson.getString("pro_id");
                    }


                    if (type != null) {
                        switch (type) {
                            case "0": {

                                if (id != null) {
                                    ListtoListActivity.ID_ME = id;
                                    Intent intent = new Intent(getApplicationContext(), ShowRssActivity.class);
                                    showNotification(getApplicationContext(), intent, title, body);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    showNotification(getApplicationContext(), intent, title, body);
                                }

                                break;
                            }
                            case "1": {
                                editor = AppController.getInstance().getSharedPreferences().edit();
                                editor.putBoolean("newPush", true);
                                editor.apply();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                showNotification(getApplicationContext(), intent, title, body);

                                break;
                            }
                            case "2": {

                                if (id != null && image != null) {
                                    editor = AppController.getInstance().getSharedPreferences().edit();
                                    editor.putBoolean("productFood", true);
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
                                    intent.putExtra("IDDD", id);
                                    intent.putExtra("IsdfDDD", image);
                                    showNotification(getApplicationContext(), intent, title, body);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    showNotification(getApplicationContext(), intent, title, body);
                                }

                                break;
                            }
                            case "3": {


                                if (catId != null && proId != null) {
                                    editor = AppController.getInstance().getSharedPreferences().edit();
                                    editor.putBoolean("getProduct", true);
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("cat_id", catId);
                                    intent.putExtra("pro_id", proId);
                                    showNotification(getApplicationContext(), intent, title, body);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    showNotification(getApplicationContext(), intent, title, body);
                                }


                                break;
                            }


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, 10);
    }

    public void showNotification(Context context, Intent intent, String title, String body) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        notificationManager.notify(notificationId, mBuilder.build());
    }

    private class Task implements Runnable {
        @Override
        public void run() {

            db = new DatabaseHandler(getApplicationContext());
            if (db.getRowCount() > 0) {
                HashMap<String, String> userDetailsHashMap = db.getUserDetails();
                String id = userDetailsHashMap.get("id");
                Intent intent = new Intent(getApplicationContext(), FactorList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        }
    }

}
