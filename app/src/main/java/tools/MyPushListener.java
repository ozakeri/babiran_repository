package tools;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import Handlers.DatabaseHandler;
import co.ronash.pushe.PusheListenerService;

public class MyPushListener extends PusheListenerService {
    private NotificationManager mNotificationManager;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;
    private DatabaseHandler db;
    private GlobalValues globalValues = new GlobalValues();
    private String type, pro_id, cat_id, title, body, blog_id, success, factorCode,user, pro_image = null;

    @Override
    public void onMessageReceived(final JSONObject message, final JSONObject content) {

        System.out.println("message====" + message);
        System.out.println("content====" + content);

        try {
            JSONObject contentJson = new JSONObject(String.valueOf(content));
            if (!contentJson.isNull("content")) {

                String jsonObjectStr = contentJson.getString("content");

                if (!jsonObjectStr.isEmpty()) {

                    if (jsonObjectStr.contains("شارژ") || jsonObjectStr.contains("شارژ شگفت انگیز") || jsonObjectStr.contains("شارژ هدیه")){

                        System.out.println("====شارژ======");
                        SharedPreferences.Editor editor = AppController.getInstance().getSharedPreferences().edit();
                        editor.putString("sharjNotify", "sharjNotify");
                        editor.apply();
                    }

                    try {
                        JSONObject contentJSONObject = new JSONObject(jsonObjectStr);
                        if (!contentJSONObject.isNull("factorcode") && !contentJSONObject.isNull("type")) {
                            String factorCode = contentJSONObject.getString("factorcode");
                            String type = contentJSONObject.getString("type");

                            if (type.equals("neworder")) {
                                body = " تامین کننده گرامی سفارش با کد " + factorCode + " برای شما ثبت شده است ";
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                showNotificationCopy(getApplicationContext(), intent, body);

                            }

                            return;
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    jsonObjectStr = jsonObjectStr.replaceAll("\"", "");
                    //jsonObjectStr = jsonObjectStr.replaceAll("=", ":");


                    String[] kvPairs = jsonObjectStr.split(",");
                    for (String kvPair : kvPairs) {
                        try {
                            String[] kv = kvPair.split("=");
                            String key = kv[0];
                            String value = kv[1];
                            key = key.replaceAll("([{-}])", "");
                            value = value.replaceAll("([{-}])", "");

                            key = key.replaceAll("([{-}])", "");
                            value = value.replaceAll("([{-}])", "");

                            if (key.equals(" type")) {
                                key = key.replace(" type", "type");
                            }

                            if (key.equals(" success")) {
                                key = key.replace(" success", "success");
                            }

                            if (key.equals(" blog_id")) {
                                key = key.replace(" blog_id", "blog_id");
                            }
                            if (key.equals(" pro_id")) {
                                key = key.replace(" pro_id", "pro_id");
                            }

                            if (key.equals(" cat_id")) {
                                key = key.replace(" cat_id", "cat_id");
                            }

                            if (key.equals(" pro_image")) {
                                key = key.replace(" pro_image", "pro_image");
                            }

                            if (key.equals(" title")) {
                                key = key.replace(" title", "title");
                            }
                            if (key.equals(" body")) {
                                key = key.replace(" body", "body");
                            }
                            if (key.equals(" user")) {
                                key = key.replace(" user", "user");
                            }

                            if (key.equals("type")) {
                                type = value;
                            }

                            if (key.equals("pro_id")) {
                                pro_id = value;
                            }

                            if (key.equals("success")) {
                                success = value;
                            }

                            if (key.equals("blog_id")) {
                                blog_id = value;
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

                            if (key.equals("user")) {
                                user = value;
                            }


                            if (key.equals("pro_image")) {
                                pro_image = value;
                            }

                            if (type != null) {
                                if (type.equals("product")) {
                                    if (pro_id != null && cat_id != null) {
                                        editor = AppController.getInstance().getSharedPreferences().edit();
                                        editor.putBoolean("getProduct", true);
                                        editor.apply();

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("pro_id", pro_id);
                                        intent.putExtra("cat_id", cat_id);
                                        intent.putExtra("pro_image", pro_image);
                                        // showNotification(getApplicationContext(), intent, titleStr, bodyStr);
                                        showNotification(getApplicationContext(), intent, title, body);
                                    }
                                } else if (type.equals("blog")) {
                                    if (blog_id != null) {

                                        ListtoListActivity.ID_ME = blog_id;
                                        Intent intent = new Intent(getApplicationContext(), ShowRssActivity.class);
                                        intent.putExtra("isPush", true);
                                        intent.putExtra("push_blog_id", blog_id);
                                        showNotificationBlog(getApplicationContext(), intent, title, body);
                                    }
                                }else if (type.equals("user")) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    showNotification(getApplicationContext(), intent, title, body);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    showNotification(getApplicationContext(), intent, title, body);
                                }
                            }


                        } catch (Exception e) {
                            e.getMessage();
                        }
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


        if (pro_image != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(pro_image).getContent());
                contentView.setImageViewBitmap(R.id.image, bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            contentView.setImageViewResource(R.id.image, R.drawable.applogo);
        }
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

    public void showNotificationBlog(Context context, Intent intent, String title, String body) {

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

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push_blog);


        if (pro_image != null) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(pro_image).getContent());
                contentView.setImageViewBitmap(R.id.image, bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            contentView.setImageViewResource(R.id.image, R.drawable.applogo);
        }
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

    public void showNotificationCopy(Context context, Intent intent, String body) {

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

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push_copy);
        contentView.setImageViewResource(R.id.image, R.drawable.applogo);
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

    public void showNotificationCopy(Context context, String title, String body) {

        Intent intent = new Intent();
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
        contentView.setTextViewText(R.id.title, "پرداخت با موفقیت انجام شد");
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
