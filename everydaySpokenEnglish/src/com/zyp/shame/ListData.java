package com.zyp.shame;

import java.util.ArrayList;
import java.util.List;

public class ListData {
	public static List<Shame> getTopics() {// 获取话题列表
		String url = HttpUtil.BASE_URL + "/ListShameServlet";
		String shameStr = HttpUtil.queryStringForPost(url);// 耗时操作
		if (shameStr == null || shameStr.equals("网络异常")) {
			return null;
		}
		String[] shames = shameStr.split("@");// 必须为2条或以上数据，否则没有@号会报错
		List<Shame> list = new ArrayList<Shame>();
		for (int i = 0; i < shames.length; i++) {
			String[] singleShame = shames[i].split("#");
			Shame shame = new Shame();
			shame.setId(singleShame[0]);
			shame.setContent(singleShame[1]);
			shame.setImage_url(singleShame[2]);
			shame.setAuthor(singleShame[3]);
			shame.setCreate_time(singleShame[4]);
			shame.setFlag(singleShame[5]);
			list.add(shame);
		}
		return list;
	}

	public static List<Comment> getCommentsByTopicID(String topicID) {// 根据话题ID获取评论列表
		String url = HttpUtil.BASE_URL + "/ListCommentServlet?id=" + topicID;
		String commentStr = HttpUtil.queryStringForPost(url);// 耗时操作
		if (commentStr == null || commentStr.equals("网络异常")) {
			return null;
		}
		String[] comments = commentStr.split("@");// 必须为2条或以上数据，否则没有@号会报错
		List<Comment> list = new ArrayList<Comment>();
		for (int i = 0; i < comments.length; i++) {
			String[] singleComment = comments[i].split("#");
			Comment comment = new Comment();
			comment.setId(singleComment[0]);
			comment.setTopic_id(singleComment[1]);
			comment.setComment_author(singleComment[2]);
			comment.setComment_content(singleComment[3]);
			comment.setComment_time(singleComment[4]);
			comment.setFlag(singleComment[5]);
			list.add(comment);
		}
		return list;
	}
	
	public static String submitComment(String topicID, String author, String comment) {// 根据话题ID提交评论
		String url = HttpUtil.BASE_URL + "/AddCommentServlet?topicID="+topicID+"&author="+author+"&comment="+comment;
		String commentStr = HttpUtil.queryStringForPost(url);// 耗时操作
		if (commentStr == null || commentStr.equals("") || commentStr.equals("网络异常")) {
			commentStr = "error";
		}
		return commentStr;
	}
}
