package com.example.demoplivo;

import static com.example.demoplivo.Constants.NOTIFICATION_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.plivo.endpoint.Incoming;
import com.plivo.endpoint.Outgoing;

import java.util.HashMap;

public class CallForegroundService extends Service implements CallListener {

    final private String TAG = getClass().getName();
    public static CallForegroundService instance;

    CallStateChangeListener callStatusListener;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        instance = this;

        registerBackendListener();
        return START_STICKY;
    }

    public void setCallStatusListener(CallStateChangeListener listener) {
        callStatusListener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        Log.i(TAG, "startMyOwnForeground: ");

        String channelName = "Call Service";

        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        /*PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, new Intent(this, MainActivity.class),
                PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle(getString(R.string.app_is_running))
                .setContentText(getString(R.string.checking_selected_apps))
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(1, notification);*/

        showCallNotification();
    }

    private void showCallNotification() {
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, new Intent(this, MainActivity.class),
                PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        Notification notification = notificationBuilder
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_is_running))
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .addAction(0, "Speaker", contentIntent)
                .addAction(0, "Mute", contentIntent)
                .addAction(0, "Mute", contentIntent)
                .build();

        startForeground(1, notification);
    }

    private void registerBackendListener() {
        ((App) getApplication()).backend().setBackendListener(this);
    }

    @Override
    public void onIncomingCall(Incoming data, CallState callState) {

    }

    @Override
    public void onOutgoingCall(Outgoing data, CallState callState) {
        Log.d(TAG, "Service onOutgoingCall Done: " + callState);
//        sendCallStateToActivity(callState, data);
        callStatusListener.onCallStateChanged(callState, data);
    }

    @Override
    public void onIncomingDigit(String digit) {

    }

    @Override
    public void mediaMetrics(HashMap messageTemplate) {

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e(TAG, "onTaskRemoved: ");
    }

    /*private void sendCallStateToActivity(CallState callState, Outgoing data) {
        Intent intent = new Intent("currentCall");
        // You can also include some extra data.
        intent.putExtra("callState", callState);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }*/
}
