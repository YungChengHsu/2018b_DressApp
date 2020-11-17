package com.technion.rbd.dressapp.BackEnd;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.technion.rbd.dressapp.MenuActivity;
import com.technion.rbd.dressapp.R;

import java.util.Map;

public class FmsAPI extends FirebaseMessagingService {

    // private Context mContext;

    public FmsAPI() {
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Log.d("API logx", "Handling new message");
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            Integer msg_type = Integer.parseInt(data.get("messageType").toString());
            if (msg_type == 1) {
                final String buyer_name = data.get("buyerName").toString();
                final String item_name = data.get("itemName").toString();

                createNotificationChannelx();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "1337").setSmallIcon(R.mipmap.ic_launcher)
                                .setAutoCancel(true);
                        notificationBuilder.setContentText(String.format("%s just ordered your item: %s.", buyer_name, item_name));
                        notificationBuilder.setContentTitle("New order!");
                        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
                        notificationBuilder.setSmallIcon(R.drawable.all_black_transparent).setAutoCancel(true);
                        NotificationManager mNotifyMgr = (NotificationManager)
                                getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.notify(1, notificationBuilder.build());
                    }
                });
            }
            if (msg_type == 2) {
                final String sender_name = data.get("senderName").toString();
                final String message_text = data.get("messageText").toString();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "1337").setSmallIcon(R.mipmap.ic_launcher)
                                .setAutoCancel(true);
                        notificationBuilder.setContentText(message_text);
                        notificationBuilder.setContentTitle("New message from " + sender_name + ":");
                        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
                        notificationBuilder.setSmallIcon(R.drawable.all_black_transparent).setAutoCancel(true);
                        notificationBuilder.setPriority(NotificationManager.IMPORTANCE_DEFAULT);
//                        notificationBuilder.setAutoCancel(true);
                        NotificationManager mNotifyMgr = (NotificationManager)
                                getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.notify(1, notificationBuilder.build());
                    }
                });
            }
            if (msg_type == 3) {
                final String item_id = data.get("itemId").toString();
                final String item_name = data.get("itemName").toString();
                final String item_cat = data.get("itemCat").toString();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "1337").setSmallIcon(R.mipmap.ic_launcher)
                                .setAutoCancel(true);
                        notificationBuilder.setContentTitle("New item in your area!");
                        notificationBuilder.setContentText("Someone is giving away " + item_name + " (" + item_cat + ").");
                        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
                        notificationBuilder.setSmallIcon(R.drawable.all_black_transparent).setAutoCancel(true);
                        notificationBuilder.setPriority(NotificationManager.IMPORTANCE_DEFAULT);
//                        notificationBuilder.setAutoCancel(true);
                        NotificationManager mNotifyMgr = (NotificationManager)
                                getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.notify(1, notificationBuilder.build());
                    }
                });
            }
        } else {
            //todo: what if paylaod = 0

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "1337").setSmallIcon(R.mipmap.ic_launcher)
                            .setAutoCancel(true);
                    notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
                    notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
                    notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
                    notificationBuilder.setSmallIcon(R.drawable.all_black_transparent).setAutoCancel(true);
                    notificationBuilder.setPriority(NotificationManager.IMPORTANCE_DEFAULT);
//                        notificationBuilder.setAutoCancel(true);
                    NotificationManager mNotifyMgr = (NotificationManager)
                            getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(1, notificationBuilder.build());
                }
            });
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("API logx", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        super.onMessageReceived(remoteMessage);
    }

    private void createNotificationChannelx() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "blablachannel";
            String description = "chatchannel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1337", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
