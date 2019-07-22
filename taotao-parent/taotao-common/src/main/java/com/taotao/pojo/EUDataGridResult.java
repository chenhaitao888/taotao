package com.taotao.pojo;

import java.util.List;
/**
 * 支持EasyUIDateGrid格式的实体类
 * <p>Title: EUDataGridResult</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月17日下午5:25:05
 * @version:1.0
 */
public class EUDataGridResult {
	private long total;
	private List<?> rows;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
}
