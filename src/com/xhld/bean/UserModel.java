package com.xhld.bean;

public class UserModel {

	public int id;
	public String name;
	public String number;
	public String sortKey;
	public String mobile;
	public String password;
	public String accCodeId;
	public String accCodePwd;
	public Customer customer;
	
	public class Customer {
		public long id;
		public String name;	//代理商名称
		public String no;	//代理商编号
		public Long accCodeId;//接入码ID
		public String thum;//代理商LOGO
		public String description;//代理商简介
	}
}
