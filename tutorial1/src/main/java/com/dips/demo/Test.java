package com.dips.demo;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.List;

import com.ibatis.common.resources.Resources;  
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class Test {
	public static void main(String args[]) throws IOException, SQLException{
		Reader reader = Resources.getResourceAsReader("sqlmapconfig.xml");  
		  SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
		  User u=new User();
		  u.setUsername("4444444444444444444");
		  u.setPassword("444444444444444");
		  sqlMap.insert("user.addStudent",u );
		  List<User> ptList = (List<User>)sqlMap.queryForList("user.getAll",null);
		  for(User pt:ptList){  
		    System.out.println(pt.getIduser()+" - "+pt.getUsername()+"---"+pt.getPassword());  
		   } 
	}
}
