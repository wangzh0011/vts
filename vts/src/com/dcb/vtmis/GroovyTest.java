import com.navis.argo.ContextHelper;
import com.navis.argo.business.api.ArgoUtils ;
import com.navis.argo.business.api.IEvent ;
import com.navis.argo.business.api.ServicesManager ;
import com.navis.argo.business.model.Facility ;
import com.navis.external.framework.AbstractExtensionCallback ;
import com.navis.framework.business.Roastery ;
import com.navis.framework.persistence.HibernateApi; 
import com.navis.framework.portal.QueryUtils ;
import com.navis.framework.portal.UserContext ;
import com.navis.framework.portal.query.DomainQuery; 
import com.navis.framework.portal.query.PredicateFactory ;
import com.navis.framework.util.BizFailure ;
import com.navis.framework.util.BizViolation ;
import com.navis.framework.util.TransactionParms ;
import com.navis.framework.util.message.MessageCollector ;
import com.navis.framework.util.message.MessageCollectorFactory ;
import com.navis.framework.util.message.MessageCollectorUtils ;
import com.navis.services.business.rules.EventType ;
import org.apache.commons.lang.StringUtils ;
import com.navis.inventory.business.atoms.UnitVisitStateEnum;
import com.navis.framework.business.Roastery;
import com.navis.framework.metafields.MetafieldEntry;
import com.navis.framework.metafields.MetafieldId;
import com.navis.framework.persistence.HibernateApi;
import com.navis.framework.portal.Ordering;
import com.navis.inventory.business.units.Unit;
import org.apache.commons.io.FilenameUtils;
import com.navis.argo.business.model.GeneralReference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import com.navis.argo.EdiFlexFields;
import com.navis.argo.EdiReleaseFlexFields;
import com.navis.argo.EdiReleaseIdentifier;
import com.navis.argo.ReleaseTransactionDocument;
import com.navis.argo.ReleaseTransactionsDocument;
import com.navis.external.edi.entity.AbstractEdiPostInterceptor;
import com.navis.framework.persistence.HibernatingEntity;
import com.navis.framework.business.Roastery;
import com.navis.framework.metafields.MetafieldEntry;
import com.navis.framework.metafields.MetafieldId;
import com.navis.framework.persistence.HibernateApi;
import com.navis.framework.portal.FieldChanges;
import com.navis.framework.portal.Ordering;
import com.navis.framework.portal.QueryUtils;
import com.navis.framework.portal.query.DomainQuery;
import com.navis.framework.portal.query.PredicateFactory;
import com.navis.inventory.business.units.Unit;
import com.navis.inventory.business.units.UnitFacilityVisit;
import com.navis.inventory.business.units.UnitFlag;
import org.apache.xmlbeans.XmlObject;
import com.navis.argo.business.api.ServicesManager;
import com.navis.services.business.rules.EventType;
import com.navis.framework.util.BizViolation;

import com.navis.argo.portal.job.GroovyJob;
import com.navis.argo.business.model.CarrierVisit;
import com.navis.argo.business.model.LocPosition;

import com.navis.inventory.business.atoms.UfvTransitStateEnum; 



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.navis.argo.business.model.GeneralReference;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.navis.apex.business.model.GroovyInjectionBase;


public class FriedEgg_Upload_N1 extends AbstractExtensionCallback{ 
	public String execute(Map args) throws BizFailure,IOException { 
		GeneralReference generalReferenceBOL = GeneralReference.findUniqueEntryById(FILE_DIRECTORIES_GR, ID1, null, null);
		UserContext context = ContextHelper.getThreadUserContext(); 
		Facility fcy = ContextHelper.getThreadFacility(); 
		Date timeNow = ArgoUtils.convertDateToLocalDateTime(ArgoUtils.timeNow(), context.getTimeZone()); 
		this.log("Update process start: " + (new SimpleDateFormat ("dd/MM/yyyy-hh:mm:ss")).format(timeNow)); 
		//populate root directory from general reference 
		String inboundReportsPath  = generalReferenceBOL.getRefValue1() ;
		//set up all directories under the root folder 
 		String inFolder = inboundReportsPath + File.separator+"in";
		File inFolderList = new File(inboundReportsPath + File.separator+"in")
		String archiveFolder = inboundReportsPath + File.separator+"archive"; 
		String logsFolder = inboundReportsPath + File.separator+"logs"; 
		String failedFolder = inboundReportsPath+File.separator+"failed"; 
		//Retrieve all files reside in the folders 
		File[] fileList = inFolderList.listFiles(); 
		if (fileList.length > 0) { 
			MessageCollector mc = getMessageCollector(); 
			try { 
				for (File file : fileList) { 
					MessageCollector msgCollector = MessageCollectorFactory.createMessageCollector(); 
					TransactionParms.getBoundParms().setMessageCollector(msgCollector); 
					String fileName = file.getName(); 
					String fileExtension = FilenameUtils.getExtension(fileName);
					File oldFile = new File(inFolder +  File.separator+ fileName)
					//Reject files if file extension is not csv 
					if (validFile(fileExtension, msgCollector)) { 
						//Only process files with csv extension 
						this.processThisFile(file, fileName, msgCollector, fcy); 
					} 
					if (msgCollector.getMessageCount() > 0) { 
						oldFile.renameTo(new File(failedFolder + File.separator + fileName))
						List <String> msgList = msgCollector.getMessages();
              					for (String msg : msgList) {
							msgLogWrite(failedFolder + File.separator + "errlog_" + fileName + ".txt",msg + "\r");
						}
						//mc.appendMessage(BizFailure.create("FILE: " + fileName)); 
						//MessageCollectorUtils.appendMessage(mc, msgCollector); 
					} else { 
						// move file archive folder 
						oldFile.renameTo(new File(archiveFolder + File.separator + fileName));
					} 
				} 
			} finally { 
				TransactionParms.getBoundParms().setMessageCollector(mc); 
			} 
		} else { 
			this.log("Found no CSV file");
			msgLogWrite(logsFolder + File.separator + "errlog.txt", (new SimpleDateFormat ("dd/MM/yyyy-hh:mm:ss")).format(timeNow) + " No CSV file exists" + "\r");
		} 
		this.log("Update process ends: " + (new SimpleDateFormat ("dd/MM/yyyy-hh:mm:ss")).format(timeNow)); 
	} 

	/** * * @param inFile * @param msgCollector */ 

	private void processThisFile(File inFile, String fileName, MessageCollector msgCollector, Facility fcy) { 
		//this.log("In processThisFile"); 
		try { 			FileInputStream fis = new FileInputStream(inFile.getAbsolutePath()); 
			try { 
				this.processFileStream(fis, msgCollector, fcy, fileName); 
			} finally { 
				if (fis != null) { fis.close(); 
				} 
			} 
		} catch (FileNotFoundException e){ 
			msgCollector.registerExceptions(e); 
		} catch (IOException e) { 
			msgCollector.registerExceptions(e); 
		} 
	} 

	/** * * @param inFis */ 
	private void processFileStream(InputStream inFis, MessageCollector msgCollector, Facility fcy, String fileName) { 
		//this.log("In processFileStream-0"); 
		Exception exception = null; 
		BufferedReader input = null; 
		input = new BufferedReader(new InputStreamReader(inFis)); 
		String line; 
		try { 
			//Skip the header 
			line = input.readLine(); 
			while ((line = input.readLine()) != null) { 
				processRecord(line, msgCollector, fcy, fileName); 
			} 
		} catch (IOException ex) { 
			exception = ex; 
		} catch (Exception ex) { 
			exception = ex; 
		} finally { 
			try { 
				if (input != null) { 
					input.close(); 
				} 
			} catch (IOException ex) { 
				exception = ex; 
			} 
		} 
		//Save error message to collector 
		if (exception != null) { 
			getMessageCollector().registerExceptions(exception); 
		} 
	} 

	private void processRecord(String inLine, MessageCollector msgCollector, Facility fcy, String fileName) { 
		//Populate all fields and store in string array from csv formatted 
		if (this.count(inLine,",")>=1) {
			String[] tokens = inLine.split(",", -1); 
			String unitId = null; 
			String position = null;
			String obCVId = null;
			String unitNotes = null;
		if ( unitId == "UnitNbr" )  {	return; }
			 
		if (tokens.length > 0) { 
			unitId   = tokens[0]; 
			position = tokens[1];
			obCVId   = tokens[2];
			unitNotes = tokens[3];
		} 
		
		//unitNotes = weight;
		
		this.log("fileName="+fileName+",Unit Id="+unitId+", unitNotes="+unitNotes  );  			
		
		Unit unit = this.getActiveUnit(unitId); 
		
		UnitFacilityVisit unitUfv = (UnitFacilityVisit) unit.getUnitUfvSet().iterator().next();
		UfvTransitStateEnum transit = unitUfv.getUfvTransitState();	
		if ( !transit.equals( UfvTransitStateEnum.S60_LOADED  ) )			
		{  return;  }	
			
			
		//Make sure unit record exists in N4 
		if (unit == null) { 
			//1. Send email to notify party concern and log an error message 
			String inSubject = "UnitNbr is null in file: " + fileName; 
			String inBody = "Cannot locate unit: " + unitId; 
			
			this.sendEmailAndRecordErrorMessage(msgCollector, inSubject, inBody); 
			this.log("Failed due to unit is null"); 
			
			return;
		} else {
			//Update unit position
			if ( position.length() >0 ){
					this.updateUnitFields( msgCollector, unit, unitId,  position, obCVId ); 
				} else {
					String inSubject = "Declaration Error with file: " + fileName; 
					String inBody = "The length of unitNotes for the unit " + unitId + " is not allowed null"; 
					this.sendEmailAndRecordErrorMessage(msgCollector, inSubject, inBody); 
					this.log("The length of unitNotes for the unit " + unitId + " is not allowed null"); 
					}
			}
		}
	} 


	private void sendEmailAndRecordErrorMessage(MessageCollector msgCollector, String inSubject, String inBody) { 
		//1.1 Send an email notification FILE_EMAIL_UTILS.sendEmail(1, inSubject, inBody); 
		//1.2 Also register an error message 
		msgCollector.appendMessage(BizFailure.create(inBody)); 
	} 

	private void updateUnitFields(MessageCollector msgCollector, Unit unit, String unitId, String position, String obCVID ) 
	{      if (unit == null) { 
			return; 
		} 

		this.log("Before updating unit"); 
		this.log("Updating unit notes");

		unit.updateRemarks( position );
		this.postUnitEvent( unit, position );

		this.log( "Updating unit position" );
		
	//verifyOBvisit
		CarrierVisit unitVisit = unit.getOutboundCv();
		String unitObCV = unitVisit.getCvId();
		
		unit.updateRemarks(  "unitObCV:"+ unitObCV +"  EDI obCVID: " + obCVID );	
		unit.updateRemarks(  "unitNotes:"+"TTTT");
		this.postUnitEvent(unit,"TTTT");		
		
		if ( !unitObCV.equals(obCVID) ) {
			sendEmail("EDI 船期VIsitId与Unit船期OB visit不一致","EDI 船期VIsitId与Unit船期OB visit不一致");
			unit.updateRemarks(  "EDI 船期VIsitId与Unit船期OB visit不一致");
		  return;
		}

	//update position( fileout start char' )
	 	int len = position.length();
	 	String pos = position.substring( 1, len );
	 //	unit.updateRemarks( aPos + "---"+ String.valueOf(len) );
		
		String from_pos = unit.findCurrentPosition().getPosName() ;
	 	LocPosition to_pos = LocPosition.createLocPosition( unitVisit.getLocType(), unitVisit.getLocId(), unitVisit.getLocGkey(), pos, null);
	
	 	unit.move(to_pos);
		this.postUnitEvent( unit, "From_pos:" + from_pos + "   ->   " + "to_pos:" + to_pos + "visitID:"+visitID );
				
	} 

	private void postUnitEvent(Unit unit, String str1) { 
		EventType eventType = EventType.findEventType(UPDATE_EVNT); 		
	
		ServicesManager sm = (ServicesManager) Roastery.getBean(ServicesManager.BEAN_ID); 
		if (eventType == null) { 
			this.log("Event type is not defined"); 
			return; 
		} 
		
		String eventNote = "update unit notes:" + str1;		
		try { 
			sm.recordEvent(eventType, eventNote , null, null, unit); 
		} catch (BizViolation inBv) { 
			this.log("Failed to insert unit event"); 
		} 
	} 


	/** * * @param inFileExt * @param msgCollector * @return */ 
	private boolean validFile(String inFileExt, MessageCollector msgCollector) 
	{ 
		boolean isValidFile = false; 
		isValidFile = "csv".equalsIgnoreCase(inFileExt); 
		if (!isValidFile) { 
			msgCollector.appendMessage(BizFailure.create("No CSV file exists" )) ; 
		} 
		return isValidFile; 
	}
	
	private Unit getActiveUnit(String sId) {
        	DomainQuery dq = QueryUtils.createDomainQuery("Unit");
        	MetafieldId miID = (new MetafieldEntry("unitId").getMetafieldId());
        	MetafieldId miVS = (new MetafieldEntry("unitVisitState").getMetafieldId());
        	MetafieldId miGK = (new MetafieldEntry("unitGkey").getMetafieldId());
        	dq.addDqPredicate(PredicateFactory.eq(miID,(Object) sId));
        	dq.addDqPredicate(PredicateFactory.eq(miVS,(Object) UnitVisitStateEnum.ACTIVE.getKey()));
        	dq.addDqOrdering(Ordering.desc(miGK));
        	dq.setMaxResults(1);
        	HibernateApi hbrapi = Roastery.getHibernateApi();
        	return (Unit) hbrapi.getUniqueEntityByDomainQuery(dq);

    	}

        private void msgLogWrite(String f, String s) throws IOException {
	        FileWriter aWriter = new FileWriter(f, true);
            	aWriter.write(s + "\n");
            	aWriter.flush();
            	aWriter.close();
        }

  	private int count(final String string, final String substring){
     		int count = 0;
     		int idx = 0;
     		while ((idx = string.indexOf(substring, idx)) != -1) {
        		idx++;
        		count++;
     		}
		return count;
  	}
  	
  	private boolean sendEmail(String subject,String content){
        def mailInfo=SimpleMailSender.getMailSender(subject,content);
        return SimpleMailSender.sendTextMail(mailInfo);
  	}


	private final String MTL_FILE_EMAIL_UTILS_CLASS = "MTLFileEmailUtils"; 
	private final String FILE_DIRECTORIES_GR = "FILE_DIRECTORIES"; 
	private final String ID1 = "UNITNOTES_UPLOAD"; 
	//protected final HibernateApi _hibernate; 
	private final String UPDATE_EVNT = "UNIT_PROPERTY_UPDATE"; 
		
	
}


 class SimpleMailSender extends GroovyInjectionBase{
	
	private static GeneralReference ref = GeneralReference.findUniqueEntryById("MSA_MAIL", "config", null, null);

	public static MailSender getMailSender(String subject, String content) {
		MailSender mailSender = new MailSender();
		mailSender.setMailServerHost("fmail.dachanbayone.com");
		//mailSender.setMailServerPort(ref.getRefValue2());
		mailSender.setValidate(false);
		mailSender.setFromAddress("alan.wang@dcbterminals.com");
		mailSender.setToAddress("alan.wang@dcbterminals.com");
		mailSender.setCc("alan.wang@dcbterminals.com");
		mailSender.setSubject(subject);
		mailSender.setContent(content);
		return mailSender;
	}
	
	/**
	 * 以文本格式发送邮件
	 * @param mailInfo 待发送的邮件的信息
	 */
	public static boolean sendTextMail(mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(),mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			String[] toAddresses=mailInfo.getToAddress().split(";");
			for(int i=0;i<toAddresses.length;i++){
				mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddresses[i]));
			}
			//抄送人
			if(mailInfo.getCc()!=null){
				String[] ccs=mailInfo.getCc().split(";");
				for(int i=0;i<ccs.length;i++){
					mailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccs[i]));
				}
			}
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 以HTML格式发送邮件
	 * @param mailInfo 待发送的邮件信息
	 */
	public static boolean sendHtmlMail(mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			String[] toAddresses=mailInfo.getToAddress().split(";");
			for(int i=0;i<toAddresses.length;i++){
				mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddresses[i]));
			}
			//抄送人
			if(mailInfo.getCc()!=null){
				String[] ccs=mailInfo.getCc().split(";");
				for(int i=0;i<ccs.length;i++){
					mailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccs[i]));
				}
			}
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

}

class MailSender extends GroovyInjectionBase{

	// 发送邮件的服务器的IP和端口
	private String mailServerHost;
	private String mailServerPort = "25";
	// 邮件发送者的地址
	private String fromAddress;
	// 邮件接收者的地址
	private String toAddress;
	//抄送者地址
	private String cc;
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	// 登陆邮件发送服务器的用户名和密码
	private String userName;
	private String password;
	// 是否需要身份验证
	private boolean validate = false;
	// 邮件主题
	private String subject;
	// 邮件的文本内容
	private String content;
	// 邮件附件的文件名
	private String[] attachFileNames;
	
	
	/** 
	 * 获得邮件会话属性 
	 */
	public Properties getProperties(){
		Properties p = new Properties();
		p.put("mail.smtp.host", this.mailServerHost);
		p.put("mail.smtp.port", this.mailServerPort);
		p.put("mail.smtp.auth", validate ? "true" : "false");
		return p;
	}
	public String getMailServerHost() {
		return mailServerHost;
	}
	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}  
	public String getMailServerPort() {
		return mailServerPort;
	}  
	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}  
	public boolean isValidate() {
		return validate;
	}  
	public void setValidate(boolean validate) {
		this.validate = validate;
	}  
	public String[] getAttachFileNames() {
		return attachFileNames;
	}  
	public void setAttachFileNames(String[] fileNames) {
		this.attachFileNames = fileNames;
	}  
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}  
	public String getPassword() {
		return password;
	}  
	public void setPassword(String password) {
		this.password = password;
	}  
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getUserName() {
		return userName;
	}  
	public void setUserName(String userName) {
		this.userName = userName;
	}  
	public String getSubject() {
		return subject;
	}  
	public void setSubject(String subject) {
		this.subject = subject;
	}  
	public String getContent() {
		return content;
	}  
	public void setContent(String textContent) {
		this.content = textContent;
	}
}

class MyAuthenticator extends Authenticator{
   String userName=null;     
   String password=null;     
         
   public MyAuthenticator(){    
   }     
   public MyAuthenticator(String username, String password) {      
       this.userName = username;      
       this.password = password;      
   }      
   protected PasswordAuthentication getPasswordAuthentication(){     
       return new PasswordAuthentication(userName, password);     
   }     
}