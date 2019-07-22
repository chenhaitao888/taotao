package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.pojo.TreeNode;
import com.taotao.result.TaotaoResult;
import com.taotao.service.ContentCategoryService;

/**
 * 内容分类管理(CMS系统)
 * <p>Title: ContentCategoryServiceImpl</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月27日下午2:51:05
 * @version:1.0
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	/**
	 * 取内容分类树
	 * <p>Title: getCategoryList</p>
	 * <p>Description: </p>
	 * @param parentId
	 * @return: List<TreeNode>
	 */
	@Override
	public List<TreeNode> getCategoryList(Long parentId) {
		//根据parentid查询节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> contentCategory = contentCategoryMapper.selectByExample(example);
		List<TreeNode> list = new ArrayList<TreeNode>();
		if(contentCategory != null && contentCategory.size() > 0){
			for (TbContentCategory tbContentCategory : contentCategory) {
				//创建TreeNode节点
				TreeNode treeNode = new TreeNode(tbContentCategory.getId(), 
						tbContentCategory.getName(), tbContentCategory.getIsParent()?"closed":"open");
				//把几点添加到list中并返回
				list.add(treeNode);
			}
			return list;
		}
		
		return null;
	}
	
	/**
	 * 新增内容管理分类节点
	 * <p>Title: insertContentCategory</p>
	 * <p>Description: </p>
	 * @param parentId
	 * @param name
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult insertContentCategory(long parentId, String name) {
		/**
		 * 
		 * 在com.taotao.mapper.TbContentCategoryMapper文件下,增加主键返回
		 * <selectKey keyProperty="id" resultType="long" order="AFTER">
  		 *	SELECT LAST_INSERT_ID()
  		 *	</selectKey>
		 */
		TbContentCategory tbContentCategory = new TbContentCategory();
		tbContentCategory.setName(name);
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setIsParent(false);
		//'状态。可选值:1(正常),2(删除)',
		tbContentCategory.setStatus(1);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		contentCategoryMapper.insert(tbContentCategory);
		//查看父节点的isParent是否为true,如果不是true则改为true
		TbContentCategory tbCategoryParent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(tbCategoryParent != null){
			if(!tbCategoryParent.getIsParent()){
				tbCategoryParent.setIsParent(true);
				contentCategoryMapper.updateByPrimaryKey(tbCategoryParent);
			}
		}
		return TaotaoResult.ok(tbContentCategory);
	}

	/**
	 * 重新命名内容管理节点
	 * <p>Title: renameContentCategory</p>
	 * <p>Description: </p>
	 * @param id
	 * @param name: void
	 */
	@Override
	public void renameContentCategory(Long id, String name) {
		//根据id查询出内容
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		if(contentCategory != null){
			//更新数据库
			contentCategory.setName(name);
			contentCategoryMapper.updateByPrimaryKey(contentCategory);
		}
	}
	
	/**
	 * 删除内容管理节点
	 * <p>Title: delContentCategory</p>
	 * <p>Description: </p>
	 * @param id
	 * @param parentId: void
	 */
	@Override
	public void delContentCategory(Long id, Long parentId) {
		contentCategoryMapper.deleteByPrimaryKey(id);
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(contentCategory != null){
			//根据父节点的id查询这个父节点下是否还有孩子
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(contentCategory.getId());
			List<TbContentCategory> tbCategories = contentCategoryMapper.selectByExample(example);
			//如果父节点为空的话,则将父节点设为叶子节点
			if(tbCategories == null || tbCategories.size() <= 0){
				contentCategory.setIsParent(false);
			}
		}
	}
}
