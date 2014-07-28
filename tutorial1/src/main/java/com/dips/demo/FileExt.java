package com.dips.demo;


import java.io.Serializable;
import java.util.ArrayList;

public class FileExt implements Serializable{
	private String username="";
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	private static final long serialVersionUID = 1L;
	private String content="";
	private String name="";
	private String path="";
	public ArrayList<String> subFile=new ArrayList<String>();
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public ArrayList<String> getSubFile() {
		return subFile;
	}
	public void setSubFile(ArrayList<String> subFile) {
		this.subFile = subFile;
	}

}
