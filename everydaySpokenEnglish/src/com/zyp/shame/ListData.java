package com.zyp.shame;

import java.util.ArrayList;
import java.util.List;

public class ListData {
	public static List<Shame> getTopics() {// ��ȡ�����б�
		String url = HttpUtil.BASE_URL + "/ListShameServlet";
		String shameStr = HttpUtil.queryStringForPost(url);// ��ʱ����
		if (shameStr == null || shameStr.equals("�����쳣")) {
			return null;
		}
		String[] shames = shameStr.split("@");// ����Ϊ2�����������ݣ�����û��@�Żᱨ��
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

	public static List<Comment> getCommentsByTopicID(String topicID) {// ���ݻ���ID��ȡ�����б�
		String url = HttpUtil.BASE_URL + "/ListCommentServlet?id=" + topicID;
		String commentStr = HttpUtil.queryStringForPost(url);// ��ʱ����
		if (commentStr == null || commentStr.equals("�����쳣")) {
			return null;
		}
		String[] comments = commentStr.split("@");// ����Ϊ2�����������ݣ�����û��@�Żᱨ��
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
	
	public static String submitComment(String topicID, String author, String comment) {// ���ݻ���ID�ύ����
		String url = HttpUtil.BASE_URL + "/AddCommentServlet?topicID="+topicID+"&author="+author+"&comment="+comment;
		String commentStr = HttpUtil.queryStringForPost(url);// ��ʱ����
		if (commentStr == null || commentStr.equals("") || commentStr.equals("�����쳣")) {
			commentStr = "error";
		}
		return commentStr;
	}
}
