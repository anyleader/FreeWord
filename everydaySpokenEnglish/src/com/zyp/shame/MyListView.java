package com.zyp.shame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jerry.word.R;

public class MyListView extends ListView {

	public static final int DONE = 1;
	public static final int PULL = 2;
	public static final int RELEASE = 3;
	public static final int REFRESHING = 4;
	private int currentStatu;
	private int downY;
	private View v;
	private int viewHeight;
	private TextView textInfo;
	private ImageView imgArrow;
	private ProgressBar progressBar;

	private OnRefreshListener onRefreshListener;

	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	private Context context;

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		v = LayoutInflater.from(context).inflate(R.layout.list_head, null);
		textInfo = (TextView) v.findViewById(R.id.tv_state);
		imgArrow = (ImageView) v.findViewById(R.id.iv_arrow);
		progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
		addHeaderView(v);
		v.measure(0, 0);
		viewHeight = v.getMeasuredHeight();
		v.setPadding(0, -viewHeight, 0, 0);
		currentStatu = DONE;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		// case MotionEvent.
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			currentStatu = PULL;
			break;
		case MotionEvent.ACTION_MOVE:
			int moveY = (int) ev.getY();
			if (currentStatu == PULL) {
				if (this.getFirstVisiblePosition() == 0) {// ����ֻ��ListView�Ķ��������ã���ֹ�ӵײ��ص������Ĺ����о���Ϊ������ˢ��
					int currentTop = moveY - downY - viewHeight;
					if (currentTop > 40) {
						v.setPadding(0, 40, 0, 0);// ��ֹ��ʱ����������̫��հ�
					} else {
						v.setPadding(0, currentTop, 0, 0);
					}
					if (moveY - downY > viewHeight) {

						textInfo.setText("�ɿ�ˢ��");

						Animation animation = AnimationUtils.loadAnimation(
								context, R.anim.rotate);
						imgArrow.startAnimation(animation);
						currentStatu = RELEASE;
					}
				}

			}
			break;
		case MotionEvent.ACTION_UP:
			if (currentStatu == RELEASE) {
				imgArrow.clearAnimation();
				imgArrow.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				textInfo.setText("����ˢ��");
				if (this.onRefreshListener != null) {
					this.onRefreshListener.onRefresh();
				}
			}else{//����û�е�ˢ�µĳ̶����ֺ�����head��
				v.setPadding(0, -viewHeight, 0, 0);
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public void refreshComplete() {
		imgArrow.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		textInfo.setText("����ˢ��");
		v.setPadding(0, -viewHeight, 0, 0);
	}
}
