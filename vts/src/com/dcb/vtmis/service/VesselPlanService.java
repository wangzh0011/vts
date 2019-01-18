package com.dcb.vtmis.service;

import java.util.List;
import java.util.Map;

import com.dcb.vtmis.entity.VesselPlan;

public interface VesselPlanService {
	/**
	 * @Description ��ȡ�����ƻ�server 
	 * @return
	 */
	public List<VesselPlan> getVesselPlan();
	
	/**
	 * 
	 * @Description �걨�����ƻ�  ��
	 * @param token
	 * @return
	 */
	public Map<String,VesselPlan> applyVesselPlan(String token) ;
	
	/**
	 * 
	 * @Description �޸Ĵ����ƻ� 
	 * @param token
	 * @return
	 */
	public Map<String,String> updateVesselPlan(String token,VesselPlan vp);

	/**
	 * �걨�����ƻ�  ����
	 * @param token
	 * @return
	 */
	public Map<String, VesselPlan> applyVesselPlan2(String token);
}
