package com.dcb.vtmis.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

public class HtmlFactory {
	private static final Logger log = Logger.getLogger(HtmlFactory.class); 
	FileInputStream fileStream = null;
	InputStreamReader reader = null;
	BufferedReader br = null;
	public String Make_Notice(String referenceId,String enShipName,String shipNo,String status,String howToDealWith){
		try {
			String classPath = this.getClass().getResource("/").getPath();// 2.ʹ����֪������·��������
			classPath = URLDecoder.decode(classPath, "utf-8");
			String projectPath = classPath.substring(0,
					classPath.lastIndexOf("classes/"))
					+ "MailHtml/1.html";
			fileStream = new FileInputStream(projectPath);
			reader = new InputStreamReader(fileStream, "UTF-8");
			br = new BufferedReader(reader);
			String data = null;
			StringBuffer domeHtml = new StringBuffer();
			while ((data = br.readLine()) != null) {
				domeHtml.append(data);
			}
			closeIO();
			String html = domeHtml.toString();
			return html.replace("referenceId", referenceId).replace("enShipName", enShipName).replace("status", status).replace("howToDealWith", howToDealWith);
		} catch (FileNotFoundException e) {
			log.error("htmlģ��û�ҵ���"+e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			log.error("����ָ���ַ���ʱ������"+e.getMessage(), e);
		} catch (IOException e) {
			log.error("��д�ļ�����"+e.getMessage(), e);
		}
		return null;
	}
	
	private void closeIO() throws IOException{
		try {
			br.close();
			reader.close();
			fileStream.close();
		} catch (IOException e) {
			throw new IOException("�ر��ļ���ȡ��ʱ������");
		}
	}
}