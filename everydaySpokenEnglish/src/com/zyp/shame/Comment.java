package com.zyp.shame;

/**
 * ���������µ�����ʵ��
 */
public class Comment {
	private String id;
	private String topic_id;// ��������ID
	private String comment_author;// ������
	private String comment_content;// ��������
	private String comment_time;// ����ʱ��
	private String flag;// ɾ����ʶ
	private String remark;// �����ֶ�

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTopic_id() {
		return topic_id;
	}

	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}

	public String getComment_author() {
		return comment_author;
	}

	public void setComment_author(String comment_author) {
		this.comment_author = comment_author;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public String getComment_time() {
		return comment_time;
	}

	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
