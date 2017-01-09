package com.zyp.shame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.jerry.word.R;

public class WelcomeActivity extends Activity {

	private boolean isFirstIn;
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 10001;
	private static final long SPLASH_DELAY_MILLIS = 1500;
	private static final String SHAREDPREFERRENCES_NAME = "first_pref";
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// Òþ²Ø±êÌâÀ¸
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);// Òþ²Ø×´Ì¬À¸
		setContentView(R.layout.activity_welcome);
		init();
	}

	private void init() {

		// SharedPreferences sp = getSharedPreferences(SHAREDPREFERRENCES_NAME,
		// MODE_PRIVATE);
		// isFirstIn = sp.getBoolean("isFirstIn", true);
		// SharedPreferences.Editor editor = sp.edit();
		// if (isFirstIn) {
		// mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		// editor.putBoolean("isFirstIn", false);
		// editor.commit();
		// } else {
		// mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		//
		// }

		mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
	}

	private void goHome() {
		Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void goGuide() {
		// Intent intent = new Intent(WelcomeActivity.this,
		// GuideActivity.class);
		// startActivity(intent);
		// finish();
	}

}