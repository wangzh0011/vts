/*package com.dcb.vtmis.job;

import org.apache.log4j.Logger;

public class VesselPlanScheduler {
	
	private static final Logger log = Logger.getLogger(VesselPlanScheduler.class);
	
	public static void main(String[] args) {
		//Properties pro = PropertiesUtil.getProperties("/vtmis.properties");
		try {
			log.debug("µ÷¶ÈÆ÷Scheduler");
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(VesselPlanJob.class).withIdentity("vesselPlanJob", Scheduler.DEFAULT_GROUP).build();
			log.debug("´¥·¢Æ÷Trigger");
			Trigger cronTrigger = TriggerBuilder.newTrigger()
			.withIdentity("cronTrigger", Scheduler.DEFAULT_GROUP)
			.withSchedule(CronScheduleBuilder.cronSchedule(pro.getProperty("expression")))
			.build();
			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}
}
*/