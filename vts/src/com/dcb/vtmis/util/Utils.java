package com.dcb.vtmis.util;

public class Utils {

	/**
	 * 判断装卸箱是否为空，此方法并不通用
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
