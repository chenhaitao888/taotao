package com.taotao.rest.service.ipml;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.dao.CodisManager;
import com.taotao.rest.service.ItemService;
import com.taotao.result.TaotaoResult;
import com.taotao.utils.JsonUtils;

/**
 * 商品信息管理
 * <p>Title: ItemService</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月5日下午10:34:47
 * @version:1.0
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	@Autowired
	private CodisManager codisManager;
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	/**
	 * 根据商品id查询商品信息
	 * <p>Title: getItemBaseInfo</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult getItemBaseInfo(long itemId) {
		//添加缓存逻辑
		try {
			//从缓存中取商品信息，商品id对应的信息
			String json = codisManager.get(REDIS_ITEM_KEY + ":" + itemId + ":base");
			//判断是否有值
			if (!StringUtils.isBlank(json)) {
				//把json转换成java对象
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return TaotaoResult.ok(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//从缓存中取商品信息，商品id对应的信息
		// 根据商品id查询商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		// 使用TaotaoResult包装一下
		try {
			//把商品信息添加到缓存
			codisManager.set(REDIS_ITEM_KEY + ":" + itemId + ":base", JsonUtils.objectToJson(item));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok(item);
	}
	
	/**
	 * 根据商品id取商品描述
	 * <p>Title: getItemDesc</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult getItemDesc(long itemId) {
		// 添加缓存逻辑
		try {
			// 从缓存中取商品信息，商品id对应的信息
			String json = codisManager.get(REDIS_ITEM_KEY + ":" + itemId + ":desc");
			// 判断是否有值
			if (!StringUtils.isBlank(json)) {
				// 把json转换成java对象
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return TaotaoResult.ok(itemDesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 从缓存中取商品信息，商品id对应的信息
		// 根据商品id查询商品信息
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		// 使用TaotaoResult包装一下
		try {
			// 把商品信息添加到缓存
			codisManager.set(REDIS_ITEM_KEY + ":" + itemId + ":desc", JsonUtils.objectToJson(itemDesc));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok(itemDesc);
	}

	/**
	 * 根据商品id取商品规格参数
	 * <p>Title: getItemParam</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @return: TaotaoResult
	 */
	@Override
	public TaotaoResult getItemParam(long itemId) {
		// 添加缓存
		try {
			// 添加缓存逻辑
			// 从缓存中取商品信息，商品id对应的信息
			String json = codisManager.get(REDIS_ITEM_KEY + ":" + itemId + ":param");
			// 判断是否有值
			if (!StringUtils.isBlank(json)) {
				// 把json转换成java对象
				TbItemParamItem paramItem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
				return TaotaoResult.ok(paramItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 根据商品id查询规格参数
		// 设置查询条件
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		// 执行查询
		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			TbItemParamItem paramItem = list.get(0);
			try {
				// 把商品信息写入缓存
				codisManager.set(REDIS_ITEM_KEY + ":" + itemId + ":param", JsonUtils.objectToJson(paramItem));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return TaotaoResult.ok(paramItem);
		}
		return TaotaoResult.build(400, "无此商品规格");
	}
}
