package com.dcb.vtmis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dcb.vtmis.action.VesselPlanAction;

public class Test {
	
	public static void main(String[] args) {
		
		//JDBCUtil.getConnection();
		ApplicationContext context = new ClassPathXmlApplicationContext("springmvc.xml");
		VesselPlanAction action = (VesselPlanAction) context.getBean("vesselPlanAction");
		action.applyVesselPlan();
		/*String token = action.doAuthentication();
		action.applyVesselPlan(token);*/
//		action.updateVesselPlan(token);
//		action.getVesselPlan(token,"0");
//		action.deleteVesselPlan(token, 10234);
		
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -2);//��ȡ��ǰʱ���ǰ2��
		try {
			System.out.println(sdf.parse("2018/12/26 00:50").before(calendar.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		

	}


}



