package com.taotao.rest.pojo;

import java.util.List;

/**
 * 前台商品分类的json根节点
 * <p>Title: CatResult</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月26日下午9:53:25
 * @version:1.0
 */
public class CatResult {
	private List<?> data;

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}
	
}
