package com.taotao.service;

import com.taotao.pojo.EUDataGridResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.result.TaotaoResult;
/**
 * 商品的规格参数模板
 * <p>Title: ItemParmService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月22日下午6:40:02
 * @version:1.0
 */
public interface ItemParamService {
	/**
	 * 根据cid查询商品规格参数模板是否存在
	 * <p>Title: getItemParamByCid</p>
	 * <p>Description: </p>
	 * @param cid
	 * @return: TaotaoResult
	 */
	TaotaoResult getItemParamByCid(long cid);
	/**
	 * 将生成的模板存入到tb_item_param表中
	 * <p>Title: insertItemParam</p>
	 * <p>Description: </p>
	 * @param cid
	 * @return: TaotaoResult
	 */
	TaotaoResult insertItemParam(TbItemParam itemParam);
	
	/**
	 * 显示规格参数模板
	 * <p>Title: showItemParam</p>
	 * <p>Description: </p>
	 * @param page
	 * @param rows
	 * @return: EUDataGridResult
	 */
	EUDataGridResult showItemParam(int page,int rows);
}
