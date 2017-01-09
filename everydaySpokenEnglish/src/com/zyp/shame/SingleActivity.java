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
	private String comment = "";//����
	InputMethodManager inputMethodManager;//���뷨
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				linearLayout.setVisibility(View.GONE);// ����Loading����
				adapter = new CommentListAdapter(list, SingleActivity.this, shame,
						width);
				listView.setAdapter(adapter);
			}
			if (msg.what == 1) {
				linearLayout.setVisibility(View.GONE);// ����Loading����
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
				Toast.makeText(getApplicationContext(), "��������ʧ��",
						Toast.LENGTH_SHORT).show();
			}else if (msg.what == 1) {
				commentText.setText("");
				Toast.makeText(getApplicationContext(), "�ύ�ɹ������һ�¹�",
						Toast.LENGTH_SHORT).show();
			}else if(msg.what == 0){
				Toast.makeText(getApplicationContext(), "�ύʧ�ܣ������������ַ�",
						Toast.LENGTH_SHORT).show();//������������ʱ�������ݿ�ʧ��
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// �����ޱ���
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);//
		// ����ȫ�������ε�����������ʱ�������뷨������ҳ�����ƣ�
		setContentView(R.layout.activity_single);

		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);// (��activity�����Ķ�������activity��ȥ�Ķ���)
			}
		});
		linearLayout = (LinearLayout) findViewById(R.id.loadCommontLayout);
		listView = (ListView) findViewById(R.id.listview_comment);
		shame = (Shame) getIntent().getSerializableExtra("shame");
		WindowManager wm = this.getWindowManager();
		width = wm.getDefaultDisplay().getWidth();// ��Ļ���
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = tm.getDeviceId();// IMEI
		machine = android.os.Build.MODEL;// ����
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
			Toast.makeText(getApplicationContext(), "���粻����", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	public void commitComment(){
		comment = commentText.getText().toString();
		if(comment == null || comment.equals("") || comment.equals(" ")){
			Toast.makeText(getApplicationContext(), "���۲���Ϊ��", Toast.LENGTH_SHORT)
			.show();
		}else{
			comment = comment.replace(" ", "_").replace("#", "").replace("&", "").replace("@", "");
			inputMethodManager.hideSoftInputFromWindow(commentText.getWindowToken(), 0);//�������뷨
			if (isNetworkConnected(this)) {
				new SubmitThread().start();
			} else {
				Toast.makeText(getApplicationContext(), "���粻����", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private class WorkThread extends Thread {
		@Override
		public void run() {
			list = ListData.getCommentsByTopicID(shame.getId());// ִ�и��Ӳ�����ͨ������ӷ�������ȡ���ݣ�
			Message msg = new Message();
			Comment c0 = new Comment();
			c0.setComment_author(shame.getAuthor());
			c0.setComment_content(shame.getContent());
			c0.setComment_time(shame.getCreate_time());
			if (list == null) {//���û�����ۣ�ֻ���ص�ǰTopic��ΪListView��Ψһһ������
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
			handler.sendMessage(msg);// ������ɺ��handler������Ϣ
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
			commentHandler.sendMessage(msg);// ������ɺ��handler������Ϣ
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {// �����ֻ��ϵķ��ؼ�
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);// (��activity�����Ķ�������activity��ȥ�Ķ���)
			return true;
		}
		return false;
	}

}
