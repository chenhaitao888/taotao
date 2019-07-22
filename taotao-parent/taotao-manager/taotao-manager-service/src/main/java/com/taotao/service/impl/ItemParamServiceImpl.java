package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.EUDataGridResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamExample.Criteria;
import com.taotao.result.TaotaoResult;
import com.taotao.service.ItemParamService;
/**
 * 商品的规格参数模板
 * <p>Title: ItemParamServiceImpl</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月22日下午6:42:32
 * @version:1.0
 */
@Service
public class ItemParamServiceImpl implements ItemParamService {
	@Autowired
	private TbItemParamMapper itemParamMapper;
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	/**
	 * 根据cid查询商品规格参数模板是否存在
	 * <p>Title: getItemParamByCid</p>
	 * <p>Description: </p>
	 * @param cid
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult getItemParamByCid(long cid) {
		TbItemParamExample example = new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(cid);
		//List<TbItemParam> list = itemParamMapper.selectByExample(example);//可以查看itemMapper文件,可以看到selectByExample并没有查询paramData这个字段
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);//此方法包含paramData这个字段
		//判断是否查询到结果
		if(list != null && list.size() > 0){
			return TaotaoResult.ok(list.get(0));
		}
		return TaotaoResult.ok();
	}
	
	/**
	 * 将生成的模板存入到tb_item_param表中
	 * <p>Title: insertItemParam</p>
	 * <p>Description: </p>
	 * @param cid
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult insertItemParam(TbItemParam itemParam) {
		// 补全pojo
		itemParam.setCreated(new Date());
		itemParam.setUpdated(new Date());
		// 插入到规格参数模板表
		itemParamMapper.insert(itemParam);
		return TaotaoResult.ok();
	}

	
	/**
	 * 显示规格参数模板
	 * <p>Title: showItemParam</p>
	 * <p>Description: </p>
	 * @param page
	 * @param rows
	 * @return: EUDataGridResult
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public EUDataGridResult showItemParam(int page, int rows) {
		//创建一个用于存放map的List
		List<Map> mapList = new ArrayList<Map>();
		//查询参数模板列表
		TbItemParamExample example = new TbItemParamExample();
		TbItemCatExample catExample = new TbItemCatExample();
		com.taotao.pojo.TbItemCatExample.Criteria criteria = catExample.createCriteria();
		String name = "";
		// 分页处理
		PageHelper.startPage(page, rows);
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
		for (TbItemParam tbItemParam : list) {
			Long itemCatId = tbItemParam.getItemCatId();
			criteria.andIdEqualTo(itemCatId);
			//查询tb_item_cat表
			List<TbItemCat> list2 = itemCatMapper.selectByExample(catExample);
			if(list2 != null && list2.size() > 0){
				TbItemCat tbItemCat = list2.get(0);
				//得到商品类目名称
				name = tbItemCat.getName();
			}
			//将要前台要显示的字段存在map中
			Map map = new HashMap();
			map.put("id", tbItemParam.getId());
			map.put("itemCatId", itemCatId);
			map.put("itemCatName", name);
			map.put("paramData", tbItemParam.getParamData());
			map.put("created", tbItemParam.getCreated());
			map.put("updated", tbItemParam.getUpdated());
			//把map存放list中,用来返回带前端
			mapList.add(map);
		}
		// 创建一个返回值对象
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(mapList);
		// 取记录总条数
		PageInfo<Map> pageInfo = new PageInfo<Map>(mapList);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

}
