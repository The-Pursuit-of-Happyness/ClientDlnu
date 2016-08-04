package com.mess.ordermess;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mess.clientdlnu.R;
import com.mess.ordermess.utils.GetAPPMessage;

public class SplashActivity extends Activity {

	private RelativeLayout splash_rel;
	private TextView tv_splash_version;
	private long durationMillis = 1500;
	private long startTime = 2000;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		
		setContentView(R.layout.aplashp_activity);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isLogin", false);
		editor.commit();

		this.splash_rel = (RelativeLayout) this.findViewById(R.id.splash_rel);
		this.tv_splash_version = (TextView) this.findViewById(R.id.tv_splash_version);

		this.tv_splash_version.setText("版本 " + GetAPPMessage.getVersion(this));

		AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);
		animation.setDuration(durationMillis);
		this.splash_rel.startAnimation(animation);

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				enterHome();
			}
		}, startTime);
	}

	/**
	 * 进入程序主界面
	 */
	protected void enterHome() {
		Intent intent = new Intent(SplashActivity.this,OrderMessMainActivity.class);
		startActivity(intent);
		SplashActivity.this.finish();
	}

	/**
	 * 进行界面的刷新
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

}
