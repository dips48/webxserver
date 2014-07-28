package com.dips.demo;


import java.io.Serializable;

public class FileUpload implements Serializable{
	private String username="";
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	private static final long serialVersionUID = 1L;
	private String FileName;
	private String content;
	private String FilePath;
	private int operation;//1��� 2��ѯ 3ɾ��
	public int isOperation() {
		return operation;
	}
	public void setOperation(int operation) {
		this.operation = operation;
	}
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFilePath() {
		return FilePath;
	}
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
}
