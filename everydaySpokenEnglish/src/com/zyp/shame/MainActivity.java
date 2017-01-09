package com.zyp.shame;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jerry.word.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.zyp.shame.MyListView.OnRefreshListener;

public class MainActivity extends Activity {
	private MyListView listview;
	private List<Shame> list;
	private ListAdapter adapter;
	private LinearLayout linearLayout;
	private int width;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				Toast.makeText(getApplicationContext(), "网络连接失败",
						Toast.LENGTH_SHORT).show();
			}
			if (msg.what == 1) {
				linearLayout.setVisibility(View.GONE);// 隐藏Loading布局
				adapter = new ListAdapter(list, MainActivity.this, width);// 更新UI
				listview.setAdapter(adapter);
				listview.setOnRefreshListener(new MyListListener());
			}
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置无标题
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setContentView(R.layout.activity_main);

		// 腾讯Bugly
		CrashReport
				.initCrashReport(getApplicationContext(), "900022809", false);
		// 腾讯信鸽推送
		Context context = getApplicationContext();
		XGPushManager.registerPush(context);

		linearLayout = (LinearLayout) findViewById(R.id.loadLayout);
		listview = (MyListView) findViewById(R.id.listview);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				Shame shame = list.get(position - 1);// 加上下拉刷新后点击错位，这里减1
				Intent intent = new Intent(MainActivity.this,
						SingleActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("shame", shame);
				intent.putExtras(bundle);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			}

		});

		WindowManager wm = this.getWindowManager();
		width = wm.getDefaultDisplay().getWidth();// 屏幕宽度

		initImageLoader();

		if (isNetworkConnected(this)) {
			new WorkThread().start();
		} else {
			Toast.makeText(getApplicationContext(), "网络不给力", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private class WorkThread extends Thread {
		@Override
		public void run() {
			list = ListData.getTopics();// 执行复杂操作（通过网络从服务器获取数据）
			Message msg = new Message();
			if (list == null) {
				msg.what = 0;
			} else {
				msg.what = 1;
			}
			handler.sendMessage(msg);// 处理完成后给handler发送消息
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {// 如果在此Activity下按下手机上的返回键，则退出程序
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			QuitApp();
			return true;
		}
		return false;
	}

	public void QuitApp() {// 退出程序
		new AlertDialog.Builder(MainActivity.this)
				.setMessage("确定退出?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
						android.os.Process.killProcess(android.os.Process
								.myPid());
						System.exit(0);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();
	}

	public void initImageLoader() {// 异步加载图片
		// 配置option
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_img)
				// 默认显示图片
				.showImageForEmptyUri(R.drawable.default_empty_img)
				.cacheInMemory()// 允许内存缓存
				.cacheOnDisc()// 允许硬盘缓存
				.build();

		// 配置config
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).defaultDisplayImageOptions(defaultOptions)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).build();

		ImageLoader.getInstance().init(config);
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	class MyListListener implements OnRefreshListener {
		@Override
		public void onRefresh() {

			new Thread() {
				public void run() {
					try {
						// 联网
						Thread.sleep(2000);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (isNetworkConnected(MainActivity.this)) {
									list = ListData.getTopics();// 执行复杂操作（通过网络从服务器获取数据）
									Message msg = new Message();
									if (list == null) {
										msg.what = 0;
									} else {
										msg.what = 1;
									}
									handler.sendMessage(msg);// 处理完成后给handler发送消息

									listview.refreshComplete();
								} else {
									Toast.makeText(getApplicationContext(),
											"网络不给力", Toast.LENGTH_SHORT).show();
									listview.refreshComplete();
								}
							}
						});
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				};
			}.start();
		}

	}
}
