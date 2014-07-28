/*
 * Copyright 2010 Alibaba Group Holding Limited.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.alibaba.webx.tutorial1.app1.module.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.form.Form;
import com.alibaba.citrus.service.form.FormService;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.FormGroup;
import com.alibaba.webx.tutorial1.app1.DBconnect;
import com.alibaba.webx.tutorial1.app1.EditObject;
import com.alibaba.webx.tutorial1.app1.LoginObject;
import com.alibaba.webx.tutorial1.app1.UploadObject;
import com.dips.demo.FileExt;
import com.dips.demo.FileUpload;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SimpleAction {
	//cookie
	//@Autowired
	//HttpServletRequest req;
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	private FormService formService;
	
	public void sendObject(Object obj)  {
		try{
		SocketChannel sc=SocketChannel.open();
		sc.connect(new InetSocketAddress(2001));
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		ObjectOutputStream oout=new ObjectOutputStream(out);
		oout.writeObject(obj);
		oout.flush();
		byte[] obArray=out.toByteArray();
		ByteBuffer buf=ByteBuffer.allocate(obArray.length);
		buf.put(obArray);
		buf.flip();
		sc.write(buf);
		sc.close();}catch(IOException e){
			System.out.println("无法连接服务器");
			e.printStackTrace();
		}
	}
	public FileExt getObject(Object obj){
		FileExt fe=null;
		try {
		SocketChannel sc=SocketChannel.open();
		sc.connect(new InetSocketAddress(2001));
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		ObjectOutputStream oout=new ObjectOutputStream(out);
		oout.writeObject(obj);
		oout.flush();
		byte[] obArray=out.toByteArray();
		ByteBuffer buf=ByteBuffer.allocate(obArray.length);
		buf.put(obArray);
		buf.flip();
		sc.write(buf);
		
		ByteBuffer obBuf=ByteBuffer.allocate(2000);
		int size=sc.read(obBuf);
		byte[] obarray=obBuf.array();
		ByteArrayInputStream in=new ByteArrayInputStream(obarray,0,size);
		ObjectInputStream oin=new ObjectInputStream(in);
	
			fe=(FileExt)oin.readObject();
		} catch(IOException e){
		System.out.println("无法进行连接");
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("类型无法转换");
			e.printStackTrace();
		}
		return fe;
	}
    //验证用户名和密码
    public void doCheck(@FormGroup("login") LoginObject param,Navigator nav,Context context){
    	String str=param.getName();
    	String pwd=param.getPasswd();
    	if(str.equals("")|pwd.equals("")){
    		context.put("errorMsg", "Name or password is valid");
			return;
		}
		if(DBconnect.CheckUser(str, pwd)){
			nav.redirectTo("app1Link").withTarget("home").withParameter("UserName",str).withParameter("FilePath", "FileServer").withParameter("FileName",str);
		}else{
			context.put("errorMsg", "用户名或者密码错误");
			return;	
		}
    }
    //新用户注册
    public void doRegister(@FormGroup("register") LoginObject param,Navigator nav,Context context){
    	String str=param.getName();
    	String pwd=param.getPasswd();
    	if(str.equals("")|pwd.equals("")){
    		context.put("errorMsg", "Name or password is valid");
			return;
		}
		if(DBconnect.InsertUser(str, pwd)){
			FileUpload fu=new FileUpload();
			fu.setFilePath("FileServer");
			fu.setFileName(str);
			fu.setOperation(1);
			fu.setContent(" ");
			sendObject(fu);
			nav.redirectTo("app1Link").withTarget("home").withParameter("UserName",str).withParameter("FilePath", "FileServer").withParameter("FileName",str);
		}else{
			context.put("errorMsg", "用户名已被注册");
			return;
		}
    }
    //删除文件
    public void doDelete(@FormGroup("show") Navigator nav,Context context){
    	FileExt fe=(FileExt)request.getSession().getAttribute("FE");
		String username=fe.getUsername();
		String path=fe.getPath();
		String filename=fe.getName();
		FileUpload fu=new FileUpload();
		fu.setFileName(filename);
		fu.setFilePath(path);
		fu.setUsername(username);
		fu.setOperation(3);
		sendObject(fu);
		nav.redirectTo("app1Link").withTarget("home").withParameter("UserName",username).withParameter("FilePath", path).withParameter("FileName",filename);
    }
    //返回上一级
    public void doReturn(@FormGroup("show") Navigator nav,Context context){
    		FileExt fe=(FileExt)request.getSession().getAttribute("FE");
    		String username=fe.getUsername();
    		String path=fe.getPath();
    		String filename=fe.getName();
    		int index=path.lastIndexOf('/');
    	if(index>0){
    		filename=path.substring(index+1);
    		path=path.substring(0,index);
			nav.redirectTo("app1Link").withTarget("home").withParameter("UserName",username).withParameter("FilePath", path).withParameter("FileName",filename);
    	}
    }
    //编辑
    public void doEdit(@FormGroup("show") Navigator nav,Context context){
    	FileExt fe=(FileExt)request.getSession().getAttribute("FE");
		String username=fe.getUsername();
		String path=fe.getPath();
		String filename=fe.getName();
		nav.redirectTo("app1Link").withTarget("edit").withParameter("UserName",username).withParameter("FilePath", path).withParameter("FileName",filename);
    }
    //添加
    public void doAdd(@FormGroup("show") Navigator nav,Context context){
    	FileExt fe=(FileExt)request.getSession().getAttribute("FE");
		String username=fe.getUsername();
		String path=fe.getPath();
		String filename=fe.getName();
		nav.redirectTo("app1Link").withTarget("add").withParameter("UserName",username).withParameter("FilePath", path).withParameter("FileName",filename);
    }
    public void doUpload(@FormGroup("add") UploadObject param,Navigator nav,Context context){
    	FileExt fe=(FileExt)request.getSession().getAttribute("FE");
    	String username=fe.getUsername();
		String path=fe.getPath();
		String filename=fe.getName();
    	Form form  = formService.getForm();
    	String title=param.getTitle();
    	String content=param.getContent();
    	if (title!=null&&content!=null) {
//    		UploadObject param=(UploadObject)form.getGroup("add");
    		FileUpload fu=new FileUpload();
    		fu.setFileName(title);
    		fu.setContent(content);
    		fu.setUsername(username);
        	fu.setFilePath(path+File.separator+filename);
        	fu.setOperation(1);
        	sendObject(fu);
        	nav.redirectTo("app1Link").withTarget("home").withParameter("UserName",username).withParameter("FilePath", path).withParameter("FileName",filename);
    	} else {
    		nav.redirectTo("app1Link").withTarget("add").withParameter("UserName",username).withParameter("FilePath", path).withParameter("FileName",filename);
    	}
    }
    
    public void doConfirm(@FormGroup("edit") EditObject param,Navigator nav,Context context){
    	String str=param.getContent();
    	FileExt fe=(FileExt)request.getSession().getAttribute("FE");
    	String username=fe.getUsername();
		String path=fe.getPath();
		String filename=fe.getName();
		FileUpload fu=new FileUpload();
		fu.setFilePath(path);
		fu.setFileName(filename);
		fu.setUsername(username);
		fu.setContent(str);
		fu.setOperation(1);
		sendObject(fu);
		nav.redirectTo("app1Link").withTarget("home").withParameter("UserName",username).withParameter("FilePath", path).withParameter("FileName",filename);
    }
    public void doCancel(@FormGroup("edit") Navigator nav,Context context){
    	FileExt fe=(FileExt)request.getSession().getAttribute("FE");
		String username=fe.getUsername();
		String path=fe.getPath();
		String filename=fe.getName();
		System.out.println(username+"  "+path+"  "+filename);
		nav.redirectTo("app1Link").withTarget("home").withParameter("UserName",username).withParameter("FilePath", path).withParameter("FileName",filename);
    }
    public void doCancel2(@FormGroup("add") Navigator nav,Context context){
    	FileExt fe=(FileExt)request.getSession().getAttribute("FE");
		String username=fe.getUsername();
		String path=fe.getPath();
		String filename=fe.getName();
		nav.redirectTo("app1Link").withTarget("home").withParameter("UserName",username).withParameter("FilePath", path).withParameter("FileName",filename);
    }
    
}
