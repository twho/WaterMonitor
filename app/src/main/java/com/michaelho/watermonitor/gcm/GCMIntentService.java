package com.michaelho.watermonitor.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.michaelho.watermonitor.MainActivity;
import com.michaelho.watermonitor.R;
import com.michaelho.watermonitor.constants.GCMConfig;
import com.michaelho.watermonitor.objects.SingleMessage;
import com.michaelho.watermonitor.tools.sqlOpenHelperMessage;
import com.michaelho.watermonitor.util.TimeUtilities;

/**
 * Created by MichaelHo on 2015/5/11.
 */
public class GCMIntentService extends IntentService implements GCMConfig {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String TAG = "GCMIntentService";
    private sqlOpenHelperMessage sqlMessage;
    private static Context context;

    public GCMIntentService()
    {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        //解析收到的文字訊息再傳入sendNotification進行顯示
        Bundle bundle = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!bundle.isEmpty())
        {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                sendNotification(bundle);
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
            {
                sendNotification(bundle);
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                for (int i = 0; i < 5; i++)
                {
                    Log.i(TAG, "Working... " + (i + 1) + "/5 @ " + SystemClock.elapsedRealtime());

                    try
                    {
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                sendNotification(bundle);
                Log.i(TAG, "Received: " + bundle.toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    //接收到的MESSAGE格式為"WA標題WA內容WA寄送者"
    //設定Notification要顯示的資訊與點擊Notification要開啟的頁面
    private void sendNotification(Bundle bundle)
    {
        context = getApplicationContext();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String msg = bundle.getString("message");
        Intent intent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();
        b.putString("MESSAGE", msg);
        intent.putExtras(b);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        String title = msg.split("WA")[1];
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(largeIcon)
                        .setContentTitle("Water Monitor")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(title);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        if(msg.substring(0, 2).equalsIgnoreCase("WA")){
            String detail = msg.split("WA")[2];
            String sender = msg.split("WA")[3];
            String ifRead = "0";
            SingleMessage singleMessage = new SingleMessage(title, detail, sender, TimeUtilities.getTimeyyyy_MM_dd_HH_mm(),ifRead);
            sqlMessage = new sqlOpenHelperMessage(this);
            sqlMessage.insertDB(singleMessage);
        }
    }
}
