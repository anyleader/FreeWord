package com.zyp.shame;

import java.util.ArrayList;
import java.util.List;

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

public class ListAdapter extends BaseAdapter {
	private List<Shame> list = new ArrayList<Shame>();
	private Context c;
	private int width;

	private ImageLoadingListener loadListener;
	private String imageURL = "";
	private Bitmap bitmap = null;

	public ListAdapter(List<Shame> list, Context c, int width) {
		super();
		this.list = list;
		this.c = c;
		this.width = width;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(c).inflate(R.layout.list_item,
					null);
			holder.author = (TextView) convertView.findViewById(R.id.author);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.create_time = (TextView) convertView
					.findViewById(R.id.create_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String content = list.get(position).getContent();
		if(content.length() > 60){
			content = content.substring(0, 60) + "...";
		}
		holder.author.setText(list.get(position).getAuthor());
		holder.content.setText(content);
		holder.create_time.setText(list.get(position).getCreate_time());
		String url = list.get(position).getImage_url();

		// HttpURLConnection方式获取网络图片
		// if (!"".equals(url) && url != null) {
		// try {
		//
		// byte[] data = WebService.getImage(url); // 得到图片的输入流
		//
		// // 二进制数据生成位图
		// Bitmap bit = BitmapFactory
		// .decodeByteArray(data, 0, data.length);
		//
		// holder.img.setVisibility(View.VISIBLE);
		// holder.img.setAdjustViewBounds(true);
		// holder.img.setMaxWidth(width);
		// holder.img.setImageBitmap(bit);
		//
		// } catch (Exception e) {
		// Log.e("NetActivity", e.toString());
		// }
		// }else{
		// holder.img.setVisibility(View.GONE);
		// }

		// ImageLoader方式异步加载网络图片
		final ImageView iv = holder.img;
		if (!"".equals(url) && url != null) {
			holder.img.setVisibility(View.VISIBLE);
			holder.img.setAdjustViewBounds(true);//保持图片比例
			holder.img.setMaxWidth(width);//图片比例最大宽度为屏幕宽度
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
				ImageLoader.getInstance().displayImage(url, holder.img,
						loadListener);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			holder.img.setVisibility(View.GONE);
		}

		return convertView;
	}

	static class ViewHolder {
		TextView author;// 作者
		TextView content;// 内容
		ImageView img;// 图片
		TextView create_time;// 创建时间
	}

}
