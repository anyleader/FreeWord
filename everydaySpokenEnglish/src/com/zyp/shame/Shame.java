package com.zyp.shame;

import java.io.Serializable;

public class Shame implements Serializable {
	/**
	 * ��������ʵ��
	 */
	private static final long serialVersionUID = -4671336692414021712L;
	private String id;
	private String content;// ����
	private String image_url;//ͼƬ��ַ
	private String author;//����
	private String create_time;// ����ʱ��
	private String flag;// ɾ����ʶ
	private String remark;// �����ֶ�

//	public Shame(String id, String content, String image_url, String author, String create_time, String flag,
//			String remark) {
//		super();
//		this.id = id;
//		this.content = content;
//		this.image_url = image_url;
//		this.author = author;
//		this.create_time = create_time;
//		this.flag = flag;
//		this.remark = remark;
//	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
