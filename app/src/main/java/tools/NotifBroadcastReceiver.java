package tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotifBroadcastReceiver extends BroadcastReceiver {

    @Override

    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("YOUR_PKG_NAME.pusheco.NOTIF_CLICKED")) {
            Log.i("Pushe==111==", "Broadcast YOUR_PKG_NAME.pusheco.NOTIF_CLICKED received");
            //add your logic here
        } else if (intent.getAction().equals("YOUR_PKG_NAME.NOTIF_DISMISSED")) {
            Log.i("Pushe==222==", "Broadcast YOUR_PKG_NAME.NOTIF_DISMISSED received");
            //add your logic here
        } else if (intent.getAction().equals("YOUR_PKG_NAME.pusheco.NOTIF_BTN_CLICKED")) {
            String btnId = intent.getStringExtra("pushe_notif_btn_id");
            Log.i("Pushe==333==", "Broadcast YOUR_PKG_NAME.pusheco.NOTIF_BTN_CLICKED received. BtnId = " + btnId);
            //add your logic here
        }
    }
}