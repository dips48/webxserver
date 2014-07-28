package com.dips.demo;

import java.io.Serializable;

public class User implements Serializable{
	private int iduser;
	private String username;
	private String password;
	public int getIduser() {
		return iduser;
	}
	public void setIduser(int iduser) {
		this.iduser = iduser;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
