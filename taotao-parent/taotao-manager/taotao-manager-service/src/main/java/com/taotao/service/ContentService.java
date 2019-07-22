package com.taotao.service;

import com.taotao.pojo.EUDataGridResult;
import com.taotao.pojo.TbContent;
import com.taotao.result.TaotaoResult;

/**
 * 内容管理
 * <p>Title: ContentService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月28日上午9:51:54
 * @version:1.0
 */
public interface ContentService {
	/**
	 * 分页显示内容
	 * <p>Title: showContent</p>
	 * <p>Description: </p>
	 * @param page
	 * @param rows
	 * @return: EUDataGridResult
	 */
	EUDataGridResult showContent(int page, int rows, Long categoryId);
	/**
	 * 增加内容管理
	 * <p>Title: addContent</p>
	 * <p>Description: </p>
	 * @param tbContent
	 * @return: TaotaoResult
	 */
	TaotaoResult addContent(TbContent tbContent);
	/**
	 * 修改内容管理
	 * <p>Title: editContent</p>
	 * <p>Description: </p>
	 * @param tbContent
	 * @return: TaotaoResult
	 */
	TaotaoResult editContent(TbContent tbContent);
}
