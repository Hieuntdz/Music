package com.musicentertainment.utils;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.musicentertainment.countrymusic.BuildConfig;
import com.musicentertainment.countrymusic.R;
import com.musicentertainment.countrymusic.SplashActivity;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


public class NotificationExtenderExample implements OneSignal.OSRemoteNotificationReceivedHandler {

    public static final int NOTIFICATION_ID = 1;
    String title, message, bigpicture, sname, url;

    private void sendNotification(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent;
        if (!Constant.pushSID.equals("0") || !Constant.pushCID.equals("0") || !Constant.pushArtID.equals("0") || !Constant.pushAlbID.equals("0")) {
            intent = new Intent(context, SplashActivity.class);
            intent.putExtra("ispushnoti", true);
        } else if(!url.equals("false") && !url.trim().isEmpty()){
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
        } else {
            intent = new Intent(context, SplashActivity.class);
        }

        NotificationChannel mChannel;
        String NOTIFICATION_CHANNEL_ID = "onlinesong_push";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Onlinesong Channel";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSound(uri)
                .setAutoCancel(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setLights(Color.RED, 800, 800)
                .setContentText(message);

        mBuilder.setSmallIcon(getNotificationIcon(mBuilder));

        mBuilder.setContentTitle(title);
        mBuilder.setTicker(message);

        if (bigpicture != null) {
            mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(bigpicture)).setSummaryText(message));
        } else {
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        }

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getColour());
            return R.drawable.ic_notification;
        } else {
            return R.mipmap.app_icon;
        }
    }

    private int getColour() {
        return 0xee2c7a;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            InputStream input;
            if(BuildConfig.SERVER_URL.contains("https://")) {
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            } else {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            }
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent receivedResult) {
        OSNotification payload = receivedResult.getNotification();
        title = payload.getTitle();
        message = payload.getBody();
        bigpicture = payload.getBigPicture();

        try {
            Constant.pushCID = payload.getAdditionalData().getString("cat_id");
            Constant.pushCName = payload.getAdditionalData().getString("cat_name");

            if(payload.getAdditionalData().has("artist_id")) {
                Constant.pushArtID = payload.getAdditionalData().getString("artist_id");
                Constant.pushArtNAME = payload.getAdditionalData().getString("artist_name");
            }

            if(payload.getAdditionalData().has("album_id")) {
                Constant.pushAlbID = payload.getAdditionalData().getString("album_id");
                Constant.pushAlbNAME = payload.getAdditionalData().getString("album_name");
            }

            if(payload.getAdditionalData().has("song_id")) {
                Constant.pushSID = payload.getAdditionalData().getString("song_id");
                sname = payload.getAdditionalData().getString("song_name");
            }
            url = payload.getAdditionalData().getString("external_link");
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendNotification(context);
    }
}