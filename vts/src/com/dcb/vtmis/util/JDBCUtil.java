package com.dcb.vtmis.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 * jdbc连接工具
 * @author alan.wang
 *
 */
public class JDBCUtil {
	private static Properties pro = PropertiesUtil.getProperties("/vtmis.properties");
	private static final Logger log = Logger.getLogger(JDBCUtil.class); 
	private static String url = pro.getProperty("jdbc.url");
	private static String username = pro.getProperty("jdbc.username");
	private static String pwd = pro.getProperty("jdbc.password");
	
	private static String url1 = pro.getProperty("jdbc.url1");
	private static String username1 = pro.getProperty("jdbc.username1");
	private static String pwd1 = pro.getProperty("jdbc.password1");
	
	private static String url2 = pro.getProperty("jdbc.url2");
	private static String username2 = pro.getProperty("jdbc.username2");
	private static String pwd2 = pro.getProperty("jdbc.password2");
	
	public static Connection getConnection() {
		try {
			Class.forName(pro.getProperty("jdbc.driver"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, pwd);
			log.info("数据库连接成功。");
		} catch (SQLException e) {
			log.error("数据库连接异常 "+e.getMessage(),e);
		}
		return conn;
	}
	
	/**
	 * 存取船舶计划
	 * @return
	 */
	public static Connection getConnection1() {//for test
		try {
			Class.forName(pro.getProperty("jdbc.driver"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url1, username1, pwd1);
			//log.info("数据库连接成功。");
		} catch (SQLException e) {
			log.error("数据库连接异常 "+e.getMessage(),e);
		}
		return conn;
	}
	
	/**
	 * 获取上下港信息
	 * @return
	 */
	public static Connection getConnection2() {//for test
		try {
			Class.forName(pro.getProperty("jdbc.driver"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url2, username2, pwd2);
			//log.info("数据库连接成功。");
		} catch (SQLException e) {
			log.error("数据库连接异常 "+e.getMessage(),e);
		}
		return conn;
	}
}
