package com.taotao.pojo;

import com.alibaba.fastjson.JSON;

public class FastJsonTest {
	public static void main(String[] args) {
		Student s = new Student();
		s.setAddress("上海");
		s.setName("小明");
		System.out.println(JSON.toJSONString(s));
	}
}

 class Student{
	private String name;
	private String address;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	//@JSONField(serialize = false)
	public String getJson(){
		return JSON.toJSONString(this);
	}
}
