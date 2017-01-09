package com.zyp.shame;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jerry.word.R;

public class SingleActivity extends Activity {
	private ListView listView;
	private CommentListAdapter adapter;
	private List<Comment> list = new ArrayList<Comment>();
	private List<Comment> allList = new ArrayList<Comment>();
	private LinearLayout linearLayout;
	private Button backBtn;
	private int width = 0;
	private Shame shame;
	private EditText commentText;
	private Button commentButton;
	private String imei = "";
	private String machine = "";
	private String comment = "";//评论
	InputMethodManager inputMethodManager;//输入法
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				linearLayout.setVisibility(View.GONE);// 隐藏Loading布局
				adapter = new CommentListAdapter(list, SingleActivity.this, shame,
						width);
				listView.setAdapter(adapter);
			}
			if (msg.what == 1) {
				linearLayout.setVisibility(View.GONE);// 隐藏Loading布局
				adapter = new CommentListAdapter(allList, SingleActivity.this, shame,
						width);
				listView.setAdapter(adapter);
			}
		}
	};
	@SuppressLint("HandlerLeak")
	private Handler commentHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 3) {
				Toast.makeText(getApplicationContext(), "网络连接失败",
						Toast.LENGTH_SHORT).show();
			}else if (msg.what == 1) {
				commentText.setText("");
				Toast.makeText(getApplicationContext(), "提交成功，审核一下哈",
						Toast.LENGTH_SHORT).show();
			}else if(msg.what == 0){
				Toast.makeText(getApplicationContext(), "提交失败，可能有特殊字符",
						Toast.LENGTH_SHORT).show();//有特殊表情符号时插入数据库失败
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置无标题
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);//
		// 设置全屏（屏蔽掉，避免评论时弹出输入法后整个页面上移）
		setContentView(R.layout.activity_single);

		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);// (新activity进来的动画，旧activity出去的动画)
			}
		});
		linearLayout = (LinearLayout) findViewById(R.id.loadCommontLayout);
		listView = (ListView) findViewById(R.id.listview_comment);
		shame = (Shame) getIntent().getSerializableExtra("shame");
		WindowManager wm = this.getWindowManager();
		width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = tm.getDeviceId();// IMEI
		machine = android.os.Build.MODEL;// 机型
		machine = machine.replace(" ", "_").replace("#", "").replace("&", "").replace("@", "");
		inputMethodManager =(InputMethodManager)this.getApplicationContext().
				getSystemService(Context.INPUT_METHOD_SERVICE);
		
		commentText = (EditText)findViewById(R.id.text_comment);
		commentButton = (Button)findViewById(R.id.btn_comment);
		commentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commitComment();
			}
		});

		if (isNetworkConnected(this)) {
			new WorkThread().start();
		} else {
			Toast.makeText(getApplicationContext(), "网络不给力", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	public void commitComment(){
		comment = commentText.getText().toString();
		if(comment == null || comment.equals("") || comment.equals(" ")){
			Toast.makeText(getApplicationContext(), "评论不能为空", Toast.LENGTH_SHORT)
			.show();
		}else{
			comment = comment.replace(" ", "_").replace("#", "").replace("&", "").replace("@", "");
			inputMethodManager.hideSoftInputFromWindow(commentText.getWindowToken(), 0);//隐藏输入法
			if (isNetworkConnected(this)) {
				new SubmitThread().start();
			} else {
				Toast.makeText(getApplicationContext(), "网络不给力", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private class WorkThread extends Thread {
		@Override
		public void run() {
			list = ListData.getCommentsByTopicID(shame.getId());// 执行复杂操作（通过网络从服务器获取数据）
			Message msg = new Message();
			Comment c0 = new Comment();
			c0.setComment_author(shame.getAuthor());
			c0.setComment_content(shame.getContent());
			c0.setComment_time(shame.getCreate_time());
			if (list == null) {//如果没有评论，只加载当前Topic作为ListView的唯一一行数据
				list = new ArrayList<Comment>();
				list.add(c0);
				msg.what = 0;
			}else{
				allList.add(c0);
				for (int i = 0; i < list.size(); i++) {
					Comment comment = new Comment();
					comment.setComment_author(list.get(i).getComment_author());
					comment.setComment_content(list.get(i).getComment_content());
					comment.setComment_time(list.get(i).getComment_time());
					allList.add(comment);
				}
				msg.what = 1;
			}
			handler.sendMessage(msg);// 处理完成后给handler发送消息
		}
	}
	
	private class SubmitThread extends Thread {
		@Override
		public void run() {
			String flag = ListData.submitComment(shame.getId(), machine, comment);
			Message msg = new Message();
			if (flag.equals("0")) {
				msg.what = 0;
			} else if(flag.equals("1")){
				msg.what = 1;
			}else if(flag.equals("error")){
				msg.what = 3;
			}
			commentHandler.sendMessage(msg);// 处理完成后给handler发送消息
		}
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {// 按下手机上的返回键
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);// (新activity进来的动画，旧activity出去的动画)
			return true;
		}
		return false;
	}

}
