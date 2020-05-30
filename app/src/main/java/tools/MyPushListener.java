package tools;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;

import net.babiran.app.AppController;
import net.babiran.app.FactorList;
import net.babiran.app.MainActivity;
import net.babiran.app.R;
import net.babiran.app.Rss.ListtoListActivity;
import net.babiran.app.Rss.ShowRssActivity;

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
    private GlobalValues globalValues = new GlobalValues();
    private String pro_id, cat_id, title, body = null;

    @Override
    public void onMessageReceived(final JSONObject message, final JSONObject content) {

        System.out.println("message=====" + message.toString());
        System.out.println("content=====" + content.toString());

        /*try {
            JSONObject contentJson = new JSONObject(String.valueOf(content));
            // System.out.println("contentJson=====" + contentJson);
            if (!contentJson.isNull("content")) {

                String jsonObjectStr = contentJson.getString("content");
                jsonObjectStr = jsonObjectStr.replaceAll("\"", "");
                jsonObjectStr = jsonObjectStr.replaceAll("=", ":");
                System.out.println("jsonObjectStr=====" + jsonObjectStr);
                String[] kvPairs = jsonObjectStr.split(",");

                for (String kvPair : kvPairs) {
                    String[] kv = kvPair.split(":");
                    String key = kv[0];
                    String value = kv[1];


                    key = key.replaceAll("([{-}])", "");
                    value = value.replaceAll("([{-}])", "");

                    System.out.println("key=====" + key);
                    System.out.println("value=====" + value);

                    if (key.equals(" pro_id")){
                        key = key.replace(" pro_id","pro_id");
                    }if (key.equals(" title")){
                        key = key.replace(" title","title");
                    }if (key.equals(" cat_id")){
                        key = key.replace(" cat_id","cat_id");
                    }if (key.equals(" body")){
                        key = key.replace(" body","body");
                    }

                    if (key.equals("pro_id")) {
                        pro_id = value;
                    }

                    if (key.equals("cat_id")) {
                        cat_id = value;
                    }

                    if (key.equals("title")) {
                        title = value;
                    }

                    if (key.equals("body")) {
                        body = value;
                    }

                    System.out.println("pro_id=====" + pro_id);
                    System.out.println("cat_id=====" + cat_id);
                    System.out.println("title=====" + title);
                    System.out.println("body=====" + body);

                }

                if (pro_id != null && cat_id != null) {
                    editor = AppController.getInstance().getSharedPreferences().edit();
                    editor.putBoolean("getProduct", true);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("pro_id", pro_id);
                    intent.putExtra("cat_id", cat_id);
                    // showNotification(getApplicationContext(), intent, titleStr, bodyStr);
                    showNotification(getApplicationContext(), intent, title, body);
                }

              *//*  JSONObject jsonObject = new JSONObject(jsonObjectStr);
                if (!jsonObject.isNull("pro_id") && !jsonObject.isNull("cat_id")) {

                    String pro_idStr = jsonObject.getString("pro_id");
                    System.out.println("pro_idStr=====" + pro_idStr);

                    String cat_idStr = jsonObject.getString("cat_id");
                    System.out.println("cat_id=====" + cat_idStr);

                    String titleStr = jsonObject.getString("title");
                    System.out.println("titleStr=====" + titleStr);

                    String bodyStr = jsonObject.getString("body");
                    System.out.println("bodyStr=====" + bodyStr);


                    if (pro_idStr != null && cat_idStr != null) {
                        editor = AppController.getInstance().getSharedPreferences().edit();
                        editor.putBoolean("getProduct", true);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("cat_id", pro_idStr);
                        intent.putExtra("pro_id", cat_idStr);
                        // showNotification(getApplicationContext(), intent, titleStr, bodyStr);
                        showNotification(getApplicationContext(), titleStr, bodyStr, intent);
                    }
                }*//*


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        try {
            JSONObject resultJson = new JSONObject(message.toString());

            String title = null;
            String body = null;
            String id = null;
            String type = null;


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


            if (type != null) {
                globalValues.setPush(true);
                switch (type) {
                    case "123": {

                        if (id != null) {
                            ListtoListActivity.ID_ME = id;
                            Intent intent = new Intent(getApplicationContext(), ShowRssActivity.class);
                            intent.putExtra("isPush", true);
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

    public void showNotification(Context context, Intent intent, String title, String body) {

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push);
        contentView.setImageViewResource(R.id.image, R.drawable.applogo);
        contentView.setTextViewText(R.id.title, title);
        contentView.setTextViewText(R.id.body, body);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(title)
                .setContentText(body)
                .setContent(contentView)
                .setContentIntent(pendingIntent);

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

    private void showNotification(Context context, String title, String body, Intent intent) {

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "default_notification_channel_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.applogo)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
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
