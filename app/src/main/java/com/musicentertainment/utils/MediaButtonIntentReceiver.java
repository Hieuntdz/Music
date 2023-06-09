package com.musicentertainment.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.musicentertainment.countrymusic.PlayerService;

public class MediaButtonIntentReceiver extends BroadcastReceiver {

    public MediaButtonIntentReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String intentAction = intent.getAction();
            if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
                return;
            }
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null) {
                return;
            }
            int action = event.getAction();
            if (action == KeyEvent.ACTION_DOWN) {
                // do something
                if (PlayerService.getInstance() != null) {
                    Intent intent_pause = new Intent(context, PlayerService.class);
                    intent_pause.setAction(PlayerService.ACTION_TOGGLE);
                    context.startService(intent_pause);
                }
            }
            abortBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}