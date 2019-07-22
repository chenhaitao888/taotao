package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.EUDataGridResult;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.result.TaotaoResult;
import com.taotao.service.ContentService;
import com.taotao.utils.HttpClientUtil;
/**
 * 内容管理
 * <p>Title: ContentServiceImpl</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年2月28日上午10:31:48
 * @version:1.0
 */
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_CONTENT_SYNC_URL}")
	private String REST_CONTENT_SYNC_URL;
	/**
	 * 分页显示内容
	 * <p>Title: showContent</p>
	 * <p>Description: </p>
	 * @param page
	 * @param rows
	 * @return: EUDataGridResult
	 */
	@Override
	public EUDataGridResult showContent(int page, int rows, Long categoryId) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		PageHelper.startPage(page, rows);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		if(list != null && list.size() > 0){
			// 创建一个返回值对象
			EUDataGridResult result = new EUDataGridResult();
			result.setRows(list);
			// 取记录总条数
			PageInfo<TbContent> pageInfo = new PageInfo<>(list);
			result.setTotal(pageInfo.getTotal());
			return result;
		}
		return null;
	}
	
	/**
	 * 增加内容管理
	 * <p>Title: addContent</p>
	 * <p>Description: </p>
	 * @param tbContent
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult addContent(TbContent tbContent) {
		try {
			tbContent.setCreated(new Date());
			tbContent.setUpdated(new Date());
			contentMapper.insert(tbContent);
			return TaotaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "系统发生异常");
		}
	}

	
	/**
	 * 修改内容管理
	 * <p>Title: editContent</p>
	 * <p>Description: </p>
	 * @param tbContent
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult editContent(TbContent tbContent) {
		try {
			tbContent.setCreated(new Date());
			tbContent.setUpdated(new Date());
			contentMapper.updateByPrimaryKey(tbContent);
			/**
			 * 添加缓存同步逻辑
			 * @author chenhaitao
			 * @date 2017.03.03
			 */
			try {
				HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return TaotaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "系统发生异常");
		}
	}
}
