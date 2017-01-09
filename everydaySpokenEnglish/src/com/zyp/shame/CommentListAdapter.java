package com.zyp.shame;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerry.word.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class CommentListAdapter extends BaseAdapter {

	private List<Comment> list = new ArrayList<Comment>();
	private Context context;
	private ImageLoadingListener loadListener;
	private Shame shame;
	private int width = 0;

	public CommentListAdapter(List<Comment> list, Context context, Shame shame,
			int width) {
		super();
		this.context = context;
		this.list = list;
		this.shame = shame;
		this.width = width;
	}

	static class ViewHolder {
		TextView comment_author;// 评论者
		TextView comment_content;// 评论内容
		TextView comment_time;// 评论时间
		ImageView img;// 主题图片
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.comment_list_item, null);
			holder.comment_author = (TextView) convertView
					.findViewById(R.id.comment_author);
			holder.comment_content = (TextView) convertView
					.findViewById(R.id.comment);
			holder.comment_time = (TextView) convertView
					.findViewById(R.id.comment_time);
			holder.img = (ImageView) convertView.findViewById(R.id.comment_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.comment_author.setTextSize(15);
			holder.comment_content.setTextSize(18);
			holder.comment_time.setTextSize(15);
			holder.comment_author.setText(shame.getAuthor());
			holder.comment_content.setText(shame.getContent());
			holder.comment_time.setText(shame.getCreate_time());
			holder.img.setVisibility(View.VISIBLE);
			displayImage(holder.img);
		} else {
			holder.comment_author.setTextSize(13);
			holder.comment_content.setTextSize(15);
			holder.comment_time.setTextSize(12);
			holder.comment_author.setText(list.get(position)
					.getComment_author());
			holder.comment_content.setText(list.get(position)
					.getComment_content());
			holder.comment_time.setText(list.get(position).getComment_time());
			holder.img.setVisibility(View.GONE);
		}

		return convertView;
	}

	public void displayImage(ImageView img) {
		String url = shame.getImage_url();
		// ImageLoader方式异步加载网络图片（ImageLoader会自动从第一次加载的缓存中获取，无需再次从网络加载）
		if (!"".equals(url) && url != null) {
			img.setAdjustViewBounds(true);// 保持图片比例
			img.setMaxWidth(width);// 图片比例最大宽度为屏幕宽度
			loadListener = new ImageLoadingListener() {
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO 自动生成的方法存根

				}

				@Override
				public void onLoadingComplete(String arg0, View arg1,
						Bitmap httpBitmap) {
					// TODO 自动生成的方法存根

				}

				@Override
				public void onLoadingFailed(String arg0, View arg1,
						FailReason arg2) {
					// TODO 自动生成的方法存根

				}

				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO 自动生成的方法存根

				}
			};
			try {
				ImageLoader.getInstance().displayImage(url, img, loadListener);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
