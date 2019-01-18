package com.dcb.vtmis.service;

import java.util.List;
import java.util.Map;

import com.dcb.vtmis.entity.VesselPlan;

public interface VesselPlanService {
	/**
	 * @Description 获取船舶计划server 
	 * @return
	 */
	public List<VesselPlan> getVesselPlan();
	
	/**
	 * 
	 * @Description 申报船舶计划  大船
	 * @param token
	 * @return
	 */
	public Map<String,VesselPlan> applyVesselPlan(String token) ;
	
	/**
	 * 
	 * @Description 修改船舶计划 
	 * @param token
	 * @return
	 */
	public Map<String,String> updateVesselPlan(String token,VesselPlan vp);

	/**
	 * 申报船舶计划  驳船
	 * @param token
	 * @return
	 */
	public Map<String, VesselPlan> applyVesselPlan2(String token);
}
