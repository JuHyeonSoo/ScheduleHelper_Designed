package com.sbc.sk.schedulehelper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Administrator on 2017-07-26.
 */

public class BroadcastD extends BroadcastReceiver {

    //알람 시간이 되었을때 onReceive를 호출함
    @Override
    public void onReceive(Context context, Intent intent) {

            String link = intent.getStringExtra("key_http");
            int intent_number= intent.getIntExtra("key_intent_num",-10);
            int intent_code= intent.getIntExtra("key_code",-10);

            String text="Check this Web site periodically :) "+(intent_code+1)+"번쨰 입력한 사이트입니다.";

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(context)
                                .setAutoCancel(true)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(text);
                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                // pending implicit intent to view url
                Intent resultIntent = new Intent(Intent.ACTION_VIEW);
                resultIntent.setData(Uri.parse(link));
                PendingIntent pending = PendingIntent.getActivity(context, (intent_number | (intent_number + 1) | (intent_number + 2)), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.setContentIntent(pending);
                // using the same tag and Id causes the new notification to replace an existing one
                mNotificationManager.notify(String.valueOf(System.currentTimeMillis()), (intent_number | (intent_number + 1) | (intent_number + 2)), notificationBuilder.build());

        }
    }
