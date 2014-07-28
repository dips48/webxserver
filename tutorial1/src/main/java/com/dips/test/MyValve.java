package com.dips.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.Valve;

public class MyValve implements Valve{
//	@Autowired
//	HttpServletRequest request;
	public void invoke(PipelineContext pipelineContext) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("执行了吗？");
//		System.out.println(request.getRemoteAddr()+request.getRemotePort());
		pipelineContext.invokeNext();
	}

}
