package com.dcb.vtmis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dcb.vtmis.service.VesselPlanService;
import com.dcb.vtmis.util.PropertiesUtil;

/**
 * @Description 船舶计划相关action
 * @author alan.wang
 * @date 2018年10月18日
 */
@Controller
public class VesselPlanAction {
	
	@Resource
	private VesselPlanService vesselPlanService;
	
	private static final Logger log = Logger.getLogger(VesselPlanAction.class);
	private static Properties pro = PropertiesUtil.getProperties("/vtmis.properties");
	
	/**
	 * 用于身份认证
	 * @return
	 */
	public String doAuthentication() {
		HttpPost post = new HttpPost(pro.getProperty("authUrl"));
		RequestConfig config = RequestConfig.custom().setAuthenticationEnabled(true)
				.setProxy(new HttpHost(pro.getProperty("proxyIp"), Integer.parseInt(pro.getProperty("proxyPort")), "HTTP"))//設置代理
					.setConnectionRequestTimeout(5000).setConnectTimeout(5000).build();
		post.setConfig(config);
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.setDefaultRequestConfig(config).build();
		CloseableHttpResponse response = null;
		String token = null;
		JSONObject postData = new JSONObject();
		postData.put("grant_type", pro.getProperty("grant_type"));
		postData.put("username", pro.getProperty("username"));
		postData.put("password", pro.getProperty("password"));
		postData.put("client_id", pro.getProperty("client_id"));
		postData.put("client_secret", pro.getProperty("client_secret"));
		post.setEntity(new StringEntity(postData.toString(),"utf-8"));
		post.setHeader("Content-Type", "application/json"); // 更改传输数据的格式。
		log.info("身份认证請求路径： "+post.getRequestLine());
		try {
			response = client.execute(post);
			String str = EntityUtils.toString(response.getEntity());
			log.info("返回状态码："+response);
			log.info("用户认证返回的json数据: "+str);
			token = new JSONObject(str).getString("Access_Token");
		} catch (ClientProtocolException e) {
			log.error(e.getMessage(),e);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}finally {
			post.abort();
            if (response != null) {
                 EntityUtils.consumeQuietly(response.getEntity());
            }

		}
//		session.setAttribute("token", token);
		return token;
	}
	
	/**
	 * 查询船舶计划
	 * @param token
	 */
	@Deprecated
	public void getVesselPlan(String token,String planID) {
		HttpClientBuilder build = HttpClientBuilder.create();
		try {
			log.debug("查询船舶计划请求uri");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("access_token", token));
			params.add(new BasicNameValuePair("PlanID", planID));
			String uri = pro.getProperty("vesselPlanUrl")+"?"+URLEncodedUtils.format(params, "utf-8");
			HttpGet get = new HttpGet(uri);
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(5000).setSocketTimeout(5000)
					.setAuthenticationEnabled(true)
					.setProxy(new HttpHost(pro.getProperty("proxyIp"), Integer.parseInt(pro.getProperty("proxyPort")), "HTTP"))
					.build();
			get.setConfig(config);
			CloseableHttpClient client = build.setDefaultRequestConfig(config).build();
			log.info("查询船舶计划请求路径： "+get.getRequestLine());
			HttpResponse response = client.execute(get);
			log.info("查询船舶计划请求的响应状态码： "+response.getStatusLine());
			String str = new String(EntityUtils.toString(response.getEntity(),"utf-8"));
			log.info("根据planID查询船舶计划： "+str);

		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 申报船舶计划
	 * @param token
	 */
	@RequestMapping("/")
	public String applyVesselPlan() {
		log.info("\n begin to do task");
		String token = doAuthentication();
		vesselPlanService.applyVesselPlan(token);
		vesselPlanService.applyVesselPlan2(token);
//		VesselPlan vp = map.get("200");
//		model.addAttribute(vp);
		return "index";
	}
	
	/**
	 * @Description 修改船舶计划
	 */
	/*public void updateVesselPlan(String token) {
		Map<String,String> map = vesselPlanService.updateVesselPlan(token);
	}*/
	
	/**
	 * 
	 * @Description 撤销船舶计划
	 */
	public void deleteVesselPlan(String token,Integer PlanId) {
		log.info("===========into deleteVesselPlan...==============");
		HttpDelete delete = new HttpDelete(pro.getProperty("vesselPlanUrl")+"/"+PlanId+"?access_token="+token);
		RequestConfig config = RequestConfig.custom().setAuthenticationEnabled(true)
				.setProxy(new HttpHost(pro.getProperty("proxyIp"), Integer.parseInt(pro.getProperty("proxyPort")), "HTTP"))//設置代理
					.setConnectionRequestTimeout(5000).setConnectTimeout(5000).build();
		delete.setConfig(config);
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.setDefaultRequestConfig(config).build();
		CloseableHttpResponse response = null;
		try {
			response = client.execute(delete);
			log.info("DELETE请求返回的状态码："+response.getStatusLine());
		} catch (ClientProtocolException e) {
			log.error(e.getMessage(),e);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
	}

	public VesselPlanService getVesselPlanService() {
		return vesselPlanService;
	}

	public void setVesselPlanService(VesselPlanService vesselPlanService) {
		this.vesselPlanService = vesselPlanService;
	}
	
	
}
