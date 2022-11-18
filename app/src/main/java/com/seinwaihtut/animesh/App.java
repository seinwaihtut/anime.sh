package com.seinwaihtut.animesh;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_DOWNLOADED_ID = "channel_downloaded";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }
    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel download_channel = new NotificationChannel(
                    CHANNEL_DOWNLOADED_ID,
                    "Downloaded",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            download_channel.setDescription("Notify the user after a torrent file has finished downloading.");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(download_channel);
        }
    }
}
