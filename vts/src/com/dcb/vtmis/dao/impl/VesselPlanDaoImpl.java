package com.dcb.vtmis.dao.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dcb.vtmis.dao.VesselPlanDao;
import com.dcb.vtmis.entity.VesselPlan;
import com.dcb.vtmis.util.JDBCUtil;
import com.dcb.vtmis.util.PropertiesUtil;

@Repository
public class VesselPlanDaoImpl implements VesselPlanDao{
	
	private static final Logger log = Logger.getLogger(VesselPlanDaoImpl.class);
	private static Properties pro = PropertiesUtil.getProperties("/vtmis.properties"); 
	
	//���걨
	@Override
	public List<VesselPlan> getVesselPlan() {
		Connection conn = null;
		if(conn == null) {
			conn = JDBCUtil.getConnection();
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		List<VesselPlan> list = new ArrayList<VesselPlan>();
		try {
			ps = conn.prepareStatement(pro.getProperty("vesselPlanSql"));
			rs = ps.executeQuery();
			rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()) {
				Class c = Class.forName("com.dcb.vtmis.entity.VesselPlan");
				Field[] field = c.getDeclaredFields();
				VesselPlan vp = (VesselPlan) c.newInstance();
				for(int i = 1;i <= columnCount;i++) {
					String columnName = rsmd.getColumnName(i);
					for(int j = 1;j < field.length;j++) {
						if(columnName.equalsIgnoreCase(field[j].getName())) {
							Object obj = rs.getObject(i);
							field[j].setAccessible(true);//˽�б���������Ϊ�ɷ���
							field[j].set(vp, obj);
							break;
						}
					}
				}
				String etb = rs.getString("etb");
				String etd = rs.getString("etd");
				String atb = rs.getString("atb");
				String atd = rs.getString("atd");
				String phase = rs.getString("PHASE");
				String custdff_vd01 = rs.getString("CUSTDFF_VD01");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, -10);//��ȡ��ǰʱ���ǰ10��
				if(sdf.parse(etb).before(calendar.getTime())){
					continue;
				}
				if("Y".equals(custdff_vd01)){//���ο���
					if(phase.contains("Inbound".toUpperCase())){
						vp.setVesselActionCode(new Integer(1));
						vp.setPlanTime(sdf.parse(etb));
					}else if(atb != null && atd == null){
						vp.setVesselActionCode(new Integer(3));
						vp.setPlanTime(sdf.parse(etd));
					}
				}else{
					if(atb == null && phase.contains("Inbound".toUpperCase())){//����
						vp.setVesselActionCode(new Integer(1));
						vp.setPlanTime(sdf.parse(etb));
					}else if(atb != null && etd != null && atd == null){//�벴
						vp.setVesselActionCode(new Integer(3));
						vp.setPlanTime(sdf.parse(etd));
					}else{
						continue;
					}
				}
				//��λ
				vp.setLocaltionName(getBerth(rs.getString("bertharrive"),rs.getString("comments")));
				vp.setId(0);
				vp.setComments("��������");
				list.add(vp);
				
			}
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(),e);
		} catch (InstantiationException e) {
			log.error(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(),e);
		} catch (ParseException e) {
			log.error(e.getMessage(),e);
		}finally {
			CloseConnection(rs,ps,conn);
		}
		return list;
	}
	
	//�����걨
	@Override
	public List<VesselPlan> getVesselPlan2() {
		Connection conn = null;
		if(conn == null) {
			conn = JDBCUtil.getConnection();
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		List<VesselPlan> list = new ArrayList<VesselPlan>();
		try {
			ps = conn.prepareStatement(pro.getProperty("vesselPlanSql2"));
			rs = ps.executeQuery();
			rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()) {
				Class c = Class.forName("com.dcb.vtmis.entity.VesselPlan");
				Field[] field = c.getDeclaredFields();
				VesselPlan vp = (VesselPlan) c.newInstance();
				for(int i = 1;i <= columnCount;i++) {
					String columnName = rsmd.getColumnName(i);
					for(int j = 1;j < field.length;j++) {
						if(columnName.equalsIgnoreCase(field[j].getName())) {
							Object obj = rs.getObject(i);
							field[j].setAccessible(true);//˽�б���������Ϊ�ɷ���
							field[j].set(vp, obj);
							break;
						}
					}
				}
				
				String etb = rs.getString("etb");//Ԥ�ƿ���ʱ��
				String etd = rs.getString("etd");//Ԥ���벴ʱ��
				String atb = rs.getString("atb");//ʵ�ʿ���ʱ��
				String atd = rs.getString("atd");//ʵ���벴ʱ��
				String phase = rs.getString("PHASE");
				String custdff_vd01 = rs.getString("CUSTDFF_VD01");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DAY_OF_MONTH, -10);//��ȡ��ǰʱ���ǰ10��
				if(sdf.parse(etb).before(calendar.getTime())){
					continue;
				}
				if("Y".equals(custdff_vd01)){//���ο���
					if(phase.contains("Inbound".toUpperCase())){
						vp.setVesselActionCode(new Integer(1));
						vp.setPlanTime(sdf.parse(etb));
					}else if(atb != null && atd == null){
						vp.setVesselActionCode(new Integer(3));
						vp.setPlanTime(sdf.parse(etd));
					}
				}else{
					if(atb == null && phase.contains("Inbound".toUpperCase())){//����
						vp.setVesselActionCode(new Integer(1));
						vp.setPlanTime(sdf.parse(etb));
					}else if(atb != null && etd != null && atd == null){//�벴
						vp.setVesselActionCode(new Integer(3));
						vp.setPlanTime(sdf.parse(etd));
					}else{
						continue;
					}
				}
				//��λ
				vp.setLocaltionName(getBerth(rs.getString("bertharrive"),rs.getString("comments")));
				vp.setId(0);
				vp.setComments("��������");
				
				String[] port = getVesselPlanPort(vp.getReferenceId());//��ȡ���¸�
				vp.setPreviousPort(port[0]);
				vp.setNextPort(port[1]);
				list.add(vp);
				
				//test
				
				/*if(k++ == 10){
					break;
				}*/
			}
		} catch (SQLException e) {
			log.error(e.getMessage(),e);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(),e);
		} catch (InstantiationException e) {
			log.error(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(),e);
		} catch (ParseException e) {
			log.error(e.getMessage(),e);
		}finally {
			CloseConnection(rs,ps,conn);
		}
		return list;
	}
	
	/**
	 * ��ȡ���¸�
	 * @param dcbVoyCd
	 * @param conn
	 * @return
	 */
	public String[] getVesselPlanPort(String dcbVoyCd){
		Connection conn = null;
		if(conn == null) {
			conn = JDBCUtil.getConnection2();
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		String[] str = null;
		try {
			ps = conn.prepareStatement(pro.getProperty("vesselPlanPortSql"));
			ps.setString(1, dcbVoyCd);
			ps.setString(2, dcbVoyCd);
			rs = ps.executeQuery();
			while(rs.next()){
				String prePort = rs.getString("prePort")==null?"":rs.getString("prePort");
				String nextPort = rs.getString("nextPort")==null?"":rs.getString("nextPort");
				str = new String[]{prePort,nextPort};
				return str;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			CloseConnection(rs,ps,conn);
		}
		return new String[]{"",""};
	}
	
	@Override
	public void insertVesselPlan(VesselPlan vp) {
		Connection conn = null;
		if(conn == null) {
			conn = JDBCUtil.getConnection1();
		}
		/*int n = 0;
		try {
			n = isExist(vp.getReferenceId(), conn);
		} catch (SQLException e1) {
			log.error(e1.getMessage(),e1);
			return;
		}
		if(n == 1) {
			//update
			updateVesselPlan(vp, conn);
		}else {*/
			//insert
			PreparedStatement ps = null;
			try {
				String sql = pro.getProperty("insertVesselPlanSql");
				ps = conn.prepareStatement(sql);
				setValues(ps, vp,"insert");
				
			} catch (SQLException e) {
				log.error(e.getMessage(),e);
			}finally {
				ResultSet rs = null;
				CloseConnection(rs,ps,conn);
			}
//		}
	}
	
	/*public int isExist(String referenceId,Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = conn.prepareStatement(pro.getProperty("isExistSql"));
		ps.setString(1, referenceId);
		rs = ps.executeQuery();
		while(rs.next()) {
			if(rs.getInt(1) == 1) {
				return 1;//exist
			}
		}
		return 0;//not exist
	}*/
	
	@Override
	public int isExist(String referenceId){
		Connection conn = null;
		if(conn == null) {
			conn = JDBCUtil.getConnection1();
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(pro.getProperty("isExistSql"));
			ps.setString(1, referenceId);
			rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getInt(1) != 0) {
					return 1;//exist
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			CloseConnection(rs,ps,conn);
		}
		return 0;//not exist
	}

	
	@Override
	public void updateVesselPlan(VesselPlan vp) {
		Connection conn = null;
		if(conn == null) {
			conn = JDBCUtil.getConnection1();
		}
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(pro.getProperty("updateVesselPlanSql"));
			setValues(ps, vp,"update");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			ResultSet rs = null;
			CloseConnection(rs,ps,conn);
		}
	}
	
	/*public void updateVesselPlan(VesselPlan vp,Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(pro.getProperty("updateVesselPlanSql"));
			setValues(ps, vp,"update");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/

	
	/**
	 * �ر�����
	 * @param rs
	 * @param ps
	 */
	public void CloseConnection(ResultSet rs,PreparedStatement ps,Connection conn) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.error(e.getMessage(),e);
			}finally {
				if(ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						log.error(e.getMessage(),e);
					}finally {
						if(conn != null) {
							try {
								conn.close();
							} catch (SQLException e) {
								log.error(e.getMessage(),e);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * insert/update ģ��
	 * @param ps
	 * @param vp
	 * @param flag
	 * @return
	 */
	public PreparedStatement setValues(PreparedStatement ps,VesselPlan vp,String flag) {
		try {
			int i = 1;
			//a ˳����sql���ĸı���ı䡣
			ps.setInt(i++, vp.getId()==null?0:vp.getId());
			ps.setString(i++, vp.getReferenceId()==null?"":vp.getReferenceId());
			ps.setString(i++, vp.getLocalShipName()==null?"":vp.getLocalShipName());
			ps.setString(i++, vp.getEnShipName()==null?"":vp.getEnShipName());
			ps.setString(i++, vp.getMmsi()==null?"":vp.getMmsi());
			ps.setString(i++, vp.getImono()==null?"":vp.getImono());
			ps.setInt(i++, vp.getFlagCode()==null?0:vp.getFlagCode());
			ps.setString(i++, vp.getShipTypeCode()==null?"":vp.getShipTypeCode());
			ps.setDouble(i++, vp.getDwt()==null?0.0:vp.getDwt());
			ps.setString(i++, vp.getCallSign()==null?"":vp.getCallSign());
			ps.setString(i++, vp.getShipNo()==null?"":vp.getShipNo());
			ps.setDouble(i++, vp.getVesselLength()==null?0.0:vp.getVesselLength());
			ps.setDouble(i++, vp.getGross()==null?0.0:vp.getGross());
			ps.setDouble(i++, vp.getNet()==null?0.0:vp.getNet());
			ps.setInt(i++, vp.getVesselActionCode()==null?0:vp.getVesselActionCode());
			ps.setString(i++, vp.getLocaltionName()==null?"":vp.getLocaltionName());
			ps.setString(i++, (vp.getPlanTime()==null?new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()):new SimpleDateFormat("yyyy-MM-dd HH:mm").format(vp.getPlanTime())));
			ps.setString(i++, vp.getSubmitContact()==null?"":vp.getSubmitContact());
			ps.setString(i++, vp.getCaptainNumber()==null?"":vp.getCaptainNumber());
			ps.setString(i++, vp.getPreviousPort()==null?"":vp.getPreviousPort());
			ps.setString(i++, vp.getNextPort()==null?"":vp.getNextPort());
			ps.setString(i++, vp.getCargoName()==null?"":vp.getCargoName());
			ps.setDouble(i++, vp.getCargoWeight()==null?0.0:vp.getCargoWeight());
			ps.setInt(i++, vp.getStatus()==null?0:vp.getStatus());
			ps.setString(i++, vp.getVoyageNumber()==null?"":vp.getVoyageNumber());
			ps.setString(i++, vp.getComments()==null?"":vp.getComments());
			ps.setString(i++, vp.getJsonStatus()==null?"":vp.getJsonStatus());
			ps.setString(i++, new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));
			if(flag.equals("update")) {
				ps.setString(i++, vp.getReferenceId()==null?"":vp.getReferenceId());
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}
	
	@Override
	public void deleteVesselPlan(String referenceId){
		Connection conn = null;
		if(conn == null) {
			conn = JDBCUtil.getConnection1();
		}
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(pro.getProperty("deleteVesselPlanSql"));
			ps.setString(1, referenceId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			ResultSet rs = null;
			CloseConnection(rs,ps,conn);
		}
	}
	
	/**
	 * ƥ�䲴λ��Ϣ
	 * @param berthNo
	 * @return
	 */
	public String getBerth(String berthNo,String comments){
		switch(berthNo){
		case "B0": if(comments.equals("DCBI")){return "DCW2";}else if(comments.equals("DCBD")){return "DCW1";}
		case "B1": return "DCW1";
		case "B2": return "DCW2";
		case "B3": return "DCW3";
		case "B4": return "DCW4";
		case "B5": return "DCW5";
		default:return "";
		}
	}
	
	/**
	 * vesselPlanʵ��Ϊ�գ���û�и��¡�
	 */
	@Override
	public VesselPlan hasUpdate(VesselPlan vp){
		Connection conn = null;
		if(conn == null) {
			conn = JDBCUtil.getConnection1();
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		VesselPlan vessel = null;
		try {
			ps = conn.prepareStatement(pro.getProperty("getVesselPlanSql"));
			ps.setString(1, vp.getReferenceId());
			rs = ps.executeQuery();
			while(rs.next()){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				String planTime = sdf.format(rs.getTimestamp("plantime"));
				Integer vesselActionCode = rs.getInt("vesselActionCode");
				String enShipName = rs.getString("enShipName");
				String localtionName = rs.getString("localtionName");
				String previousPort = rs.getString("previousPort")==null?"":rs.getString("previousPort");
				String nextPort = rs.getString("nextPort")==null?"":rs.getString("nextPort");
				String comments = rs.getString("comments")==null?"":rs.getString("comments");
				vessel = new VesselPlan(vp.getReferenceId(), enShipName, vesselActionCode, localtionName, sdf.parse(planTime), previousPort, nextPort, comments);
				if(planTime.equals(sdf.format(vp.getPlanTime())) && 
						vesselActionCode.equals(vp.getVesselActionCode()) &&
						localtionName.equals(vp.getLocaltionName()) &&
						enShipName.equals(vp.getEnShipName()) &&
						previousPort.equals(vp.getPreviousPort()==null?"":vp.getPreviousPort()) &&
						nextPort.equals(vp.getNextPort()==null?"":vp.getNextPort()) && 
						comments.equals(vp.getComments()==null?"":vp.getComments())){
					vessel = null;
					return vessel;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}finally{
			CloseConnection(rs,ps,conn);
		}
		return vessel;
	}
}