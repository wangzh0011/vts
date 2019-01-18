package com.dcb.vtmis.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.dcb.vtmis.entity.VesselPlan;

public interface VesselPlanDao {
	/**
	 * ��ȡ�����ƻ�  ��
	 * @return
	 */
	public List<VesselPlan> getVesselPlan();
	
	/**
	 * ��ȡ�����ƻ� ����
	 * @return
	 */
	public List<VesselPlan> getVesselPlan2();
	
	/**
	 * 
	 * @Description ��vesselPlan�������ݴ������ݿ� 
	 * @param vp
	 * @param statusCode
	 */
	public void insertVesselPlan(VesselPlan vp);
	
	/**
	 * 
	 * @Description ����vesselPlan���� 
	 * @param vp
	 */
	public void updateVesselPlan(VesselPlan vp);

	/**
	 * 
	 * @Description �ж�vesselPlan�Ƿ����
	 * @param referenceId
	 * @return
	 * @throws SQLException
	 */
	public int isExist(String referenceId);

	/**
	 * ����referenceIDɾ�������ƻ�
	 * @param referenceId
	 */
	public void deleteVesselPlan(String referenceId);

	/**
	 * �ж��Ƿ��и���  vesselPlanʵ��Ϊ�գ���û�и��¡�
	 * @param referenceId
	 * @return
	 */
	public VesselPlan hasUpdate(VesselPlan vp);

		
}
