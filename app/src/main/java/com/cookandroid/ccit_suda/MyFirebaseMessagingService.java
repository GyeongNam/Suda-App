package com.cookandroid.ccit_suda;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 *
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 *
 * <intent-filter>
 *   <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("Firebase", "FirebaseInstanceIDService : " + s);
    }


    @Override
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // 메시지 수신 시 실행되는 메소드
//        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
//            sendNotification(remoteMessage);
//        }
//    }


    /**
     * 메시지가 수신되었을 때 실행되는 메소드
     * **/
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        if(key == "1"){
//            nointent = new Intent(this,PostdetailActivity.class);
//            nointent.putExtra("primarykey",key);
//        }
//        else if(key == "2"){
//            nointent = new Intent(this, chatting.class);
//            nointent.putExtra("room",key);
//        }
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("body");
        String key = remoteMessage.getData().get("key");
        String num = remoteMessage.getData().get("num");
        Log.d("뭐냐 너", "onMessageReceived: "+ num + " // " + key);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("test", test);

     //   PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(key.equals("2") ){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                String channel = "ccit";
                String channel_nm = "suda";


                NotificationManager notichannel = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channelMessage.setDescription("채널에 대한 설명.");
                channelMessage.enableLights(true);
                channelMessage.enableVibration(true);
                channelMessage.setShowBadge(false);
                channelMessage.setVibrationPattern(new long[]{1000, 1000});
                notichannel.createNotificationChannel(channelMessage);
                Intent nointent = new Intent(this, boardActivity.class);
                nointent.setAction(Intent.ACTION_MAIN);
                nointent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                nointent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


//                if(isAppRunning(((boardActivity)boardActivity.context_board))){
//                    nointent = new Intent(this, chatting.class);
//                }
                nointent.putExtra("room",num);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, nointent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


                //푸시알림을 Builder를 이용하여 만듭니다.
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channel)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle(title)//푸시알림의 제목
                                .setContentText(message)//푸시알림의 내용
                                .setChannelId(channel)
                                .setAutoCancel(true)//선택시 자동으로 삭제되도록 설정.
                                .setContentIntent(pendingIntent)//알림을 눌렀을때 실행할 인텐트 설정.
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(9999, notificationBuilder.build());

            } else {
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, "ccit")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setAutoCancel(true)
             //                   .setContentIntent(pendingIntent)
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(9999, notificationBuilder.build());

            }
        }
         else if(key.equals("1")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                String channel = "ccit";
                String channel_nm = "suda";


                NotificationManager notichannel = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channelMessage.setDescription("채널에 대한 설명.");
                channelMessage.enableLights(true);
                channelMessage.enableVibration(true);
                channelMessage.setShowBadge(false);
                channelMessage.setVibrationPattern(new long[]{1000, 1000});
                notichannel.createNotificationChannel(channelMessage);

                Intent nointent = new Intent(this, PostdetailActivity.class);
                nointent.putExtra("primarykey",num);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, nointent,
                        PendingIntent.FLAG_ONE_SHOT);


                //푸시알림을 Builder를 이용하여 만듭니다.
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channel)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle(title)//푸시알림의 제목
                                .setContentText(message)//푸시알림의 내용
                                .setChannelId(channel)
                                .setAutoCancel(true)//선택시 자동으로 삭제되도록 설정.
                                .setContentIntent(pendingIntent)//알림을 눌렀을때 실행할 인텐트 설정.
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(9999, notificationBuilder.build());

            } else {
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, "ccit")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setAutoCancel(true)
                                //                   .setContentIntent(pendingIntent)
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(9999, notificationBuilder.build());

            }
         }
    }
}
