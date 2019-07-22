package com.taotao.portal.pojo;

import com.taotao.pojo.TbItem;
/**
 * 解决商品基本信息图片不能显示的bug
 * <p>Title: ItemInfo</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2017年3月5日下午11:32:44
 * @version:1.0
 */
public class ItemInfo extends TbItem{
	public String[] getImages() {
		String image = getImage();
		if (image != null) {
			String[] images = image.split(",");
			return images;
		}
		return null;
	}

}
