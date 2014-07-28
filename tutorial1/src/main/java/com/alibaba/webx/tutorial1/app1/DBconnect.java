package com.alibaba.webx.tutorial1.app1;

import java.io.Reader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import com.dips.demo.User;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class DBconnect {
	private static Connection con;
	public static SqlMapClient getSql(){
		try{
		Reader reader = Resources.getResourceAsReader("sqlmapconfig.xml");  
		  SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
		  return sqlMap;
		  }catch(Exception e){
			  System.out.println("无法找到配置文件");
		  }
		  return null;
	}
	public static void init(){
	try{
		Class.forName("com.mysql.jdbc.Driver");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("�޷�װ����");
		}
	try{
	String url="jdbc:mysql://localhost:3306/webconnect?user=root&password=csu*#netlab";
	 con = DriverManager.getConnection(url);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("���ò������");
		}
}	
	public static ResultSet query(String str){
		if(con==null){
			init();
		}
		try{
		Statement stat=con.createStatement();
		ResultSet rs=stat.executeQuery(str);
		return rs;}catch(Exception e){
			System.out.println("��ѯ������");
			return null;
		}
	}
	public static void exec(String str){
		if(con==null){
			init();
		}
		try{
		Statement stat=con.createStatement();
		stat.execute(str);
		}catch(Exception e){
			System.out.println("��ѯ������");
			e.printStackTrace();
		}
	}
//	public static boolean CheckUser(String username,String password){
//		String str="SELECT * FROM USER WHERE username='"+username+"' and password='"+password+"'";
//		ResultSet rs=query(str);
//		try{
//		if(rs.next()){
//			return true;
//		}}catch(Exception e){
//			e.printStackTrace();
//		}
//		return false;
//		
//	}
//	public static boolean InsertUser(String username,String password){
//		String str="SELECT * FROM USER WHERE username='"+username+"'";
//		System.out.println(str);
//		ResultSet rs=query(str);
//		try{
//		if(rs.next()){
//			return false;
//		}else{
//			str="INSERT INTO user (`username`, `password`) VALUES ('"+username+"', '"+password+"')";
//			System.out.println(str);
//			DBconnect.exec(str);
//		}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return true;
//	}
	public static boolean CheckUser(String username,String password){
		SqlMapClient sql=getSql();
		try {
			List<User> list=(List<User>)sql.queryForList("user.selectUserByName", username);
			for(User u:list){
				if(u.getPassword().equals(password)){
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	public static boolean InsertUser(String username,String password){
		SqlMapClient sql=getSql();
		try{
			List<User> list=(List<User>)sql.queryForList("user.selectUserByName", username);
			if(list.size()>0){
				return false;
			}else{
				User u=new User();
				u.setPassword(password);
				u.setUsername(username);
				sql.insert("user.addUser", u);
				return true;
			}
		}catch(Exception e){
			
		}
		return false;
	}
	
}
