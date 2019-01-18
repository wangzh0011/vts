package com.dcb.vtmis.util;


import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailUtil {
	private static final Logger log = Logger.getLogger(MailUtil.class); 
	private static Properties pro = PropertiesUtil.getProperties("/vtmis.properties");
	
	private InternetAddress[] toInternetAddress(String[] to) {
		InternetAddress[] addresses = new InternetAddress[to.length];
		try {
			for (int i = 0; i < to.length; i++) {
				InternetAddress address = new InternetAddress(to[i]);
				addresses[i] = address;
			}
		} catch (AddressException e) {
			e.printStackTrace();
		}
		return addresses;
	}
	
	private static String[] toArray(String mails) {
		if (mails == null || mails == "") {
			return null;
		}
		return mails.split(",");
	}
	
	private InternetAddress[] toCCInternetAddress() {
		String []cc = toArray(pro.getProperty("tocc"));
		if( cc != null && cc.length > 0 ) {
			return toInternetAddress(cc);
		}
		return new InternetAddress[]{};
	}
	
	public void sendEmail(String[] to, String subject, String html,
			JavaMailSender mailSender){
		try {
			MimeMessage mime = mailSender.createMimeMessage();

			MimeMessageHelper helper;
			helper = new MimeMessageHelper(mime,
					MimeMessageHelper.MULTIPART_MODE_MIXED, "utf-8");
			
			// �����ռ��ˣ��ļ���
			helper.setTo(toInternetAddress(to));
			//���ó���
			InternetAddress[] ccc = toCCInternetAddress();
			if(ccc != null && ccc.length > 0 ) {
				helper.setCc(ccc);
			}
			helper.setFrom(new InternetAddress(pro.getProperty("from")));
			helper.setSubject(subject);
			helper.setText(html, true);
			mailSender.send(mime);
		} catch (NullPointerException e) {
			log.error("�ʼ�Ϊ��!",e);
		} catch (MailException e) {
			log.error("���ʼ�ʱ������!",e);
		} catch (MessagingException e) {
			log.error("�ʼ�����ʱ������!",e);
		}
	}
}
