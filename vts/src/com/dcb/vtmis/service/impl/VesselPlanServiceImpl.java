package com.dcb.vtmis.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.dcb.vtmis.dao.VesselPlanDao;
import com.dcb.vtmis.entity.VesselPlan;
import com.dcb.vtmis.service.VesselPlanService;
import com.dcb.vtmis.util.Constants;
import com.dcb.vtmis.util.HtmlFactory;
import com.dcb.vtmis.util.MailUtil;
import com.dcb.vtmis.util.PropertiesUtil;
import com.dcb.vtmis.util.Utils;

@Service("vesselPlanService")
public class VesselPlanServiceImpl implements VesselPlanService{
	
	private static final Logger log = Logger.getLogger(VesselPlanServiceImpl.class);
	private static Properties pro = PropertiesUtil.getProperties("/vtmis.properties");
	private String[] to = pro.getProperty("to").split(",");
	private MailUtil mailUtil = new MailUtil();
	private HtmlFactory htmlFactory = new HtmlFactory();
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private VesselPlanDao vesselPlanDao;
	
	@Override
	public List<VesselPlan> getVesselPlan() {
		return vesselPlanDao.getVesselPlan();
	}
	
	@Override
	public Map<String,VesselPlan> applyVesselPlan(String token) {//�걨��
		Map<String,VesselPlan> map = new HashMap<String,VesselPlan>();
		List<VesselPlan> list = vesselPlanDao.getVesselPlan();
		//�걨���
		doApplyVesselPlan(list, token, "��");
		return map;
	}
	
	@Override
	public Map<String,VesselPlan> applyVesselPlan2(String token) {//�걨����
		Map<String,VesselPlan> map = new HashMap<String,VesselPlan>();
		List<VesselPlan> list = vesselPlanDao.getVesselPlan2();
		//���ǵ�ͬһ���������ܻ���2����ͬ���Σ�һװһж��
		for(int i = 0; i < list.size(); i++){
			if(i < list.size()-1){
				if(list.get(i).getEnShipName().equals(list.get(i+1).getEnShipName()) 
						&& list.get(i).getVesselActionCode().equals(list.get(i+1).getVesselActionCode())){//�ж����������ƻ��Ƿ�����ͬһ����
					/*if(list.get(i).getPlanTime().equals(list.get(i+1).getPlanTime())){//�ж�Ԥ�ƿ��벴ʱ���Ƿ�һ��
						list.remove(i+1);
					}else{*/
						//mailUtil.sendEmail(to, "�����´�½������", htmlFactory.Make_Notice(list.get(i).getReferenceId()+","+list.get(i+1).getReferenceId(),list.get(i).getEnShipName()+","+list.get(i+1).getEnShipName(),list.get(i).getShipNo(),"",""), mailSender);
						if(list.get(i).getVesselActionCode().equals(1)){//����
							if(Utils.isNotNull(list.get(i).getEst_discharge()) /*&& Utils.isNotNull(list.get(i+1).getEst_load())*/){
								log.info("����"+list.get(i).getReferenceId());
								log.info("�Ƴ�"+list.get(i+1).getReferenceId());
								list.remove(i+1);	
							}else if(Utils.isNotNull(list.get(i+1).getEst_discharge()) /*&& Utils.isNotNull(list.get(i).getEst_load())*/){
								log.info("����"+list.get(i+1).getReferenceId());
								log.info("�Ƴ�"+list.get(i).getReferenceId());
								list.remove(i);
							}else{//����Ƴ�
								log.info("����"+list.get(i).getReferenceId());
								log.info("�Ƴ�"+list.get(i+1).getReferenceId());
								list.remove(i+1);
							}
						}else if(list.get(i).getVesselActionCode().equals(3)){//�벴
							if(Utils.isNotNull(list.get(i).getEst_load()) /*&& Utils.isNotNull(list.get(i+1).getEst_discharge())*/){
								log.info("����"+list.get(i).getReferenceId());
								log.info("�Ƴ�"+list.get(i+1).getReferenceId());
								list.remove(i+1);
							}else if(Utils.isNotNull(list.get(i+1).getEst_load()) /*&& Utils.isNotNull(list.get(i).getEst_discharge())*/){
								log.info("����"+list.get(i+1).getReferenceId());
								log.info("�Ƴ�"+list.get(i).getReferenceId());
								list.remove(i);
							}else{
								log.info("����"+list.get(i).getReferenceId());
								log.info("�Ƴ�"+list.get(i+1).getReferenceId());
								list.remove(i+1);
							}
						}
//					}
				}
			}
		}
		//�걨���
		doApplyVesselPlan(list, token, "����");		
		return map;
	}
	
	@Override
	public Map<String,String> updateVesselPlan(String token,VesselPlan vp) {
		log.info("==============================begin updateVesselPlan...=================================");
		HttpPut put = new HttpPut(pro.getProperty("vesselPlanUrl")+"?access_token="+token);
		RequestConfig config = RequestConfig.custom().setAuthenticationEnabled(true)
				.setProxy(new HttpHost(pro.getProperty("proxyIp"), Integer.parseInt(pro.getProperty("proxyPort")), "HTTP"))//�O�ô���
					.setConnectionRequestTimeout(5000).setConnectTimeout(5000).build();
		put.setConfig(config);
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.setDefaultRequestConfig(config).build();
		CloseableHttpResponse response = null;
//		List<VesselPlan> list = getVesselPlan();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Map<String,String> map = new HashMap<String,String>();
		JSONObject postData = new JSONObject();
		postData.put("ID", vp.getId());
		postData.put("ReferenceID", vp.getReferenceId());
		postData.put("LocalShipName", vp.getLocalShipName());
		postData.put("EnShipName", vp.getEnShipName());
		postData.put("MMSI", vp.getMmsi());
		postData.put("IMONO", vp.getImono());
		postData.put("FlagCode", vp.getFlagCode());
		postData.put("ShipTypeCode", vp.getShipTypeCode());
		postData.put("DWT", vp.getDwt());
		postData.put("Callsign", vp.getCallSign());
		postData.put("ShipNO", vp.getShipNo());
		postData.put("VesselLength", vp.getVesselLength());
		postData.put("Gross", vp.getGross());
		postData.put("Net", vp.getNet());
		postData.put("VesselActionCode", vp.getVesselActionCode());
		postData.put("LocationName", vp.getLocaltionName());
		postData.put("PlanTime", sdf.format(vp.getPlanTime()));
		postData.put("SubmitContact", vp.getSubmitContact());
		postData.put("CaptainNumber", vp.getCaptainNumber());
		postData.put("PreviousPort", vp.getPreviousPort());
		postData.put("NextPort", vp.getNextPort());
		postData.put("CargoName", vp.getCargoName());
		postData.put("CargoWeight", vp.getCargoWeight());
		postData.put("Status", vp.getStatus());
		postData.put("VoyageNumber", vp.getVoyageNumber());
		postData.put("Comments", vp.getComments());
		postData.put("Update", 1);//�޸�   Ĭ����0   �޸���1
		put.setEntity(new StringEntity(postData.toString(),"utf-8"));
		log.info("�޸����ݣ�"+postData.toString());
		put.setHeader("Content-Type", "application/json"); // ���Ĵ������ݵĸ�ʽ��
		put.setHeader("Accept", "application/json");
		try {
			log.info("getEntity(): "+put.getEntity());
			log.info("getRequestLine(): "+put.getRequestLine());
			response = client.execute(put);
			String str = EntityUtils.toString(response.getEntity());
			log.info("�޸Ĵ����ƻ���Ӧ״̬��"+response.getStatusLine());
			log.info("�޸Ĵ����ƻ���Ӧ���ݣ�"+str);
			String httpStatusCode = String.valueOf(response.getStatusLine().getStatusCode());
			vp.setJsonStatus(httpStatusCode);
			//����״̬�뷢���ʼ�
			sendEmail(httpStatusCode, vp, str, Constants.UPDATE);
		} catch (ClientProtocolException e) {
			log.error(e.getMessage(),e);
//				map.put("ClientProtocolException", e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage(),e);
//				map.put("IOException", e.getMessage());
		}
			
		return map;
	}
	
	/**
	 * �����ʼ�
	 * @param httpStatusCode
	 * @param vp
	 * @param content
	 * @param updateOrInsert
	 */
	private void sendEmail(String httpStatusCode,VesselPlan vp,String content,String updateOrInsert){
		if(httpStatusCode.equals("200")) {
			log.info(vp.getEnShipName()+" ���걨�ɹ�������");
			if("insert".equals(updateOrInsert)){
				vesselPlanDao.insertVesselPlan(vp);
			}else if("update".equals(updateOrInsert)){
				vesselPlanDao.updateVesselPlan(vp);
			}
			//�����ʼ�
//			mailUtil.sendEmail(to, Constants._200, htmlFactory.Make_Notice(vp.getReferenceId(),vp.getEnShipName(),vp.getShipNo(),Constants._200), mailSender);
			log.info("========================================================================================");
			log.info("========================================================================================");
		}
		else if(httpStatusCode.equals("400")) {
			log.warn(Constants._400);
			//ɾ�����ؿ����Ӧ������
			vesselPlanDao.deleteVesselPlan(vp.getReferenceId());
			//�����ʼ�
			mailUtil.sendEmail(to, Constants._400, htmlFactory.Make_Notice(vp.getReferenceId(),vp.getEnShipName(),vp.getShipNo(),Constants._400,"�����鴬���ƻ����ݸ�ʽ��ϵͳ����30���Ӻ������걨�˴����ƻ���"), mailSender);
			log.info("�����ʼ��ɹ�");
			log.info("========================================================================================");
			log.info("========================================================================================");
		}
		else if(httpStatusCode.equals("401")) {
			log.warn(Constants._401);
			mailUtil.sendEmail(to, Constants._401, htmlFactory.Make_Notice(vp.getReferenceId(),vp.getEnShipName(),vp.getShipNo(),Constants._401,"������ϵ���¾ּ���˺�Ȩ�����⡣"), mailSender);
		}
		else if(httpStatusCode.equals("403")) {
			log.warn(Constants._403);
			//ɾ�����ؿ����Ӧ������
			vesselPlanDao.deleteVesselPlan(vp.getReferenceId());
			log.info("��ɾ�����ؿ����ѱ���˵ļ�¼");
			//�����ʼ�
			//mailUtil.sendEmail(to, Constants._403, htmlFactory.Make_Notice(vp.getReferenceId(),vp.getEnShipName(),vp.getShipNo(),Constants._403,""), mailSender);
//			log.info("�����ʼ��ɹ�");
			log.info("========================================================================================");
			log.info("========================================================================================");
		}
		else if(httpStatusCode.equals("406")) {
			log.warn(vp.getEnShipName()+Constants._406);
			if(content.contains("�ƻ��ϱ��ظ�")){//����Ӧ����Ϊ�ƻ��ϱ��ظ��������û������ʼ���֪�û���ȡ���˼ƻ������걨
				vesselPlanDao.insertVesselPlan(vp);
				mailUtil.sendEmail(to, vp.getEnShipName()+"�ƻ��ϱ��ظ�", htmlFactory.Make_Notice(vp.getReferenceId(),vp.getEnShipName(),vp.getShipNo(),"�ƻ��ϱ��ظ�","�����¼�������мƻ��걨ƽ̨ȡ���˼ƻ���"), mailSender);
			}else{
				//�����ʼ�
				mailUtil.sendEmail(to, vp.getEnShipName()+Constants._406, htmlFactory.Make_Notice(vp.getReferenceId(),vp.getEnShipName(),vp.getShipNo(),Constants._406,""), mailSender);
			}
//			log.info("�����ʼ��ɹ�");
			log.info("========================================================================================");
			log.info("========================================================================================");
		}
		else if(httpStatusCode.equals("412")) {
			log.warn(Constants._412);
			//ɾ�����ؿ����Ӧ������
			vesselPlanDao.deleteVesselPlan(vp.getReferenceId());
			log.info("��ɾ�����ؿ����������걨������");
			//�����ʼ�
			mailUtil.sendEmail(to, Constants._412, htmlFactory.Make_Notice(vp.getReferenceId(),vp.getEnShipName(),vp.getShipNo(),Constants._412,""), mailSender);
			log.info("�����ʼ��ɹ�");
			log.info("========================================================================================");
			log.info("========================================================================================");
		}
	}
	
	/**
	 * �����ƻ��걨ģ��
	 * @param list
	 * @param token
	 * @param post
	 * @param client
	 * @param response
	 * @param shipType
	 */
	public void doApplyVesselPlan(List<VesselPlan> list,String token,String shipType){
		HttpPost post = new HttpPost(pro.getProperty("vesselPlanUrl")+"?access_token="+token);
		RequestConfig config = RequestConfig.custom().setAuthenticationEnabled(true)
				.setProxy(new HttpHost(pro.getProperty("proxyIp"), Integer.parseInt(pro.getProperty("proxyPort")), "HTTP"))//�O�ô���
					.setConnectionRequestTimeout(5000).setConnectTimeout(5000).build();
		post.setConfig(config);
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.setDefaultRequestConfig(config).build();
		CloseableHttpResponse response = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		int isExist = 0;
		VesselPlan vessel = null;
		for (VesselPlan vp : list) {
			/*log.debug("��������begin");
			log.debug("��������end");*/
			isExist = vesselPlanDao.isExist(vp.getReferenceId());
			if(isExist == 1) {
				log.info(vp.getReferenceId()+"�Ѵ���");
				//��vesselPlan�����ݱȽ��Ƿ��и���
				vessel = vesselPlanDao.hasUpdate(vp);
				if(vessel != null){//�����б仯 ���и��²���
					log.info("�����б仯�����ڸ������ݡ�");
					updateVesselPlan(token,vp);
					log.info("���ݱ��ǰ��"+vessel);//�������ݿ��е�����
					log.info("���ݱ����"+vp);//N4����
					log.info("========================================================================================");
					log.info("========================================================================================");
					continue;
				}else{
					log.info("�����ޱ仯�������и��²�����");
					log.info("========================================================================================");
					log.info("========================================================================================");
					continue;
				}
			}
			if("��".equals(shipType)){
				log.info("============================begin applyVesselPlan ��...================================");
			}else if("����".equals(shipType)){
				log.info("============================begin applyVesselPlan ����...================================");
			}
			JSONObject postData = new JSONObject();
			postData.put("ID", vp.getId());
			postData.put("ReferenceID", vp.getReferenceId());
			postData.put("LocalShipName", vp.getLocalShipName());
			postData.put("EnShipName", vp.getEnShipName());
			postData.put("MMSI", vp.getMmsi());
			postData.put("IMONO", vp.getImono());
			postData.put("FlagCode", vp.getFlagCode());
			postData.put("ShipTypeCode", vp.getShipTypeCode());
			postData.put("DWT", vp.getDwt());
			postData.put("Callsign", vp.getCallSign());
			postData.put("ShipNO", vp.getShipNo());
			postData.put("VesselLength", vp.getVesselLength());
			postData.put("Gross", vp.getGross());
			postData.put("Net", vp.getNet());
			postData.put("VesselActionCode", vp.getVesselActionCode());
			postData.put("LocationName", vp.getLocaltionName());
			postData.put("PlanTime", sdf.format(vp.getPlanTime()));
			postData.put("SubmitContact", vp.getSubmitContact());
			postData.put("CaptainNumber", vp.getCaptainNumber());
			postData.put("PreviousPort", vp.getPreviousPort());
			postData.put("NextPort", vp.getNextPort());
			postData.put("CargoName", vp.getCargoName());
			postData.put("CargoWeight", vp.getCargoWeight());
			postData.put("Status", vp.getStatus());
			postData.put("VoyageNumber", vp.getVoyageNumber());
			postData.put("Comments", vp.getComments());
			postData.put("Update", 0);
			post.setEntity(new StringEntity(postData.toString(),"utf-8"));
			log.info("�걨���ݣ�"+postData.toString());
			post.setHeader("Content-Type", "application/json"); // ���Ĵ������ݵĸ�ʽ��
			post.setHeader("Accept", "application/json");
			try {
				log.info("getEntity(): "+post.getEntity());
				log.info("getRequestLine(): "+post.getRequestLine());
				response = client.execute(post);
				String str = EntityUtils.toString(response.getEntity());
				log.info("�걨�����ƻ���Ӧ״̬��"+response.getStatusLine());
				log.info("�걨�����ƻ���Ӧ���ݣ�"+str);
				String httpStatusCode = String.valueOf(response.getStatusLine().getStatusCode());
				vp.setJsonStatus(httpStatusCode);
				//����״̬�뷢���ʼ�
				sendEmail(httpStatusCode, vp, str, Constants.INSERT);
//				map.put(httpStatusCode, vp);
			} catch (ClientProtocolException e) {
				log.error(e.getMessage(),e);
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		list.clear();
		System.gc();//����jvm������������
	}
	
	

}