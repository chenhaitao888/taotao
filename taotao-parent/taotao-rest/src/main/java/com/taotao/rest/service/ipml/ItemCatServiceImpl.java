package com.taotao.rest.service.ipml;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
/**
 * 前台商品分类服务
 * <p>Title: ItemCatServiceImpl</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月26日下午9:59:03
 * @version:1.0
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper itemCatMapper;
	/**
	 * 取商品分类
	 * <p>Title: getItemCatList</p>
	 * <p>Description: </p>
	 * @return: CatResult
	 */
	@Override
	public CatResult getItemCatList() {
		CatResult catResult = new CatResult();
		//查询分类列表
		catResult.setData(getCatList(0));
		return catResult;
	}
	/**
	 * 查询分类列表
	 * <p>Title: getCatList</p>
	 * <p>Description: </p>
	 * @param parentId
	 * @return: List<?>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<?> getCatList(long parentId){
		// 创建查询条件
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		// 返回值list
		List resultList = new ArrayList<>();
		int count = 0;
		//向resultList中添加节点
		for (TbItemCat tbItemCat : list) {
			//如果是父节点
			if(tbItemCat.getIsParent()){
				/*给节点赋值*/
				CatNode catNode = new CatNode();
				//如果是第一层
				if(parentId == 0){
					catNode.setName("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
				}
				//如果是第二层
				else {
					catNode.setName(tbItemCat.getName());
				}
				catNode.setUrl("/products/"+tbItemCat.getId()+".html");
				catNode.setItem(getCatList(tbItemCat.getId()));
				resultList.add(catNode);
			}
			//如果是叶子节点
			else{
				resultList.add("/products/"+tbItemCat.getId()+".html|" + tbItemCat.getName());
			}
			count++;
			//第一层只取14条记录,因为前台只能显示14条
			if(parentId == 0 && count == 14){
				break;
			}
		}
		return resultList;
	}
}
