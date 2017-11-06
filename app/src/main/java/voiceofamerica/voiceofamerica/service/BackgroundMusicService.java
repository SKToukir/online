package voiceofamerica.voiceofamerica.service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import voiceofamerica.voiceofamerica.MainActivity;

import java.io.IOException;

/**
 * Created by mwongela on 5/13/16.
 */
public class BackgroundMusicService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {


    private MediaPlayer mediaPlayer;

    private static final int NOTIFICATION = 1;
    @Nullable
    private NotificationManager mNotificationManager = null;
    private final NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this);

    private Context ctx;
    private String RadioName;
    private String RadioURL;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate(){
        super.onCreate();

        SharedPreferences settings = getSharedPreferences(getString(voiceofamerica.voiceofamerica.R.string.prefs), 0);
        RadioURL  = settings.getString(getString(voiceofamerica.voiceofamerica.R.string.prefs_radio_url), null);
        RadioName  = settings.getString(getString(voiceofamerica.voiceofamerica.R.string.prefs_radio_name), null);

        setupNotifications();
        showBufferNotification(RadioName);

        ctx = this;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(RadioURL);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        cancelNotification(ctx, NOTIFICATION);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if(!mediaPlayer.isPlaying()){
            showNotification(RadioName);
            mediaPlayer.start();
        }
    }

    private void setupNotifications() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);
        mNotificationBuilder
                .setSmallIcon(voiceofamerica.voiceofamerica.R.mipmap.ic_launcher)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(getText(voiceofamerica.voiceofamerica.R.string.app_name))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setOngoing(true);
    }

    private void showNotification(String RadioName) {
        mNotificationBuilder
                .setTicker(getString(voiceofamerica.voiceofamerica.R.string.ticker_now_playing, RadioName))
                .setSmallIcon(voiceofamerica.voiceofamerica.R.mipmap.not_icon)
                .setContentText(getString(voiceofamerica.voiceofamerica.R.string.playing_radio_name, RadioName));
        if (mNotificationManager != null) {
            mNotificationManager.notify(NOTIFICATION, mNotificationBuilder.build());
        }
    }

    private void showBufferNotification(String RadioName) {
        mNotificationBuilder
                .setTicker(getString(voiceofamerica.voiceofamerica.R.string.radio_buffering, RadioName))
                .setSmallIcon(voiceofamerica.voiceofamerica.R.mipmap.not_icon)
                .setContentText(getString(voiceofamerica.voiceofamerica.R.string.radio_buffering, RadioName));
        if (mNotificationManager != null) {
            mNotificationManager.notify(NOTIFICATION, mNotificationBuilder.build());
        }
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }
}