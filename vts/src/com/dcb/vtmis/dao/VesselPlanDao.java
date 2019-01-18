package com.dcb.vtmis.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.dcb.vtmis.entity.VesselPlan;

public interface VesselPlanDao {
	/**
	 * 获取船舶计划  大船
	 * @return
	 */
	public List<VesselPlan> getVesselPlan();
	
	/**
	 * 获取船舶计划 驳船
	 * @return
	 */
	public List<VesselPlan> getVesselPlan2();
	
	/**
	 * 
	 * @Description 将vesselPlan对象数据存入数据库 
	 * @param vp
	 * @param statusCode
	 */
	public void insertVesselPlan(VesselPlan vp);
	
	/**
	 * 
	 * @Description 更新vesselPlan对象 
	 * @param vp
	 */
	public void updateVesselPlan(VesselPlan vp);

	/**
	 * 
	 * @Description 判断vesselPlan是否存在
	 * @param referenceId
	 * @return
	 * @throws SQLException
	 */
	public int isExist(String referenceId);

	/**
	 * 根据referenceID删除船舶计划
	 * @param referenceId
	 */
	public void deleteVesselPlan(String referenceId);

	/**
	 * 判断是否有更新  vesselPlan实例为空，则没有更新。
	 * @param referenceId
	 * @return
	 */
	public VesselPlan hasUpdate(VesselPlan vp);

		
}
