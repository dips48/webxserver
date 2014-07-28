package com.alibaba.webx.tutorial1.app1.module.screen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.dips.demo.FileExt;
import com.dips.demo.FileUpload;

public class Home {
	@Autowired
	HttpServletRequest request;
	public void execute(@Param("name") String name, Context context) {
		FileUpload fu=new FileUpload();
		if(request.getParameter("FileName")!=null){
		fu.setFileName(request.getParameter("FileName"));}
		if(request.getParameter("FilePath")!=null){
		fu.setFilePath(request.getParameter("FilePath"));}
		if(request.getParameter("UserName")!=null){
		fu.setUsername(request.getParameter("UserName"));}
		fu.setOperation(2);
		FileExt fe=getObject(fu);
		request.getSession().setAttribute("FE", fe);
		context.put("FE", fe);
    }
	//send request and get response
	public FileExt getObject(Object obj){
		FileExt fe=null;
		try {
			//send the quest object
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
		// get the reponse object
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
}
