package com.dcb.vtmis.job;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dcb.vtmis.action.VesselPlanAction;
/**
 * 
 * @Description 船舶计划Job 
 * @author alan.wang
 * @date 2018年10月29日下午2:55:15
 */
@Component
public class VesselPlanJob{
	
	private static ApplicationContext context = new ClassPathXmlApplicationContext("classpath:springmvc.xml");
	private static VesselPlanAction action = (VesselPlanAction) context.getBean("vesselPlanAction");
	
	@Scheduled(cron="0 0/30 * * * ? ")
	public void taskForVesselPlan(){
		action.applyVesselPlan();
	}

}
