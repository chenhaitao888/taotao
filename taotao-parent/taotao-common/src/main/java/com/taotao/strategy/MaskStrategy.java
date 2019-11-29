package com.taotao.strategy;

import java.lang.reflect.Field;

/**
 * 掩码策略接口
 * <p>Title: MaskStrategy</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年7月24日下午8:39:30
 * @version:1.0
 */
@FunctionalInterface
public interface MaskStrategy {
	void mask() throws Exception;
}
