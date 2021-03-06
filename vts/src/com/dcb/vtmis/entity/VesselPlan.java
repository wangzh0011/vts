package com.dcb.vtmis.entity;

import java.io.Serializable;
import java.util.Date;

public class VesselPlan implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;//船舶计划标识号，提交船舶动态时请传0
	private String referenceId;//第三方系统内的ID，在使用接口提交申请时传入
	private String localShipName;//中文船名
	private String enShipName;//英文船名
	@Deprecated
	private String mmsi;//9位码;重复性校验,当为船舶种类为‘11222’时，可为空。其他船舶不可为空
	@Deprecated
	private String imono;//国外及国际航线的国内船舶，不可为空；7位数字 如果为空输入“555”
	@Deprecated
	private Integer flagCode;//国籍编码
	@Deprecated
	private String shipTypeCode;//船舶类型编码
	private Double dwt;//额定载重吨
	private String callSign;//呼号
	private String shipNo;//船舶识别号
	private Double vesselLength;//船舶长度
	private Double gross;//总吨
	private Double net;//净吨
	private Integer vesselActionCode;//船舶进离港  可填字段--1:对应“进港”，3:对应“离港”
	private String localtionName;//拟离地点
	private Date planTime;//预计时间
	@Deprecated
	private String submitContact;//短信通知号码
	@Deprecated
	private String captainNumber;//船方电话
	private String previousPort;//	上一港	
	private String nextPort;	//下一港	
	private String cargoName;	//货物名称	
	@Deprecated
	private Double cargoWeight;	//载货吨/载客人数	
	private Integer status;//Integer
	private String voyageNumber;//	航次	由第三方系统提供，对一条船舶值唯一
	private String comments;//	备注	String
	
	private String jsonStatus;//http返回码
	
	private String est_load;//装船量
	private String est_discharge;//卸船量
	
	public VesselPlan() {
		// TODO Auto-generated constructor stub
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getLocalShipName() {
		return localShipName;
	}
	public void setLocalShipName(String localShipName) {
		this.localShipName = localShipName;
	}
	public String getEnShipName() {
		return enShipName;
	}
	public void setEnShipName(String enShipName) {
		this.enShipName = enShipName;
	}
	@Deprecated
	public String getMmsi() {
		return mmsi;
	}
	@Deprecated
	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}
	@Deprecated
	public String getImono() {
		return imono;
	}
	@Deprecated
	public void setImono(String imono) {
		this.imono = imono;
	}
	@Deprecated
	public Integer getFlagCode() {
		return flagCode;
	}
	@Deprecated
	public void setFlagCode(Integer flagCode) {
		this.flagCode = flagCode;
	}
	@Deprecated
	public String getShipTypeCode() {
		return shipTypeCode;
	}
	@Deprecated
	public void setShipTypeCode(String shipTypeCode) {
		this.shipTypeCode = shipTypeCode;
	}
	public Double getDwt() {
		return dwt;
	}
	public void setDwt(Double dwt) {
		this.dwt = dwt;
	}
	public String getCallSign() {
		return callSign;
	}
	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}
	public String getShipNo() {
		return shipNo;
	}
	public void setShipNo(String shipNo) {
		this.shipNo = shipNo;
	}
	public Double getVesselLength() {
		return vesselLength;
	}
	public void setVesselLength(Double vesselLength) {
		this.vesselLength = vesselLength;
	}
	public Double getGross() {
		return gross;
	}
	public void setGross(Double gross) {
		this.gross = gross;
	}
	public Double getNet() {
		return net;
	}
	public void setNet(Double net) {
		this.net = net;
	}
	public Integer getVesselActionCode() {
		return vesselActionCode;
	}
	public void setVesselActionCode(Integer vesselActionCode) {
		this.vesselActionCode = vesselActionCode;
	}
	public String getLocaltionName() {
		return localtionName;
	}
	public void setLocaltionName(String localtionName) {
		this.localtionName = localtionName;
	}
	public Date getPlanTime() {
		return planTime;
	}
	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}
	@Deprecated
	public String getSubmitContact() {
		return submitContact;
	}
	@Deprecated
	public void setSubmitContact(String submitContact) {
		this.submitContact = submitContact;
	}
	@Deprecated
	public String getCaptainNumber() {
		return captainNumber;
	}
	@Deprecated
	public void setCaptainNumber(String captainNumber) {
		this.captainNumber = captainNumber;
	}
	public String getPreviousPort() {
		return previousPort;
	}
	public void setPreviousPort(String previousPort) {
		this.previousPort = previousPort;
	}
	public String getNextPort() {
		return nextPort;
	}
	public void setNextPort(String nextPort) {
		this.nextPort = nextPort;
	}
	public String getCargoName() {
		return cargoName;
	}
	public void setCargoName(String cargoName) {
		this.cargoName = cargoName;
	}
	@Deprecated
	public Double getCargoWeight() {
		return cargoWeight;
	}
	@Deprecated
	public void setCargoWeight(Double cargoWeight) {
		this.cargoWeight = cargoWeight;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getVoyageNumber() {
		return voyageNumber;
	}
	public void setVoyageNumber(String voyageNumber) {
		this.voyageNumber = voyageNumber;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getJsonStatus() {
		return jsonStatus;
	}

	public void setJsonStatus(String jsonStatus) {
		this.jsonStatus = jsonStatus;
	}

	public String getEst_load() {
		return est_load;
	}

	public void setEst_load(String est_load) {
		this.est_load = est_load;
	}

	public String getEst_discharge() {
		return est_discharge;
	}

	public void setEst_discharge(String est_discharge) {
		this.est_discharge = est_discharge;
	}

	@Override
	public String toString() {
		return "VesselPlan [referenceId=" + referenceId + ", enShipName="
				+ enShipName + ", vesselActionCode=" + vesselActionCode
				+ ", localtionName=" + localtionName + ", planTime=" + planTime
				+ ", previousPort=" + previousPort + ", nextPort=" + nextPort
				+ ", comments=" + comments + "]";
	}

	public VesselPlan(String referenceId, String enShipName,
			Integer vesselActionCode, String localtionName, Date planTime,
			String previousPort, String nextPort, String comments) {
		super();
		this.referenceId = referenceId;
		this.enShipName = enShipName;
		this.vesselActionCode = vesselActionCode;
		this.localtionName = localtionName;
		this.planTime = planTime;
		this.previousPort = previousPort;
		this.nextPort = nextPort;
		this.comments = comments;
	}

	
	

}
