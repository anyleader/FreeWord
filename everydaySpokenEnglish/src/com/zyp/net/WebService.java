package com.zyp.net;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebService {
	public static byte[] getImage(String path){
		
		URL url;
		byte[] b=null;
		try {
			url = new URL(path);   //����URL
			HttpURLConnection con;
			
		    con = (HttpURLConnection)url.openConnection();  //������
		
			con.setRequestMethod("GET"); //�������󷽷�
			//�������ӳ�ʱʱ��Ϊ5s
			con.setConnectTimeout(5000);
			InputStream in=con.getInputStream();  //ȡ���ֽ�������
		
		    b=StreamTool.readInputStream(in);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return b;  //����byte����
		
	}
	
}
