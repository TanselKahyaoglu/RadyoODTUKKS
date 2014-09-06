package com.tanss.radyoodtukks;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;

public class RadioService extends Service {
	private MediaPlayer player;
	String yayin = "http://144.122.152.54:8000/;listen.pls";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	   @Override
	    public void onDestroy() {
	        super.onDestroy();
	        Log.d("onDestroy", "onDestroy");
	        stopPlaying();
	        if (player != null) {
	            if (player.isPlaying()) {
	                player.stop();
	            }
	            player.release();
	        }
	    }
	@Override
	public void onStart(Intent intent, int startId) {
		Log.i("onStart", "Service onStart");
		initializeMediaPlayer();
		startPlaying();
		}
	private void initializeMediaPlayer() {
		Log.i("initPlayer", "initPlayer");
		player = new MediaPlayer();
		try {
			player.setDataSource(yayin);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				Log.i("Buffering", "" + percent);
			}
		});
	}
	private void startPlaying() {
		Log.i("startPlaying", "startPlaying");
		player.prepareAsync();
		player.setOnPreparedListener(new OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
		player.start();
        player.isLooping();
			}
		});
	}
	private void stopPlaying() {
		Log.i("stopPlaying", "stopPlaying");
			player.stop();
	        player.release();
			initializeMediaPlayer();
	}

}