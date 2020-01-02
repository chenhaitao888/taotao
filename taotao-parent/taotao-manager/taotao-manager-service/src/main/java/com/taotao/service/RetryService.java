package com.taotao.service;

import java.util.List;
import java.util.Map;

import com.taotao.exception.TaoTaoException;

public interface RetryService {
	Map<String, Object> retryTest(List<String> list) throws Exception;
	void Test1();
	void Test2();
	void test3(int i, int qcount, int evNum) throws NullPointerException;
	//Boolean testRe();
	void test5();
	void test6(int i);
}
