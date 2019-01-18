package com.dcb.test.myTest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dcb.vtmis.action.VesselPlanAction;

@Controller
public class MyTest {

	@RequestMapping("/")
	public String test(){
		ApplicationContext context = new ClassPathXmlApplicationContext("springmvc.xml");
		VesselPlanAction action = (VesselPlanAction) context.getBean("vesselPlanAction");
		action.applyVesselPlan();
		return "index";
	}
}
