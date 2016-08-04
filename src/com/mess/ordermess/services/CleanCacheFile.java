package com.mess.ordermess.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;

import com.mess.clientdlnu.R;
import com.mess.ordermess.constant.Constants;
import com.mess.ordermess.utils.FileUtils;

public class CleanCacheFile extends Service {

	private Timer timer;
	private FileUtils fileUtils;
	private long when = 600000;
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		fileUtils = new FileUtils(this);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		int valueItem = sp.getInt("Clear_Cache_Circle", 3);
		long clearCacheTime;
		if (0 == valueItem) {
			clearCacheTime = 15 * 60 * 1000;
		} else if (1 == valueItem) {
			clearCacheTime = 30 * 60 * 1000;
		} else if (2 == valueItem) {
			clearCacheTime = 1 * 60 * 60 * 1000;
		} else {
			clearCacheTime = 24 * 60 * 60 * 1000;
		}
		when = clearCacheTime;
		timer = new Timer(true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (null == timer) {
			timer = new Timer(true);
		}

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				fileUtils.deleteFile();
			}

		}, when);
		// return START_STICKY_COMPATIBILITY;
		// return super.onStartCommand(intent, flags, startId);
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}
}
