package com.dcb.vtmis.util;

public class Utils {

	/**
	 * �ж�װж���Ƿ�Ϊ�գ��˷�������ͨ��
	 * @param parameter
	 * @return
	 */
	public static Boolean isNotNull(String parameter){
		if(parameter != null && parameter.length() > 0 && !parameter.equals("0")){
			return true;
		}
		return false;
	}
}